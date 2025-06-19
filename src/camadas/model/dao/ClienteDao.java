package camadas.model.dao;

import camadas.model.domain.Cliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ClienteDao {
    public void create (Cliente cliente) throws RuntimeException{
        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement("insert into clientes (nome, telefone, endereco, cod) values (?, ?, ?, ?);");
            stm.setString(1, cliente.getNome());
            stm.setString(2, cliente.getTelefone());
            stm.setString(3, cliente.getEndereco());
            stm.setString(4, cliente.getCod());
            stm.executeUpdate();
            stm.close();
            db.connection.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void delete (String cod) throws RuntimeException{
        try {
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement("delete from clientes where cod = ?");
            stm.setString(1, cod);
            stm.executeUpdate();
            stm.close();
            db.connection.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public Cliente get (String cod){
        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement("select * from clientes where cod = ?");
            stm.setString(1, cod);
            ResultSet result = stm.executeQuery();
            return new Cliente(result.getString("nome"), result.getString("telefone"), result.getString("endereco"), result.getString("cod"));
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void getList(){

    }
    public void update(Cliente cliente){

    }

    public List<Cliente> getSearch(String nome){

    }
}
