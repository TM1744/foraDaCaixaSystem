package camadas.model.dao;

import camadas.model.domain.ItemMaterial;
import camadas.model.domain.Material;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemMaterialDao {
    public void create(ItemMaterial itemMaterial){
        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement("""
                    insert into itemmaterial (idmaterial, quantidade, cod) values ((select id from materiais where cod = ?), ?, ?);
                    """);
            stm.setString(1, itemMaterial.getMaterial().getCod());
            stm.setInt(2, itemMaterial.getQuantidade());
            stm.setString(3, itemMaterial.getCod());

            stm.executeUpdate();

            stm.close();
            db.connection.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void delete(String cod){
        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement("delete from itemMaterial where cod = ?");
            stm.setString(1, cod);
            stm.executeUpdate();
            stm.close();
            db.connection.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public ItemMaterial get(String cod){
        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement("""
                    select itemMaterial.quantidade, itemMaterial.cod,
                           materiais.descricao, materiais.valor, materiais.quantidadeEstoque, materiais.cod,
                    from Itemmaterial join materiais on itemmaterial.idmaterial = materiais.id
                    where itemmaterial.cod = ?;
                    """);
            stm.setString(1, cod);
            ResultSet result = stm.executeQuery();
            Material material = new Material(
                    result.getString("materiais.descricao"),
                    result.getFloat("materiais.valor"),
                    result.getString("materiais.cod"),
                    result.getInt("materiais.quantidadeEstoque")
            );
            ItemMaterial itemMaterial = new ItemMaterial(
                    material,
                    result.getInt("ItemMaterial.quantidade"),
                    result.getString("ItemMaterial.cod")
            );

            result.close();
            stm.close();
            db.connection.close();
            return itemMaterial;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<ItemMaterial> getList(){
        try{
            Database db = new Database();
            ResultSet result = db.connection.createStatement().executeQuery("""
                    select itemMaterial.quantidade, itemMaterial.cod,
                           materiais.descricao, materiais.valor, materiais.quantidadeEstoque, materiais.cod,
                    from Itemmaterial join materiais on itemmaterial.idmaterial = materiais.id;
                    """);
            List<ItemMaterial> itemMaterialList = new ArrayList<>();

            while(result.next()){
                Material material = new Material(
                        result.getString("materiais.descricao"),
                        result.getFloat("materiais.valor"),
                        result.getString("materiais.cod"),
                        result.getInt("materiais.quantidadeEstoque")
                );
                ItemMaterial itemMaterial = new ItemMaterial(
                        material,
                        result.getInt("ItemMaterial.quantidade"),
                        result.getString("ItemMaterial.cod")
                );
                itemMaterialList.add(itemMaterial);
            }
            result.close();
            db.connection.close();
            return itemMaterialList;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}
