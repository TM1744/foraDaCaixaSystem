package camadas.model.dao;

import camadas.model.domain.ItemMaterial;
import camadas.model.domain.Material;
import camadas.model.domain.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProdutoDao {
    public void create(Produto produto) {
        try {
            Database db = new Database();
            db.connection.setAutoCommit(false);

            String insertProduto = "insert into produtos (descricao, valor, cod) values (?, ?, ?);";
            String insertItemMaterial = "insert into itemMaterial (idMaterial, idProduto, quantidade, cod) values (?, ?, ?, ?)";
            String getIdMaterial = "select id from materiais where cod = ?";
            int idMaterial = 0;
            int idProduto = 0;

            try (PreparedStatement stm = db.connection.prepareStatement(insertProduto, Statement.RETURN_GENERATED_KEYS)) {
                stm.setString(1, produto.getDescricao());
                stm.setFloat(2, produto.getValor());
                stm.setString(3, produto.getCod());
                stm.executeUpdate();

                try (ResultSet result = stm.getGeneratedKeys()) {
                    if (result.next()) {
                        idProduto = result.getInt(1);
                    } else {
                        throw new SQLException("Falha ao procurar id de produto");
                    }
                }
            }

            try (PreparedStatement stm = db.connection.prepareStatement(getIdMaterial)) {
                for (ItemMaterial item : produto.getItensMateriais()) {
                    stm.setString(1, item.getMaterial().getCod());
                    try (ResultSet result = stm.executeQuery()) {
                        if (result.next()) {
                            idMaterial = result.getInt("id");
                        } else {
                            throw new SQLException("Falha ao procurar id de material-cod: " + item.getMaterial().getCod());
                        }
                    }
                    try (PreparedStatement stm2 = db.connection.prepareStatement(insertItemMaterial)) {
                        stm2.setInt(1, idMaterial);
                        stm2.setInt(2, idProduto);
                        stm2.setInt(3, item.getQuantidade());
                        stm2.setString(4, item.getCod());
                        stm2.executeUpdate();
                    }
                }
            }
            try {
                db.connection.commit();
            } catch (SQLException e) {
                db.connection.rollback();
                throw new RuntimeException("Erro ao criar produto - " + e);
            }
            db.connection.close();
        } catch (SQLException rollbackEx) {
            throw new RuntimeException("Erro ao restaurar banco de dados com rollback- ", rollbackEx);
        }
    }

    public void delete(String cod) {
        try {
            Database db = new Database();
            try {
                String getIdProduto = "select id from Produtos where cod = ?";
                int idProduto = 0;
                String deleteProduto = "delete from produtos where cod = ?";
                String deleteItemMaterial = "delete from ItemMaterial where idProduto = ?";

                try (PreparedStatement stm = db.connection.prepareStatement(getIdProduto)) {
                    stm.setString(1, cod);
                    try (ResultSet result = stm.executeQuery()) {
                        if (result.next()) {
                            idProduto = result.getInt("id");
                        } else {
                            throw new SQLException("Erro ao procurar id de produto");
                        }
                    }
                }

                try (PreparedStatement stm = db.connection.prepareStatement(deleteItemMaterial)) {
                    stm.setInt(1, idProduto);
                    stm.executeUpdate();
                } catch (SQLException e) {
                    throw new SQLException("Erro ao deletar Item-material");
                }

                try (PreparedStatement stm = db.connection.prepareStatement(deleteProduto)) {
                    stm.setString(1, cod);
                    stm.executeUpdate();
                } catch (SQLException e) {
                    throw new SQLException("Erro ao deletar Produto");
                }
            } catch (SQLException e) {
                try {
                    db.connection.rollback();
                    throw e;
                } catch (SQLException rollbackEx) {
                    throw new SQLException("Erro ao realizar rollback + " + e);
                }
            } finally {
                db.connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao Cadastrar produto - " + e);
        }
    }

    public void update(String codAntigo, Produto produtoNovo) {
        try {
            Database db = new Database();

            try {
                String updateProduto = "update produtos set descricao = ?, valor = ?, cod = ? where cod = ?";
                String getIdProduto = "select id from produtos where cod = ?";
                int idProduto = 0;
                String deleteItemMaterial = "delete from ItemMaterial where idProduto = ?";
                String insertItemMaterial = "insert into ItemMaterial (idMaterial, idProduto, quantidade, cod) values (?, ?, ?, ?)";
                String getIdMaterial = "select id from materiais where cod = ?";


                try (PreparedStatement stm = db.connection.prepareStatement(getIdProduto)) {
                    stm.setString(1, codAntigo);
                    try (ResultSet result = stm.executeQuery()) {
                        if (result.next()) {
                            idProduto = result.getInt("id");
                        } else {
                            throw new SQLException("SELECT não retornou valor válido");
                        }
                    }
                }

                try (PreparedStatement stm = db.connection.prepareStatement(deleteItemMaterial)) {
                    stm.setInt(1, idProduto);
                    stm.executeUpdate();
                } catch (SQLException e) {
                    throw new SQLException("Erro ao deletar Item-Material");
                }

                try {
                    for (ItemMaterial item : produtoNovo.getItensMateriais()) {
                        try (PreparedStatement insert = db.connection.prepareStatement(insertItemMaterial)) {
                            insert.setInt(2, idProduto);
                            try (PreparedStatement get = db.connection.prepareStatement(getIdMaterial)) {
                                get.setString(1, item.getMaterial().getCod());
                                try (ResultSet result = get.executeQuery()) {
                                    if (result.next()) {
                                        insert.setInt(1, result.getInt("id"));
                                    } else {
                                        throw new SQLException("Erro ao procurar id de material");
                                    }
                                    insert.setInt(3, item.getQuantidade());
                                    insert.setString(4, item.getCod());
                                    insert.executeUpdate();
                                } catch (SQLException e) {
                                    throw e;
                                }
                            } catch (SQLException e) {
                                throw new SQLException("Erro ao executar query: " + e);
                            }
                        }
                    }
                } catch (SQLException e) {
                    throw new SQLException("Erro ao inserir Item-Material: " + e);
                }

                try (PreparedStatement update = db.connection.prepareStatement(updateProduto)) {
                    update.setString(1, produtoNovo.getDescricao());
                    update.setFloat(2, produtoNovo.getValor());
                    update.setString(3, produtoNovo.getCod());
                    update.setString(4, codAntigo);
                    update.executeUpdate();
                } catch (SQLException e) {
                    throw new SQLException("Erro ao atualizar produto");
                }

            } catch (SQLException e) {
                try {
                    db.connection.rollback();
                    throw e;
                } catch (SQLException rollbackEx) {
                    throw new SQLException("Erro ao realizar rollback + " + e);
                }
            } finally {
                db.connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar Produto: " + e);
        }
    }

    public Produto get(String cod) {
        Produto produto = new Produto(cod);
        Set<ItemMaterial> itensMateriais = new HashSet<>();
        try {
            Database db = new Database();
            db.connection.setAutoCommit(false);

            String getProduto = "Select * from produtos where cod = ?";
            int idProduto = 0;
            String getSetItemMaterial = "Select * from ItemMaterial where idProduto = ?";
            String getMaterial = "Select * from materiais where id = ?";

            try {
                try (PreparedStatement stm = db.connection.prepareStatement(getProduto)) {
                    stm.setString(1, cod);
                    try (ResultSet result = stm.executeQuery()) {
                        produto.setDescricao(result.getString("descricao"));
                        produto.setValor(result.getFloat("valor"));
                        idProduto = result.getInt("id");
                        produto.setMargemLucro(result.getFloat("MargemLucro"));
                    }
                } catch (SQLException e){
                    throw new SQLException("Erro ao procurar Produto");
                }

                try (PreparedStatement getIM = db.connection.prepareStatement(getSetItemMaterial)) {
                    getIM.setInt(1, idProduto);
                    try (ResultSet resultIM = getIM.executeQuery()) {
                        while (resultIM.next()) {
                            try (PreparedStatement getM = db.connection.prepareStatement(getMaterial)) {
                                getM.setInt(1, resultIM.getInt("idMaterial"));
                                try (ResultSet resultM = getM.executeQuery()) {
                                    itensMateriais.add(new ItemMaterial(
                                            new Material(
                                                    resultM.getString("descricao"),
                                                    resultM.getFloat("valor"),
                                                    resultM.getString("cod"),
                                                    resultM.getInt("quantidadeEstoque")
                                            ),
                                            resultIM.getInt("quantidade"),
                                            resultIM.getString("cod")
                                    ));
                                }
                            }
                        }
                    }
                } catch (SQLException e){
                    throw new SQLException("Erro ao procurar ItemMaterial");
                }

                produto.setItensMateriais(itensMateriais);
            }catch (SQLException e) {
                try {
                    db.connection.rollback();
                    throw e;
                } catch (SQLException rollbackEx) {
                    throw new SQLException("Erro ao realizar rollback + " + e);
                }
            } finally {
                db.connection.commit();
                db.connection.close();
            }
        } catch (SQLException e){
            throw new RuntimeException("Erro ao procurar produto: " + e);
        }
        return produto;
    }

    public List<Produto> getList (){
        List<Produto> produtos = new ArrayList<>();
        try{
            Database db = new Database();
            db.connection.setAutoCommit(false);

            String getAllProdutos = "select * from produtos";

            String getAllItemMateriais = """
                select
                    itemMaterial.id as idItemMaterial,
                    idMaterial,
                    descricao,
                    valor,
                    quantidadeEstoque,
                    materiais.cod as codMateriais,
                    idProduto,
                    quantidade,
                    ItemMaterial.cod as codItemMaterial
                from itemMaterial join materiais on itemMaterial.idmaterial = materiais.id
                where idProduto = ?;
                """;

            Integer idProduto = 0;

            try{
                try(ResultSet resultP = db.connection.createStatement().executeQuery(getAllProdutos)){
                    while (resultP.next()){
                        try(PreparedStatement getIM = db.connection.prepareStatement(getAllItemMateriais)){
                            getIM.setInt(1, resultP.getInt("id"));
                            try(ResultSet resultIM = getIM.executeQuery()){
                                Set<ItemMaterial> itemMaterialSet = new HashSet<>();
                                while (resultIM.next()){
                                    itemMaterialSet.add(new ItemMaterial(
                                            new Material(
                                                    resultIM.getString("descricao"), // Após alterar o select acima, também altere esses getters
                                                    resultIM.getFloat("valor"),
                                                    resultIM.getString("codMateriais"),
                                                    resultIM.getInt("quantidadeEstoque")
                                            ),
                                            resultIM.getInt("quantidade"),
                                            resultIM.getString("codItemMaterial")
                                    ));
                                }
                                produtos.add(new Produto(
                                        resultP.getString("descricao"),
                                        resultP.getFloat("valor"),
                                        itemMaterialSet,
                                        resultP.getString("cod"),
                                        resultP.getFloat("margemLucro")
                                ));
                            }
                        }
                    }
                }
                db.connection.commit();
            }catch (SQLException e){
                try{
                    db.connection.rollback();
                } catch (SQLException rollback){
                    throw new SQLException("Erro ao dar rollback", rollback);
                }
            } finally {
                db.connection.close();
            }
        } catch (SQLException e){
            throw new RuntimeException("Erro ao procurar lista de produtos", e);
        }
        return produtos;
    }

    public List<Produto> getSearch(String descricao){
        String getProdutoByDescricao = "select * from produto where descricao like ?";
        int idProduto = 0;
        String getItensMateriais = """
                select
                    itemMaterial.id as idItemMaterial,
                    idMaterial,
                    descricao,
                    valor,
                    quantidadeEstoque,
                    materiais.cod as codMateriais,
                    idProduto,
                    quantidade,
                    ItemMaterial.cod as codItemMaterial
                from itemMaterial join materiais on itemMaterial.idmaterial = materiais.id
                where idProduto = ?;
                """;
        List<Produto> produtos = new ArrayList<>();
        try{
            Database db = new Database();
            db.connection.setAutoCommit(false);
            try{
                try(PreparedStatement getP = db.connection.prepareStatement(getProdutoByDescricao)){
                    getP.setString(1, "%" + descricao + "%");
                    try(ResultSet resultP = getP.executeQuery()){
                        while(resultP.next()){
                            idProduto = resultP.getInt("id");
                            try(PreparedStatement getIM = db.connection.prepareStatement(getItensMateriais)){
                                getIM.setInt(1, idProduto);
                                Set<ItemMaterial> itemMaterialSet = new HashSet<>();
                                try(ResultSet resultIM = getIM.executeQuery()){
                                    while (resultIM.next()){
                                        itemMaterialSet.add(
                                                new ItemMaterial(
                                                        new Material(
                                                                resultIM.getString("descricao"),
                                                                resultIM.getFloat("valor"),
                                                                resultIM.getString("codMateriais"),
                                                                resultIM.getInt("quantidadeEstoque")
                                                        ),
                                                        resultIM.getInt("quantidade"),
                                                        resultIM.getString("codItemMaterial")
                                                )
                                        );
                                    }
                                    produtos.add(
                                            new Produto(
                                                    resultP.getString("descricao"),
                                                    resultP.getFloat("valor"),
                                                    itemMaterialSet,
                                                    resultP.getString("cod"),
                                                    resultP.getFloat("margemLucro")
                                            )
                                    );
                                }
                            }
                        }
                    }
                }
                db.connection.commit();
            }catch (SQLException e){
                try{
                    db.connection.rollback();
                }catch (SQLException rollback){
                    throw new SQLException("Erro ao dar rollback", rollback);
                }
            }finally {
                db.connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao procurar produto por descrição", e);
        }
        return produtos;
    }
}

