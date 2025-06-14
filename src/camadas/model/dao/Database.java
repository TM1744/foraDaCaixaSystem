package camadas.model.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {

    public Database(){
        try {
            Connection connection;
            connection = DriverManager.getConnection("jdbc:sqlite:bancoDeDados/base.db");
            Statement stm = connection.createStatement();
            stm.executeUpdate("create table if not exists " +
                    "Cliente (" +
                    "id serial primary key," +
                    "nome varchar(250)," +
                    "telefone varchar(250)," +
                    "endereco varchar(250)" +
                    ");");
            stm.executeUpdate("create table if not exists " +
                    "Material (" +
                    "id serial primary key," +
                    "descricao varchar (250)," +
                    "valor money" +
                    ");");
            stm.executeUpdate("create table if not exists " +
                    "ItemMaterial (" +
                    "id serial primary key," +
                    "idMaterial int foreign key references Material(id)," +
                    "quantidade int" +
                    ");");
            stm.executeUpdate("create table if not exists " +
                    "Produto (" +
                    "id serial primary key," +
                    "descricao varchar(250)," +
                    "valor money" +
                    ");");
            stm.executeUpdate("""
                    create table if not exists ItemMaterialRelacional (
                    	id serial PRIMARY key,
                      	idItemMaterial int foreign key REFERENCES ItemMaterial(id),
                      	idProduto int foreign key REFERENCES Produto(id)
                    );
                    """);
            stm.executeUpdate("""
                    create table if not exists ItemProduto (
                    	id serial PRIMARY key,
                      	idProduto int FOREIGN key REFERENCES Produto(id),
                      	quantidade int
                    );
                    """);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }



}
