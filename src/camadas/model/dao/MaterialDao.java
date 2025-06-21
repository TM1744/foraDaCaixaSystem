package camadas.model.dao;

import camadas.model.domain.Material;

import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialDao {
    public void create (Material material){
        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement("insert into materiais (descricao, valor, cod) values (?, ?, ?);");
            stm.setString(1, material.getDescricao());
            stm.setFloat(2, material.getValor());
            stm.setString(3, material.getCod());
            stm.executeUpdate();
            stm.close();
            db.connection.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void delete (String cod){
        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement("delete from materiais where cod = ?;");
            stm.setString(1, cod);
            stm.executeUpdate();
            stm.close();
            db.connection.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Material get(String cod){
        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement("select * from materiais where cod = ?;");
            stm.setString(1, cod);
            ResultSet result = stm.executeQuery();
            Material material = new Material(result.getString("descricao"), result.getFloat("valor"), result.getString("cod"));
            stm.close();
            result.close();
            db.connection.close();
            return material;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<Material> getList(){
        try{
            Database db = new Database();
            ResultSet result = db.connection.createStatement().executeQuery("select * from materiais;");
            List<Material> materiais = new ArrayList<>();
            while (result.next()){
                Material material = new Material(result.getString("descricao"), result.getFloat("valor"), result.getString("cod"));
                materiais.add(material);
            }
            result.close();
            db.connection.close();
            return materiais;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void update (String codAntigo, Material materialAtualizado){
        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement("update materiais set descricao = ?, set valor = ?, set cod = ? where cod = ?");
            stm.setString(1, materialAtualizado.getDescricao());
            stm.setFloat(2, materialAtualizado.getValor());
            stm.setString(3, materialAtualizado.getCod());
            stm.setString(4, codAntigo);
            stm.executeUpdate();
            stm.close();
            db.connection.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<Material> search(String descricao){}
}
