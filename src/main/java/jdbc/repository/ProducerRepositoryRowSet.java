package jdbc.repository;

import jdbc.Conn.ConectionFactory;
import jdbc.Dominio.Producer;
import jdbc.listener.CustomRowSetListener;
import org.postgresql.core.ConnectionFactory;

import javax.sql.RowSetListener;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.JdbcRowSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProducerRepositoryRowSet {
    public static List<Producer> findByNameJdbcRowSet(String name) {
        String sql = "SELECT * FROM producer WHERE name LIKE ?";
        List<Producer> producers = new ArrayList<>();
        try (JdbcRowSet jrs = ConectionFactory.getJdbcRowSet()) {
            jrs.addRowSetListener(new CustomRowSetListener());
            jrs.setCommand(sql);
            jrs.setString(1, String.format("%%%s%%", name));
            jrs.execute();
            while (jrs.next()) {
                Producer producer = Producer.builder()
                        .name(jrs.getString("name"))
                        .id(jrs.getInt("id_producer"))
                        .build();
                producers.add(producer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return producers;
    }

    public static void updateJdbcRowSet(Producer producer) {
        String sql = "SELECT * FROM producer WHERE id_producer = ?;";
        try (JdbcRowSet jrs = ConectionFactory.getJdbcRowSet()) {
            jrs.addRowSetListener(new CustomRowSetListener());
            jrs.setCommand(sql);
            jrs.setInt(1, producer.getId());
            jrs.execute();
            if(!jrs.next()) return;
            jrs.updateString(1, producer.getName());
            jrs.updateRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCachedRowSet(Producer producer) {
        String sql = "SELECT * FROM producer WHERE id_producer = ?;";
        try (CachedRowSet crs = ConectionFactory.getCachedRowSet()) {
            Connection conection = ConectionFactory.getConnection();
            conection.setAutoCommit(false);
            crs.addRowSetListener(new CustomRowSetListener());
            crs.setCommand(sql);
            crs.setInt(1, producer.getId());
            crs.execute(conection);
            if(!crs.next()) return;
            crs.updateString(1, producer.getName());
            crs.updateRow();
            TimeUnit.SECONDS.sleep(10);
            crs.acceptChanges();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
