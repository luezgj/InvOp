package tpinvop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class AdminBD {
    private final String url = "jdbc:postgresql://localhost/dvdrental";
    private final String user = "invop";
    private final String password = "1234";
    
    public Connection connectDatabase() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
 
        return conn;
    } 
}