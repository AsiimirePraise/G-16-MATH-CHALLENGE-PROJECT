package server;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {
    // initialize all these attributes
    String url;
    String username;
    String password;
    Connection connection;
    Statement statement = connection.createStatement();

    public DbConnection() throws SQLException {
        // initialize a connection here and save the connection
    }

    public void create(String sqlCommand) throws SQLException {
        this.statement.execute(sqlCommand);
    }

    public void read(String sqlCommand) throws SQLException {
        this.statement.execute(sqlCommand);
    }

    public void update(String sqlCommand) throws SQLException {
        this.statement.execute(sqlCommand);
    }

    public void delete(String sqlCommand) throws SQLException {
        this.statement.execute(sqlCommand);
    }
}