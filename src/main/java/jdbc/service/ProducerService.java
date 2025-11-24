package jdbc.service;

import jdbc.Dominio.Producer;
import jdbc.repository.ProducerRepository;

import java.sql.SQLException;

public class ProducerService {
    public static void save(Producer producer) throws SQLException {
        ProducerRepository.save(producer);
    }
    public static void delete(int id) throws SQLException {
        if(id < 0){
            throw new IllegalArgumentException("Invalid value for id: " + id);
        }
        ProducerRepository.delete(id);
    }
}
