package camadas.model.dao;

import camadas.model.domain.Produto;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProdutoDao {
    public void create(Produto produto){
        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement("""
                    insert into produtos (descricao, valor, cod) values (?, ?, ?);
                    """);
            stm.setString(1, produto.getDescricao());
            stm.setFloat(2, produto.getValor());
            stm.setString(3, produto.getCod());
            stm.executeUpdate();
            stm = db.connection.prepareStatement("""
                    insert into Produto_ItemMaterial (idProduto, idItemMaterial) values ((select id from produtos where cod = ?), (select id from ItemMaterial where cod = ?));
                    """);
            stm.setString(1, produto.getCod());
            stm.setString(2, pr);
            stm.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}
