package jdbc.repository;

import jdbc.Conn.ConectionFactory;
import jdbc.Dominio.Producer;
import lombok.extern.log4j.Log4j2;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ProducerRepository {

    // ---------------------- 1. SAVE (INSERT) ----------------------
    public static void save(Producer producer) {
        // Usa o placeholder '?' para o valor
        String sql = "INSERT INTO producer (name) VALUES (?);";

        try (Connection conn = ConectionFactory.getConnection();
             // 1. Cria o PreparedStatement
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 2. Define o valor do placeholder (segurança e tipagem)
            ps.setString(1, producer.getName());

            // 3. Executa a query SEM passar a string SQL novamente
            int rowsAffected = ps.executeUpdate();

            String message = (rowsAffected == 1) ? "Sucessfully saved" : "Failed";
            log.info("Inserted producer '{}', rows affected: {} | Status: {}",
                    producer.getName(), rowsAffected, message);

        } catch (SQLException e) {
            log.error("Error while inserting producer '{}'", producer.getName(), e);
        }
    }

    // ---------------------- 2. DELETE ----------------------
    public static void delete(int id) {
        // Usa o placeholder '?' para o valor
        String sql = "DELETE FROM producer WHERE id_producer = ?;";

        try (Connection conn = ConectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 1. Define o ID no placeholder
            ps.setInt(1, id);

            // 2. Executa a query
            int rowsAffected = ps.executeUpdate();

            String message = (rowsAffected == 1) ? "Sucessfully deleted" : "Failed";
            log.info("Deleted producer with ID: {}, rows affected: {} | Status: {}",
                    id, rowsAffected, message);

        } catch (SQLException e) {
            log.error("Error while trying to delete producer with ID: {}", id, e);
        }
    }

    // ---------------------- 3. UPDATE ----------------------
    public static void update (int id, String name) {
        // Usa placeholders '?' para o nome e para o ID
        String sql = "UPDATE producer SET name = ? WHERE id_producer = ?;";

        try (Connection conn = ConectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 1. Define o novo nome (primeiro '?')
            ps.setString(1, name);
            // 2. Define o ID (segundo '?')
            ps.setInt(2, id);

            // 3. Executa a query
            int rowsAffected = ps.executeUpdate();

            String message = (rowsAffected == 1) ? "Sucessfully updated" : "Failed";
            log.info("Update producer ID: {}, new name: {}, rows affected: {} | Status: {}",
                    id, name, rowsAffected, message);

        } catch (SQLException e) {
            log.error("Error while trying to update producer with ID: {}", id, e);
        }
    }

    // ---------------------- 4. FIND ALL ----------------------
    public static List<Producer> findAll() {
        String sql = "SELECT id_producer, name FROM producer;";
        List<Producer> producers = new ArrayList<>();

        try (Connection conn = ConectionFactory.getConnection();
             // Não precisa de ? então pode usar Statement, mas PreparedStatement é mais comum
             PreparedStatement ps = conn.prepareStatement(sql);
             // ExecuteQuery sem argumentos
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producer producer = Producer.builder()
                        .id(rs.getInt("id_producer"))
                        .name(rs.getString("name"))
                        .build();
                producers.add(producer);
            }

        } catch (SQLException e) {
            log.error("Error while listing all producers", e);
        }
        return producers;
    }

    // ---------------------- 5. FIND AND PRINT ALL ----------------------
    public static void findAndPrintAll() {
        // Reutiliza o findAll e imprime
        List<Producer> producers = findAll();
        if (producers.isEmpty()) {
            System.out.println("Nenhum produtor encontrado no banco de dados.");
            return;
        }

        System.out.println("--- Lista de Produtores ---");
        for (Producer producer : producers) {
            System.out.println("ID: " + producer.getId());
            System.out.println("Name: " + producer.getName());
            System.out.println("------------------------------------------------------");
        }
    }

    // ---------------------- 6. FIND BY NAME ----------------------
    public static List<Producer> findByName(String name) {
        // Usa o LIKE com placeholder (o '%' deve ser adicionado ao dado, não na query)
        String sql = "SELECT id_producer, name FROM producer WHERE name LIKE ?;";
        List<Producer> producers = new ArrayList<>();

        try (Connection conn = ConectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Adiciona o wildcard '%' ao valor (seguro)
            ps.setString(1, "%" + name + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producer producer = Producer.builder()
                            .id(rs.getInt("id_producer"))
                            .name(rs.getString("name"))
                            .build();
                    producers.add(producer);
                }
            }
        } catch (SQLException e) {
            log.error("Error while searching producer by name: {}", name, e);
        }

        System.out.printf("Found %d producers matching '%s'%n", producers.size(), name);
        producers.forEach(p -> System.out.println(p)); // Assume que o toString do Producer é útil
        System.out.println("------------------------------------");
        return producers;
    }

    // ---------------------- 7. FIND BY ID ----------------------
    public static Producer findById(int id) {
        String sql = "SELECT id_producer, name FROM producer WHERE id_producer = ?;";

        try (Connection conn = ConectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Producer producer = Producer.builder()
                            .id(rs.getInt("id_producer"))
                            .name(rs.getString("name"))
                            .build();

                    System.out.println("Producer found: " + producer);
                    System.out.println("------------------------------------");
                    return producer;
                }
            }
        } catch (SQLException e) {
            log.error("Error while searching producer by ID: {}", id, e);
        }
        System.out.println("Producer with ID " + id + " not found.");
        return null;
    }
    // ---------------------- 8. SHOW META DATA
    public static void showProducerMetaData() {
        String sql = "SELECT * FROM producer;";

        try (Connection conn = ConectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData metaData = rs.getMetaData();
                rs.next();
                int clmc = metaData.getColumnCount();
                for (int i = 1; i <= clmc; i++) {
                    log.info("Table name'{}'", metaData.getTableName(i));
                    log.info("Column name'{}'", metaData.getColumnName(i));
                    log.info("Column size'{}'", metaData.getColumnDisplaySize(i));
                    log.info("Column type'{}'", metaData.getColumnTypeName(i));
                }
        } catch (SQLException e) {
            log.error("Error while showing producer", e);
        }
    }
    // ---------------------- 9. SHOW DRIVER DATA
    public static void showDriverMetaData() {
        try (Connection conn = ConectionFactory.getConnection()){
            DatabaseMetaData metaData = conn.getMetaData();
            if(metaData.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE)) {
                log.info("ResultSet supports ResultSetType.SCROLL_SENSITIVE");
                if(metaData.supportsResultSetConcurrency(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
                    log.info("And supports CONCUR_UPDATABLE");
                }
            }
            if(metaData.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY)) {
                log.info("ResultSet supports ResultSetType.FORWARD_ONLY");
                if(metaData.supportsResultSetConcurrency(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
                    log.info("And supports CONCUR_UPDATABLE");
                }
            }
            if(metaData.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE)) {
                log.info("ResultSet supports ResultSetType.SCROLL_INSENSITIVE");
                if(metaData.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                    log.info("And supports CONCUR_UPDATABLE");
                }
            }
        } catch (SQLException e) {
            log.error("Error while showing producer", e);
        }
    }

    public static void showTypeScrollWorking() {
        String sql = "SELECT * FROM producer;";

        try (Connection conn = ConectionFactory.getConnection();
             Statement ps = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            // Adiciona o wildcard '%' ao valor (seguro)
            ResultSet rs = ps.executeQuery(sql);
            log.info("First row?: "+rs.first());
            log.info("Number row -->: "+rs.getRow());
            log.info(Producer.builder().id(rs.getInt("id_producer")).name(rs.getString("name")).build().toString());
            log.info("-------------------------------------------------");
            log.info("Last row?: "+rs.last());
            log.info("Number row -->: "+rs.getRow());
            log.info(Producer.builder().id(rs.getInt("id_producer")).name(rs.getString("name")).build().toString());
            log.info("-----------------------------------");
            log.info("Row absolute?: "+rs.absolute(1));
            log.info("Number row -->: "+rs.getRow());
            log.info(Producer.builder().id(rs.getInt("id_producer")).name(rs.getString("name")).build().toString());
            log.info("---------------------------------------------");
            log.info("Row relative ?: "+rs.relative(1));
            log.info("Number row -->: "+rs.getRow());
            log.info(Producer.builder().id(rs.getInt("id_producer")).name(rs.getString("name")).build().toString());
            log.info("---------------------------------------------");
            log.info("is first?'{}'", rs.first());
            log.info("isLast?'{}'", rs.last());

        } catch (SQLException e) {
            log.error("Error while find all producers");
        }
    }
}