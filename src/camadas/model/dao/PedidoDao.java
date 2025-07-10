package camadas.model.dao;

import camadas.model.domain.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PedidoDao {

    public void create(Pedido pedido, Boolean isVenda){
        String insertPedido = """
                insert into pedidos (idcliente, valorTotal, dataRegistro, dataEntrega, cod, isFinalizado)
                values (?,?,?,?,?,?);
                """;
        int idPedido = 0;
        String getClienteByCod = "select id from clientes where cod = ?";
        int idCliente = 0;
        String insertItemProduto = "insert into ItemProduto (idProduto, idPedido, quantidade) values (?, ?, ?);";
        String getIdProduto = "select id from produtos where cod = ?";
        String getQuantidadeMaterialEstoque = "select quantidadeEstoque from materiais where cod = ?";
        String updateMaterialEstoque = "update materiais set quantidadeEstoque = ? where cod = ?";

        try{
            Database db = new Database();
            db.connection.setAutoCommit(false);
            try{
                try(PreparedStatement getCliente = db.connection.prepareStatement(getClienteByCod)){
                    getCliente.setString(1, pedido.getCliente().getCod());
                    try(ResultSet resultSet = getCliente.executeQuery()){
                        idCliente = resultSet.getInt("id");
                    }
                }

                try(PreparedStatement insertP = db.connection.prepareStatement(insertPedido, Statement.RETURN_GENERATED_KEYS)){
                    insertP.setInt(1, idCliente);
                    insertP.setFloat(2, pedido.getValorTotal());
                    insertP.setString(3, pedido.getDataDeRegistro().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    insertP.setString(4, pedido.getDataEntrega().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    insertP.setString(5, pedido.getCod());
                    if(isVenda){
                        insertP.setInt(6, 1);
                    }else {
                        insertP.setInt(6, 0);
                    }
                    insertP.executeUpdate();

                    try(ResultSet getKey = insertP.getGeneratedKeys()){
                        idPedido = getKey.getInt(1);
                    }
                }

                for(ItemProduto item : pedido.getProdutos()){
                    try(PreparedStatement getProduto = db.connection.prepareStatement(getIdProduto)){
                        getProduto.setString(1, item.getProduto().getCod());
                        try(ResultSet resultSetProdutoId = getProduto.executeQuery()){
                            try(PreparedStatement insertIP = db.connection.prepareStatement(insertItemProduto)){
                                insertIP.setInt(1, resultSetProdutoId.getInt("id"));
                                insertIP.setInt(2, idPedido);
                                insertIP.setInt(3, item.getQuantidade());
                                insertIP.executeUpdate();
                            }
                        }
                    }
                }

                for(ItemProduto itemProduto : pedido.getProdutos()){
                    for(ItemMaterial itemMaterial : itemProduto.getProduto().getItensMateriais()){
                        try(PreparedStatement getQuantidadeEstoque = db.connection.prepareStatement(getQuantidadeMaterialEstoque)){
                            getQuantidadeEstoque.setString(1, itemMaterial.getMaterial().getCod());
                            try(ResultSet resultSet = getQuantidadeEstoque.executeQuery()){
                                try(PreparedStatement updateMaterial = db.connection.prepareStatement(updateMaterialEstoque)){
                                    int valorFinalEstoque = (resultSet.getInt("QuantidadeEstoque") - (itemMaterial.getQuantidade() * itemProduto.getQuantidade()));
                                    if(valorFinalEstoque < 0){
                                        throw new SQLException("Quantidade em estoque de material ficará negativa");
                                    } else {
                                        updateMaterial.setInt(1, valorFinalEstoque);
                                        updateMaterial.setString(2, itemMaterial.getMaterial().getCod());
                                        updateMaterial.executeUpdate();
                                    }
                                }
                            }
                        }
                    }
                }

                db.connection.commit();
            }catch (SQLException e){
                try{
                    db.connection.rollback();
                }catch (SQLException rollbackEx){
                    throw new SQLException("Erro ao realizar rollback", e);
                }
            }finally {
                db.connection.close();
            }
        }catch (SQLException e){
            throw new RuntimeException("Erro ao cadastra pedido", e);
        }
    }

    public void delete (String cod){
        String deletePedidoByCod = "delete from pedidos where id = ?";
        String deleteItemProdutosByIdProduto = "delete from ItemProduto where idProduto = ?";
        String getIdProduto = "select id from produtos where cod = ?";
        int idProduto = 0;
        String getIsFinalizado = "select isFinalizado from pedidos where cod = ?";
        String getQuantidadeMaterialEstoque = "select quantidadeEstoque from materiais where cod = ?";
        String updateMaterialEstoque = "update materiais set quantidadeEstoque = ? where cod = ?";

        try{
            Database db = new Database();
            db.connection.setAutoCommit(false);

            try{
                try(PreparedStatement getId = db.connection.prepareStatement(getIdProduto)){
                    getId.setString(1, cod);
                    try(ResultSet result = getId.executeQuery()){
                        idProduto = result.getInt("id");
                    }
                }

                try(PreparedStatement getF = db.connection.prepareStatement(getIsFinalizado)){
                    getF.setString(1, cod);
                    try(ResultSet resultF = getF.executeQuery()){
                        if (resultF.getInt("isFinalizado") == 0){
                            Pedido pedido = new Pedido(this.get(cod));
                            for(ItemProduto itemProduto : pedido.getProdutos()){
                                for(ItemMaterial itemMaterial : itemProduto.getProduto().getItensMateriais()){
                                    try(PreparedStatement getQuantidadeEstoque = db.connection.prepareStatement(getQuantidadeMaterialEstoque)){
                                        getQuantidadeEstoque.setString(1, itemMaterial.getMaterial().getCod());
                                        try(ResultSet resultSet = getQuantidadeEstoque.executeQuery()){
                                            try(PreparedStatement updateMaterial = db.connection.prepareStatement(updateMaterialEstoque)){
                                                int valorFinalEstoque = (resultSet.getInt("quantidadeEstoque") + (itemMaterial.getQuantidade() * itemProduto.getQuantidade()));
                                                updateMaterial.setInt(1, valorFinalEstoque);
                                                updateMaterial.setString(2, itemMaterial.getMaterial().getCod());
                                                updateMaterial.executeUpdate();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


                try(PreparedStatement deleteIP = db.connection.prepareStatement(deleteItemProdutosByIdProduto)){
                    deleteIP.setInt(1, idProduto);
                    deleteIP.executeUpdate();
                }

                try(PreparedStatement deleteP = db.connection.prepareStatement(deletePedidoByCod)){
                    deleteP.setInt(1, idProduto);
                    deleteP.executeUpdate();
                }

                db.connection.commit();
            }catch (SQLException e){
                try{
                    db.connection.rollback();
                }catch (SQLException rollbackEx){
                    throw new SQLException("Erro ao realizar rollback", rollbackEx);
                }
            }finally {
                db.connection.close();
            }
        }catch (SQLException e){
            throw new RuntimeException("Erro ao deletar pedido", e);
        }
    }

    public void update(String codAtingo, Pedido pedido){
        String getIdPedidoByCod = "select id from pedidos where cod = ?";
        int idPedido = 0;
        String getIdClienteByCod = "select id from clientes where cod = ?";
        int idCliente = 0;
        String getIdProdutoByCod = "select id from produtos where cod = ?";
        String updatePedido = "update pedidos set idCliente = ?, valorTotal = ?, dataRegistro = ?, dataEntrega = ?, cod = ? where id = ?";
        String deleteItemProduto = "delete from ItemProduto where idPedido = ?";
        String insertItemProduto = "insert from ItemProduto (idProduto, idPedido, quantidade) values (?, ?, ?)";

        try{
            Database db = new Database();
            db.connection.setAutoCommit(false);
            try{
                try(PreparedStatement getPedido = db.connection.prepareStatement(getIdPedidoByCod)){
                    getPedido.setString(1, codAtingo);
                    try(ResultSet resultSet = getPedido.executeQuery()){
                        idPedido = resultSet.getInt("id");
                    }
                }

                try(PreparedStatement getC = db.connection.prepareStatement(getIdClienteByCod)){
                    getC.setString(1, pedido.getCliente().getCod());
                    try(ResultSet resultSet = getC.executeQuery()){
                        idCliente = resultSet.getInt("id");
                    }
                }

                try(PreparedStatement deleteIP = db.connection.prepareStatement(deleteItemProduto)){
                    deleteIP.setInt(1, idPedido);
                    deleteIP.executeUpdate();
                }

                try(PreparedStatement updateP = db.connection.prepareStatement(updatePedido)){
                    updateP.setInt(1, idCliente);
                    updateP.setFloat(2, pedido.getValorTotal());
                    updateP.setString(3, pedido.getDataDeRegistro().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    updateP.setString(4, pedido.getDataEntrega().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    updateP.setString(5, pedido.getCod());
                    updateP.setInt(6, idPedido);
                    updateP.executeUpdate();
                }

                for(ItemProduto item : pedido.getProdutos()){
                    try(PreparedStatement getP = db.connection.prepareStatement(getIdProdutoByCod)){
                        getP.setString(1, item.getProduto().getCod());
                        try(ResultSet result = getP.executeQuery()){
                            try(PreparedStatement insertIP = db.connection.prepareStatement(insertItemProduto)){
                                insertIP.setInt(1, result.getInt("id"));
                                insertIP.setInt(2, idPedido);
                                insertIP.setInt(3, item.getQuantidade());
                                insertIP.executeUpdate();
                            }
                        }
                    }
                }

                db.connection.commit();
            }catch (SQLException e){
                try{
                    db.connection.rollback();
                }catch (SQLException rollbackEx){
                    throw new SQLException("Erro ao realizar rollback", rollbackEx);
                }
            }finally {
                db.connection.close();
            }
        }catch (SQLException e){
            throw new RuntimeException("Erro ao atualizar produto", e);
        }
    }

    public Pedido get(String cod){
        String getPedidoByCod = """
                select pedidos.id as idPedido, nome, telefone, endereco, clientes.cod as codCliente,
                        valorTotal, dataRegistro, dataEntrega, pedidos.cod as codPedido,
                        isFinalizado
                from pedidos join clientes on pedidos.idCliente = clientes.id
                where codPedido = ?;
                """;

        String getItemProdutoByIdPedido = """
                select produtos.id as idProduto, descricao, valor, margemLucro, cod,
                        quantidade
                from Itemproduto join produtos on itemproduto.idProduto = produtos.id
                where itemProduto.idPedido = ?;
                """;
        String getItemMaterialByIdProduto = """
                select descricao, valor, quantidadeEstoque, cod,
                        quantidade
                from ItemMaterial join materiais on itemMaterial.idMaterial = materiais.id
                where itemMaterial.idProduto = ?;
                """;

        Pedido pedido = new Pedido(cod);

        try{
            Database db = new Database();
            try{
                try(PreparedStatement getPedido = db.connection.prepareStatement(getPedidoByCod)){
                    getPedido.setString(1, cod);
                    try(ResultSet resultPedido = getPedido.executeQuery()){

                        pedido.setCliente(
                                new Cliente(
                                        resultPedido.getString("nome"),
                                        resultPedido.getString("telefone"),
                                        resultPedido.getString("endereco"),
                                        resultPedido.getString("codCliente")
                                )
                        );
                        pedido.setFinalizado(resultPedido.getInt("isFinalizado") == 1);
                        pedido.setDataDeRegistro(resultPedido.getString("dataRegistro"));
                        pedido.setDataEntrega(resultPedido.getString("dataEntrega"));
                        pedido.setValorTotal(resultPedido.getFloat("valorTotal"));

                        try(PreparedStatement getIP = db.connection.prepareStatement(getItemProdutoByIdPedido)){
                            getIP.setInt(1, resultPedido.getInt("idPedido"));
                            try(ResultSet resultIP = getIP.executeQuery()){
                                while (resultIP.next()){
                                    try(PreparedStatement getIM = db.connection.prepareStatement(getItemMaterialByIdProduto)){
                                        getIM.setInt(1, resultIP.getInt("idProduto"));
                                        try(ResultSet resultIM = getIM.executeQuery()){
                                            List<ItemMaterial> itemMaterialList = new ArrayList<>();
                                            while (resultIM.next()){
                                                itemMaterialList.add(
                                                        new ItemMaterial(
                                                                new Material(
                                                                        resultIM.getString("descricao"),
                                                                        resultIM.getFloat("valor"),
                                                                        resultIM.getString("cod"),
                                                                        resultIM.getInt("quantidadeEstoque")
                                                                ),
                                                                resultIM.getInt("quantidade")
                                                        )
                                                );
                                            }

                                            pedido.getProdutos().add(
                                                    new ItemProduto(
                                                            new Produto(
                                                                    resultIP.getString("descricao"),
                                                                    resultIP.getFloat("valor"),
                                                                    itemMaterialList,
                                                                    resultIP.getString("cod"),
                                                                    resultIP.getFloat("margemLucro")
                                                            ),
                                                            resultIP.getInt("quantidade")
                                                    )
                                            );
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }finally {
                db.connection.close();
            }
        }catch (SQLException e){
            throw new RuntimeException("Erro ao buscar o pedido");
        }
        return pedido;
    }

    public List<Pedido> getListNaoFinalizados(){
        String getPedidoCod = """
                select cod from pedidos where isFinalizado = 0;
                """;

        List<Pedido> pedidos = new ArrayList<>();

        try{
            Database db = new Database();
            try{
                try(ResultSet resultSet = db.connection.createStatement().executeQuery(getPedidoCod)){
                    while (resultSet.next()){
                        pedidos.add(this.get(resultSet.getString("cod")));
                    }
                }
            }finally {
                db.connection.close();
            }
        }catch (SQLException e){
            throw new RuntimeException("Erro ao buscar pedidos que não estão finalizados");
        }

        return pedidos;
    }

    public List<Pedido> getListPedidosFinalizados(){
        String getPedidoCod = """
                select cod from pedidos where isFinalizado = 1;
                """;

        List<Pedido> pedidos = new ArrayList<>();

        try{
            Database db = new Database();
            try{
                try(ResultSet resultSet = db.connection.createStatement().executeQuery(getPedidoCod)){
                    while (resultSet.next()){
                        pedidos.add(this.get(resultSet.getString("cod")));
                    }
                }
            }finally {
                db.connection.close();
            }
        }catch (SQLException e){
            throw new RuntimeException("Erro ao buscar pedidos finalizados");
        }

        return pedidos;
    }

    public List<Pedido> getSearchByNomeCliente(String nomeCliente, Integer opcao){
        String getPedidoCodByNomeCliente1 = """
                Select cod
                from pedidos join clientes on pedidos.idCliente = clientes.id
                where clientes.nome like ? and isFinalizado = 1;
                """;

        String getPedidoCodByNomeCliente2 = """
                Select cod
                from pedidos join clientes on pedidos.idCliente = clientes.id
                where clientes.nome like ? and isFinalizado = 0;
                """;

        String getPedidoCodByNomeCliente3 = """
                Select cod
                from pedidos join clientes on pedidos.idCliente = clientes.id
                where clientes.nome like ?;
                """;

        List<Pedido> pedidos = new ArrayList<>();

        try{
            Database db = new Database();
            try{
                switch (opcao){
                    case 1:
                        try(PreparedStatement getP = db.connection.prepareStatement(getPedidoCodByNomeCliente1)){
                            getP.setString(1, "%" + nomeCliente + "%");
                            try(ResultSet resultSet = getP.executeQuery()){
                                while (resultSet.next()){
                                    pedidos.add(this.get(resultSet.getString("cod")));
                                }
                            }
                        }
                        break;

                    case 2:
                        try(PreparedStatement getP = db.connection.prepareStatement(getPedidoCodByNomeCliente2)){
                            getP.setString(1, "%" + nomeCliente + "%");
                            try(ResultSet resultSet = getP.executeQuery()){
                                while (resultSet.next()){
                                    pedidos.add(this.get(resultSet.getString("cod")));
                                }
                            }
                        }
                        break;

                    case 3:
                        try(PreparedStatement getP = db.connection.prepareStatement(getPedidoCodByNomeCliente3)){
                            getP.setString(1, "%" + nomeCliente + "%");
                            try(ResultSet resultSet = getP.executeQuery()){
                                while (resultSet.next()){
                                    pedidos.add(this.get(resultSet.getString("cod")));
                                }
                            }
                        }
                        break;

                    default:
                        throw new IllegalArgumentException("Valor de opcção é inválido para o banco");
                }
            }finally {
                db.connection.close();
            }
        }catch (SQLException e){
            throw new RuntimeException("Erro ao realizar busca de pedido por nome de cliente");
        }
        return pedidos;
    }

    public List<Pedido> getSearchByDataEntrega(String nomeCliente, Integer opcao){
        String getPedidoCodByDataEntrega1 = """
                Select cod
                from pedidos
                where dataEntrega like ? and isFinalizado = 1;
                """;

        String getPedidoCodByDataEntrega2 = """
                Select cod
                from pedidos
                where dataEntrega like ? and isFinalizado = 0;
                """;

        String getPedidoCodByDataEntrega3 = """
                Select cod
                from pedidos
                where dataEntrega like ?;
                """;

        List<Pedido> pedidos = new ArrayList<>();

        try{
            Database db = new Database();
            try{
                switch (opcao){
                    case 1:
                        try(PreparedStatement getP = db.connection.prepareStatement(getPedidoCodByDataEntrega1)){
                            getP.setString(1, "%" + nomeCliente + "%");
                            try(ResultSet resultSet = getP.executeQuery()){
                                while (resultSet.next()){
                                    pedidos.add(this.get(resultSet.getString("cod")));
                                }
                            }
                        }
                        break;

                    case 2:
                        try(PreparedStatement getP = db.connection.prepareStatement(getPedidoCodByDataEntrega2)){
                            getP.setString(1, "%" + nomeCliente + "%");
                            try(ResultSet resultSet = getP.executeQuery()){
                                while (resultSet.next()){
                                    pedidos.add(this.get(resultSet.getString("cod")));
                                }
                            }
                        }
                        break;

                    case 3:
                        try(PreparedStatement getP = db.connection.prepareStatement(getPedidoCodByDataEntrega3)){
                            getP.setString(1, "%" + nomeCliente + "%");
                            try(ResultSet resultSet = getP.executeQuery()){
                                while (resultSet.next()){
                                    pedidos.add(this.get(resultSet.getString("cod")));
                                }
                            }
                        }
                        break;

                    default:
                        throw new IllegalArgumentException("Valor de opcção é inválido para o banco");
                }
            }finally {
                db.connection.close();
            }
        }catch (SQLException e){
            throw new RuntimeException("Erro ao realizar busca de pedido por nome de cliente");
        }
        return pedidos;
    }
}
