package jdbc.Conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectionFactory {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://aws-1-us-east-2.pooler.supabase.com:6543/postgres";
        String user = "postgres.kndxwsntnhspxtwuwtdp";
        String password = "L6Ip2meYf4ylDJ6f";
        return DriverManager.getConnection(url,user,password);
    }
}
