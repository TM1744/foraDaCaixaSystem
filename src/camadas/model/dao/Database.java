package camadas.model.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database{
    public Connection connection;
    private final String dns = "jdbc:sqlite:bancoDeDados/banco/base.db";

    public Database(){
        try {
            this.connection = DriverManager.getConnection(dns);
            Statement stm = connection.createStatement();
            stm.executeUpdate("""
            CREATE TABLE IF NOT EXISTS Clientes (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nome TEXT NOT NULL,
            telefone TEXT NOT NULL UNIQUE,
            endereco TEXT NOT NULL,
            cod INTEGER NOT NULL UNIQUE
            );
            """);

            stm.executeUpdate("""
            CREATE TABLE IF NOT EXISTS Materiais (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            descricao TEXT NOT NULL,
            valor REAL NOT NULL CHECK (valor > 0),
            quantidadeEstoque INTEGER NOT NULL CHECK (quantidadeEstoque >= 0),
            cod INTEGER NOT NULL UNIQUE
            );
            """);

            stm.executeUpdate("""
            CREATE TABLE IF NOT EXISTS ItemMaterial (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            idMaterial INTEGER NOT NULL,
            idProduto Integer not null,
            quantidade INTEGER CHECK(quantidade > 0),
            FOREIGN KEY (idMaterial) REFERENCES Materiais(id),
            foreign key (idProduto) references Produtos(id)
            );
            """);

            stm.executeUpdate("""
            CREATE TABLE IF NOT EXISTS Produtos (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            descricao TEXT NOT NULL,
            valor REAL NOT NULL CHECK(valor > 0),
            margemLucro real not null default 0,
            cod INTEGER NOT NULL UNIQUE
            );
            """);

            stm.executeUpdate("""
            CREATE TABLE IF NOT EXISTS Vendas (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            idCliente INTEGER NOT NULL,
            valorTotal REAL NOT NULL CHECK (valorTotal > 0),
            dataEntrega DATE,
            cod INTEGER NOT NULL UNIQUE,
            FOREIGN KEY (idCliente) REFERENCES Clientes(id)
            );
            """);

            stm.executeUpdate("""
            CREATE TABLE IF NOT EXISTS ItemProduto (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            idProduto INTEGER NOT NULL,
            idVenda integer not null,
            cod integer not null unique,
            quantidade INTEGER NOT NULL CHECK(quantidade > 0),
            FOREIGN KEY (idProduto) REFERENCES Produtos(id),
            foreign key (idVenda) references Vendas (id)
            );
            """);

            stm.executeUpdate("""
                    create table if not exists MargemDeLucro (
                    id integer primary key autoincrement,
                    porcentagem real not null default 30
                    );
                    """);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }



}
