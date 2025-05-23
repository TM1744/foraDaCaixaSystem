package camadas.model.dao;

import camadas.model.domain.Material;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {
    public void create(Material material) throws SQLException {
        Database db = new Database();
        PreparedStatement stm = db.connection.prepareStatement("insert into materiais (descricao, valor) values (?, ?)");
        stm.setString(1, material.getDescricao());
        stm.setFloat(2, material.getValor());
        stm.executeUpdate();
        stm.close();
        db.close();
    }

    public List<Material> get() throws SQLException {
        Database db = new Database();
        List<Material> materiais = new ArrayList<>();
        ResultSet result = db.connection.createStatement().executeQuery("select descricao, valor from materiais");
        while (result.next()){
            Material material = new Material(
                    result.getString("descricao"),
                    result.getFloat("valor")
            );
            materiais.add(material);
        }
        result.close();
        db.close();
        return materiais;
    }
    public Material get(Integer index) throws SQLException{
        Database db = new Database();
        PreparedStatement stm = db.connection.prepareStatement("select descricao, valor from materiais where id = ?");
        stm.setInt(1, index);
        ResultSet result = stm.executeQuery();
        Material material = new Material(
                result.getString("descricao"),
                result.getFloat("valor")
        );
        stm.close();
        result.close();
        db.close();
        return material;
    }

    public void update(String nome, String descricaoAlterado, Float valorAlterado) throws SQLException {
        Database db = new Database();
        PreparedStatement stm = db.connection.prepareStatement("update materiais set descricao = ?, valor = ? where descricao = ?");
        stm.setString(1, descricaoAlterado);
        stm.setFloat(2, valorAlterado);
        stm.setString(3, nome);
        stm.executeUpdate();
        stm.close();
        db.close();
    }
}
