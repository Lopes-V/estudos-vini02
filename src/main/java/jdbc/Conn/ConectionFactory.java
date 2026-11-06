package jdbc.Conn;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectionFactory {
    // Credenciais vêm de variáveis de ambiente: DB_URL, DB_USER e DB_PASSWORD.
    private static String env(String nome) {
        String valor = System.getenv(nome);
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException("Variável de ambiente ausente: " + nome);
        }
        return valor;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(env("DB_URL"), env("DB_USER"), env("DB_PASSWORD"));
    }

    public static JdbcRowSet getJdbcRowSet() throws SQLException {
        JdbcRowSet jdbcRowSet = RowSetProvider.newFactory().createJdbcRowSet();
        jdbcRowSet.setUrl(env("DB_URL"));
        jdbcRowSet.setUsername(env("DB_USER"));
        jdbcRowSet.setPassword(env("DB_PASSWORD"));
        return jdbcRowSet;
    }

    public static CachedRowSet getCachedRowSet() throws SQLException {
        return RowSetProvider.newFactory().createCachedRowSet();
    }
}
