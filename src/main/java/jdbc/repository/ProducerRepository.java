package jdbc.repository;

import jdbc.Conn.ConectionFactory;
import jdbc.Dominio.Producer;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ProducerRepository {

    public static void save(Producer producer) throws SQLException {
        // 1. A query deve listar as colunas e usar '?' como placeholder
        String sql = "INSERT INTO `anime_store`.`producer` (`name`) VALUES ('%s');".formatted(producer.getName());

        // 2. USE PreparedStatement, não Statement
        try (Connection conn = ConectionFactory.getConnection();
             Statement ps = conn.prepareStatement(sql)) {

            // 4. Executa a query
            String rowsAffected;
            if (ps.executeUpdate(sql) == 1) {
                rowsAffected = "Sucessfully saved";
            } else {
                rowsAffected = "Failed";
            }
            log.info("Inserted producer '{}' in the database, rows affected '{}' ", producer.getName(), rowsAffected);

        } catch (SQLException e) {
            log.error("Error while inserting producer '{}'", producer.getName(), e);
        }
    }
    public static void delete(int id) throws SQLException {
        // 1. A query deve listar as colunas e usar '?' como placeholder
        String sql = "DELETE FROM `anime_store`.`producer` WHERE (`id_producer` = '%d');".formatted(id);

        // 2. USE PreparedStatement, não Statement
        try (Connection conn = ConectionFactory.getConnection();
             Statement ps = conn.prepareStatement(sql)) {

            // 4. Executa a query
            String rowsAffected;
            if (ps.executeUpdate(sql) == 1) {
                rowsAffected = "Sucessfully deleted";
            } else {
                rowsAffected = "Failed";
            }
            log.info("Deleted producer '{}' in the database, rows affected '{}' ", id, rowsAffected);

        } catch (SQLException e) {
            log.error("Error while trying to delete producer '{}'", id, e);
        }
    }
    public static void update (int id,String name) throws SQLException {
        // 1. A query deve listar as colunas e usar '?' como placeholder
        String sql = "UPDATE `anime_store`.`producer`SET `name` ='%s' WHERE(`id_producer` ='%d');".formatted(name,id);

        // 2. USE PreparedStatement, não Statement
        try (Connection conn = ConectionFactory.getConnection();
             Statement ps = conn.prepareStatement(sql)) {

            // 4. Executa a query
            String rowsAffected;
            if (ps.executeUpdate(sql) == 1) {
                rowsAffected = "Sucessfully updated";
            } else {
                rowsAffected = "Failed";
            }
            log.info("Update producer '{}' in the database, rows affected '{}' ", id, rowsAffected);

        } catch (SQLException e) {
            log.error("Error while trying to update producer '{}'", id, e);
        }
    }
    public static List<Producer> findAll() {
        String sql = "SELECT * FROM anime_store.producer;";
        List<Producer> producers = new ArrayList<>();

        try (Connection conn = ConectionFactory.getConnection();
             Statement ps = conn.prepareStatement(sql);
             // Use executeQuery() para queries de busca, que retorna um ResultSet
             ResultSet rs = ps.executeQuery(sql)) {

            // Itera sobre as linhas retornadas
            while (rs.next()) {
                // Mapeia os dados do ResultSet para o objeto Producer
                Producer producer = Producer.builder()
                        .id(rs.getInt("id_producer"))
                        .name(rs.getString("name"))
                        .build();

                producers.add(producer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return producers;
    }
    public static void findAndPrintAll() {
        String sql = "SELECT * FROM anime_store.producer;";

        try (Connection conn = ConectionFactory.getConnection();
             Statement ps = conn.prepareStatement(sql);
             // Use executeQuery() para queries de busca, que retorna um ResultSet
             ResultSet rs = ps.executeQuery(sql)) {

            // Itera sobre as linhas retornadas
            while (rs.next()) {
                // Mapeia os dados do ResultSet para o objeto Producer
                Producer producer = Producer.builder()
                        .id(rs.getInt("id_producer"))
                        .name(rs.getString("name"))
                        .build();
                System.out.println("id: " + producer.getId());
                System.out.println("name: " + producer.getName());
                System.out.println("------------------------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void findByName(String name) throws SQLException {
        String sql = "SELECT * FROM anime_store.producer WHERE name like '%s';".formatted("%"+ name +"%");

        try (Connection conn = ConectionFactory.getConnection();
             Statement ps = conn.prepareStatement(sql);
             // Use executeQuery() para queries de busca, que retorna um ResultSet
             ResultSet rs = ps.executeQuery(sql)) {

            // Itera sobre as linhas retornadas
            while (rs.next()) {
                // Mapeia os dados do ResultSet para o objeto Producer
                Producer producer = Producer.builder()
                        .id(rs.getInt("id_producer"))
                        .name(rs.getString("name"))
                        .build();
                if(producer.getName().equals(name)) {
                    System.out.println("producer found");
                    System.out.println(producer);
                    System.out.println("------------------------------------");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void findById(int id) throws SQLException {
        //SELECT * FROM anime_store.producer WHERE name like '%s';".formatted("%"+ name +"%");
        String sql = "SELECT * FROM anime_store.producer WHERE id_producer = %d;".formatted(id);

        try (Connection conn = ConectionFactory.getConnection();
             Statement ps = conn.prepareStatement(sql);
             // Use executeQuery() para queries de busca, que retorna um ResultSet
             ResultSet rs = ps.executeQuery(sql)) {

            // Itera sobre as linhas retornadas
            while (rs.next()) {
                // Mapeia os dados do ResultSet para o objeto Producer
                Producer producer = Producer.builder()
                        .id(rs.getInt("id_producer"))
                        .name(rs.getString("name"))
                        .build();
                if(producer.getId() == id) {
                    System.out.println("producer found");
                    System.out.println(producer);
                    System.out.println("------------------------------------");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}