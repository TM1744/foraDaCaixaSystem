package camadas.model.dao;

import camadas.model.domain.Cliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public Cliente get(String cod) throws RuntimeException {
        try {
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement("SELECT * FROM clientes WHERE cod = ?");
            stm.setString(1, cod);
            ResultSet result = stm.executeQuery();

            Cliente cliente = null;
            if (result.next()) {
                cliente = new Cliente(
                        result.getString("nome"),
                        result.getString("telefone"),
                        result.getString("endereco"),
                        result.getString("cod")
                );
            }

            result.close();
            stm.close();
            db.connection.close();

            return cliente; // pode retornar null se não encontrar
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Cliente> getList() throws RuntimeException{
        try{
            Database db = new Database();
            ResultSet result = db.connection.createStatement().executeQuery("select * from clientes;");
            List<Cliente> clientes = new ArrayList<>();
            while (result.next()){
                clientes.add(new Cliente(result.getString("nome"), result.getString("telefone"), result.getString("endereco"), result.getString("cod")));
            }
            result.close();
            db.connection.close();
            return clientes;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

    }
    public void update(Cliente cliente1, String codigo) throws RuntimeException{
        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement("update clientes set nome = ?, telefone = ?, endereco = ?, cod = ? where cod = ?");
            stm.setString(1, cliente1.getNome());
            stm.setString(2, cliente1.getTelefone());
            stm.setString(3, cliente1.getEndereco());
            stm.setString(4, cliente1.getCod());
            stm.setString(5, codigo);
            stm.executeUpdate();
            stm.close();
            db.connection.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<Cliente> getSearch(String nome) throws RuntimeException{
        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement("select * from clientes where nome like ?;");
            stm.setString(1, "%" + nome.toUpperCase() + "%");
            ResultSet result = stm.executeQuery();
            List<Cliente> clientes = new ArrayList<>();
            while (result.next()){
                clientes.add(new Cliente(result.getString("nome"), result.getString("telefone"), result.getString("endereco"), result.getString("cod")));
            }
            stm.close();
            result.close();
            db.connection.close();
            return clientes;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }
}
