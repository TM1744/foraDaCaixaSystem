package camadas.model.dao;

import camadas.model.domain.ItemMaterial;
import camadas.model.domain.Produto;

import java.sql.*;
import java.util.ArrayList;

public class ProdutoDao {
    public void create(Produto produto){
        try{
            Database db = new Database();
            db.connection.setAutoCommit(false);

            String insertProduto = "insert into produtos (descricao, valor, cod) values (?, ?, ?);";
            String getIdProduto = "select id from produtos where cod = ?";
            String insertItemMaterial = "insert into itemMaterial (idMaterial, idProduto, quantidade, cod) values (?, ?, ?, ?)";
            String getIdMaterial = "select id from materiais where cod = ?";
            int idMaterial = 0;
            int idProduto = 0;

            try(PreparedStatement stm = db.connection.prepareStatement(insertProduto)){
                stm.setString(1, produto.getDescricao());
                stm.setFloat(2, produto.getValor());
                stm.setString(3, produto.getCod());
                stm.executeUpdate();
            }

            try(PreparedStatement stm = db.connection.prepareStatement(getIdProduto)){
                stm.setString(1, produto.getCod());
                ResultSet result = stm.executeQuery();
                if(result.next()){
                    idProduto = result.getInt("id");
                }else{
                    throw new SQLException("Falha ao procurar id de produto");
                }
            }

            try(PreparedStatement stm = db.connection.prepareStatement(getIdMaterial)){
                for(ItemMaterial item : produto.getItensMateriais()){
                    stm.setString(1, item.getMaterial().getCod());
                    ResultSet result = stm.executeQuery();
                    if(result.next()){
                        idMaterial = result.getInt("id");
                    }else{
                        throw new SQLException("Falha ao procurar id de material-cod: " + item.getMaterial().getCod());
                    }
                    try(PreparedStatement stm2 = db.connection.prepareStatement(insertItemMaterial)){
                        stm2.setInt(1, idMaterial);
                        stm2.setInt(2, idProduto);
                        stm2.setInt(3, item.getQuantidade());
                        stm2.setString(4, item.getCod());
                        stm2.executeUpdate();
                    }
                }
            }

            db.connection.commit();
        }catch (SQLException e){
            throw new RuntimeException("Erro ao criar produto - ", e);
        }
    }
}
