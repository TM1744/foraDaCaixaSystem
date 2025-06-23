package camadas.model.dao;

import camadas.model.domain.ItemMaterial;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemMaterialDao {
    public void create (ItemMaterial itemMaterial){
        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement("insert into itemMaterial (IdMaterial, quantidade) values (?, ?);");
            PreparedStatement stm2 = db.connection.prepareStatement("select id from materiais where cod = ?;");
            stm2.setString(1, itemMaterial.getMaterial().getCod());
            ResultSet result = stm2.executeQuery();
            stm.setInt(1, result.getInt("id"));
            stm.setInt(2, itemMaterial.getQuantidade());
            stm2.close();
            stm.close();
            result.close();
            db.connection.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void delete(Integer id){
        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement("delete from itemMaterial where id = ?");
            stm.setInt(1, id);
            stm.executeUpdate();
            stm.close();
            db.connection.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public ItemMaterial get(String codMaterial, ){
        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement("select *")
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
