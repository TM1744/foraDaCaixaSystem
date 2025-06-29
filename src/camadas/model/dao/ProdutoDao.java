package camadas.model.dao;

import camadas.model.domain.ItemMaterial;
import camadas.model.domain.Produto;

import java.sql.*;
import java.util.ArrayList;

public class ProdutoDao {
    public void create2(Produto produto) {
        try {
            Database db = new Database();
            db.connection.setAutoCommit(false); // Início da transação

            // Inserir o produto
            String insertProdutoSQL = "INSERT INTO produtos (descricao, valor, cod) VALUES (?, ?, ?)";
            int idProduto;
            try (PreparedStatement insertProduto = db.connection.prepareStatement(insertProdutoSQL)) {
                insertProduto.setString(1, produto.getDescricao());
                insertProduto.setFloat(2, produto.getValor());
                insertProduto.setString(3, produto.getCod());
                insertProduto.executeUpdate();
            }

            // Recuperar o ID do produto inserido (SQLite)
            try (Statement stmt = db.connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    idProduto = rs.getInt(1);
                } else {
                    throw new SQLException("Erro ao obter ID do produto após o insert.");
                }
            }

            // Preparar inserção de itens materiais
            String getMaterialIdSQL = "SELECT id FROM materiais WHERE cod = ?";
            String insertItemSQL = "INSERT INTO itemMaterial (idMaterial, idProduto, quantidade, cod) VALUES (?, ?, ?, ?)";

            try (PreparedStatement getMaterialId = db.connection.prepareStatement(getMaterialIdSQL);
                 PreparedStatement insertItemMaterial = db.connection.prepareStatement(insertItemSQL)) {

                for (ItemMaterial item : produto.getItensMateriais()) {
                    // Obter ID do material
                    getMaterialId.setString(1, item.getCod());
                    try (ResultSet rs = getMaterialId.executeQuery()) {
                        if (rs.next()) {
                            int idMaterial = rs.getInt("id");

                            // Inserir itemMaterial
                            insertItemMaterial.setInt(1, idMaterial);
                            insertItemMaterial.setInt(2, idProduto);
                            insertItemMaterial.setInt(3, item.getQuantidade());
                            insertItemMaterial.setString(4, item.getCod());
                            insertItemMaterial.executeUpdate();
                        } else {
                            throw new SQLException("Material com código '" + item.getCod() + "' não encontrado.");
                        }
                    }
                }
            }

            db.connection.commit(); // Commit da transação
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar produto no SQLite", e);
        }
    }

    public void create(Produto produto){
        try{
            Database db = new Database();
            db.connection.setAutoCommit(false);

            String insertProduto = "insert into produtos (descricao, valor, cod) values (?, ?, ?);";




        }catch (SQLException e){
            throw new RuntimeException("Erro ao criar produto - ", e);
        }
    }
}
