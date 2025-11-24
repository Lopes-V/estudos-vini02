package jdbc.test;

import jdbc.Dominio.Producer;
import jdbc.repository.ProducerRepository;
import jdbc.service.ProducerService;
import lombok.extern.log4j.Log4j2;

import java.sql.SQLException;
import java.util.Scanner;

@Log4j2

public class ConectionFactoryTest {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("""
                    1 --> add producer
                    2 --> remove producer
                    3 --> update producer
                    4 --> print all producers
                    5 --> find by producer name
                    6 --> find by producer id
                    7 --> quit
                    """);
            int e = sc.nextInt();
            switch (e) {
                case 1: {
                    System.out.println("Enter producer name:");
                    String name = sc.next();
                    Producer producer = Producer.builder().name(name).build();
                    ProducerService.save(producer);
                    break;
                }
                case 2: {
                    for (Producer p : ProducerRepository.findAll()) {
                        System.out.println("id: " + p.getId());
                        System.out.println("name: " + p.getName());
                        System.out.println("-------------------------------------");
                    }
                    System.out.println("enter producer id");
                    int id = sc.nextInt();
                    ProducerService.delete(id);
                    break;
                }
                case 3: {
                    for (Producer p : ProducerRepository.findAll()) {
                        System.out.println("id: " + p.getId());
                        System.out.println("name: " + p.getName());
                        System.out.println("-------------------------------------");
                    }
                    System.out.println("Enter producer id:");
                    int id = sc.nextInt();
                    String name = sc.next();
                    ProducerRepository.update(id,name);
                    break;
                }
                case 4: {
                    ProducerRepository.findAndPrintAll();
                    break;
                }
                case 5: {
                    System.out.println("Enter producer name:");
                    String name = sc.next();
                    ProducerRepository.findByName(name);
                    break;
                }
                case 6: {
                    System.out.println("Enter producer id:");
                    int id = sc.nextInt();
                    ProducerRepository.findById(id);
                    break;
                }
                case 7: {
                    System.out.println("Bye");
                    System.exit(0);
                }
            }
        }
    }
}
