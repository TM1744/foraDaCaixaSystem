package camadas.model.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private final String DNS = "jdbc:postgresql://localhost:5432/postgres?ssl=false";
    public Connection connection;

    public Database() throws SQLException{
        connection = DriverManager.getConnection(DNS, "postgres","postgres");
    }

    public final void close() throws SQLException{
        connection.close();
    }
}
