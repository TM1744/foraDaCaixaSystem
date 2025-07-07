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
    public Float getMargemLucro(){
        String getValorMargem = "select porcentagem from MargemDeLucro where id = 1";
        float valor = 0f;
        try{
            Database db = new Database();
            try(ResultSet result = db.connection.createStatement().executeQuery(getValorMargem)){
                valor = result.getFloat("porcentagem");
            }
        }catch (SQLException e){
            throw new RuntimeException("Erro ao obter margem de lucro para o produto", e);
        }
        return valor;
    }

    public void create(Produto produto){
        String insertProduto = "insert into produtos (descricao, valor, cod, margemLucro) values (?, ?, ?, ?);";
        String insertItemMaterial = "insert into ItemMaterial (idMaterial, idProduto, quantidade) values (?, ?, ?)";
        String getIdMaterial = "select id from materiais where cod = ?";

        try {
            Database db = new Database();
            db.connection.setAutoCommit(false);
            try {
                int idProduto;
                try (PreparedStatement insertP = db.connection.prepareStatement(insertProduto, Statement.RETURN_GENERATED_KEYS)) {
                    insertP.setString(1, produto.getDescricao());
                    insertP.setFloat(2, produto.getValor());
                    insertP.setString(3, produto.getCod());
                    insertP.setFloat(4, produto.getMargemLucro());
                    insertP.executeUpdate();

                    try (ResultSet resultKey = insertP.getGeneratedKeys()) {
                        if (resultKey.next()) {
                            idProduto = resultKey.getInt(1);
                        } else {
                            throw new SQLException("Nenhuma chave gerada para o produto");
                        }
                    }
                }

                for (ItemMaterial item : produto.getItensMateriais()) {
                    int idMaterial;
                    try (PreparedStatement getM = db.connection.prepareStatement(getIdMaterial)) {
                        getM.setString(1, item.getMaterial().getCod());
                        try (ResultSet resultM = getM.executeQuery()) {
                            if (resultM.next()) {
                                idMaterial = resultM.getInt(1);
                            } else {
                                throw new SQLException("Material com código " + item.getMaterial().getCod() + " não encontrado");
                            }
                        }
                    }

                    try (PreparedStatement insertIM = db.connection.prepareStatement(insertItemMaterial)) {
                        insertIM.setInt(1, idMaterial);
                        insertIM.setInt(2, idProduto);
                        insertIM.setInt(3, item.getQuantidade());
                        insertIM.executeUpdate();
                    }
                }

                db.connection.commit();
            } catch (SQLException e) {
                try {
                    db.connection.rollback();
                } catch (SQLException rollback) {
                    e.addSuppressed(rollback);
                }
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar produto", e);
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
                String insertItemMaterial = "insert into ItemMaterial (idMaterial, idProduto, quantidade) values (?, ?, ?)";
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
        List<ItemMaterial> itensMateriais = new ArrayList<>();
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
                                            resultIM.getInt("quantidade")
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
                    quantidade
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
                                List<ItemMaterial> itemMaterialSet = new ArrayList<>();
                                while (resultIM.next()){
                                    itemMaterialSet.add(new ItemMaterial(
                                            new Material(
                                                    resultIM.getString("descricao"), // Após alterar o select acima, também altere esses getters
                                                    resultIM.getFloat("valor"),
                                                    resultIM.getString("codMateriais"),
                                                    resultIM.getInt("quantidadeEstoque")
                                            ),
                                            resultIM.getInt("quantidade")
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
        String getProdutoByDescricao = "select * from produtos where descricao like ?";
        String getItensMateriais = """
        select
            itemMaterial.id as idItemMaterial,
            idMaterial,
            descricao,
            valor,
            quantidadeEstoque,
            materiais.cod as codMateriais,
            idProduto,
            quantidade
        from itemMaterial
        join materiais on itemMaterial.idmaterial = materiais.id
        where idProduto = ?;
    """;

        List<Produto> produtos = new ArrayList<>();

        try{
            Database db = new Database();
            try (PreparedStatement getP = db.connection.prepareStatement(getProdutoByDescricao)) {
                getP.setString(1, "%" + descricao + "%");

                try (ResultSet resultP = getP.executeQuery()) {
                    while (resultP.next()) {
                        int idProduto = resultP.getInt("id");

                        List<ItemMaterial> itemMaterialSet = new ArrayList<>();
                        try (PreparedStatement getIM = db.connection.prepareStatement(getItensMateriais)) {
                            getIM.setInt(1, idProduto);

                            try (ResultSet resultIM = getIM.executeQuery()) {
                                while (resultIM.next()) {
                                    Material material = new Material(
                                            resultIM.getString("descricao"),
                                            resultIM.getFloat("valor"),
                                            resultIM.getString("codMateriais"),
                                            resultIM.getInt("quantidadeEstoque")
                                    );
                                    int quantidade = resultIM.getInt("quantidade");

                                    itemMaterialSet.add(new ItemMaterial(material, quantidade));
                                }
                            }
                        }

                        Produto produto = new Produto(
                                resultP.getString("descricao"),
                                resultP.getFloat("valor"),
                                itemMaterialSet,
                                resultP.getString("cod"),
                                resultP.getFloat("margemLucro")
                        );

                        produtos.add(produto);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao procurar produto por descrição", e);
        }

        return produtos;
    }
}

