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
            stm.executeUpdate("""
                    create table if not exists Clientes (
                    	id serial primary key,
                    	nome varchar(250) not null,
                    	telefone varchar(11) not null unique,
                    	endereco varchar(250) not null
                    );
                    """);
            stm.executeUpdate("""
                    create table if not exists Materiais (
                    	id serial primary key,
                    	descricao varchar(250) not null,
                    	valor money not null check (valor > 0::money)
                    );
                    """);
            stm.executeUpdate("""
                    create table if not exists ItemMaterial (
                    	id serial primary key,
                    	idMaterial int not null,
                    	quantidade int check(quantidade > 0),
                    	foreign key (idMaterial) references Materiais(id)
                    );
                    """);
            stm.executeUpdate("""
                    create table if not exists Produtos (
                    	id serial primary key,
                    	descricao varchar(250) not null,
                    	valor money check(valor > 0::money) not null
                    );
                    """);
            stm.executeUpdate("""
                    create table if not exists Produto_ItemMaterial (
                    	id serial primary key,
                    	idProduto int not null,
                    	idItemMaterial int not null,
                    	foreign key (idProduto) references Produtos (id),
                    	foreign key (idItemMaterial) references ItemMaterial (id)
                    );
                    """);
            stm.executeUpdate("""
                    create table if not exists Vendas (
                    	id serial primary key,
                    	idCliente int not null,
                    	valorTotal money not null check (valorTotal > 0::money),
                    	dataEntrega date,
                    	foreign key (idCliente) references Clientes (id)
                    );
                    """);
            stm.executeUpdate("""
                    create table if not exists ItemProduto (
                    	id serial primary key,
                    	idProduto int not null,
                    	quantidade int not null check(quantidade > 0),
                    	foreign key (idProduto) references Produtos(id)
                    );
                    """);
            stm.executeUpdate("""
                    create table if not exists Venda_ItemProduto (
                    	id serial primary key,
                    	idVenda int not null,
                    	idItemProduto int not null,
                    	foreign key (idVenda) references Vendas(id),
                    	foreign key (idItemProduto) references ItemProduto(id)
                    );
                    """);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }



}
