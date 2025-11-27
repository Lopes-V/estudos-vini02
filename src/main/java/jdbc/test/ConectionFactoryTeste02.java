package jdbc.test;

import jdbc.Dominio.Producer;
import jdbc.repository.ProducerRepository;
import jdbc.repository.ProducerRepositoryRowSet;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Log4j2
public class ConectionFactoryTeste02 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("""
                1 --> find by name
                2 --> update
                3 --> quit
                """);
        int escolha = sc.nextInt();
        switch (escolha) {
            case 1:{
                System.out.println("enter producer name");
                String name = sc.next();
                List<Producer> p = ProducerRepositoryRowSet.findByNameJdbcRowSet(name);
                if(!p.isEmpty())p.forEach(producer ->
                        System.out.println("id -> "+ producer.getId() +
                        "\nname -> " + producer.getName()));
                break;
            }
            case 2:{
                ProducerRepository.findAndPrintAll();
                System.out.println("enter producer name");
                String name = sc.next();
                System.out.println("enter producer id");
                int id = sc.nextInt();
                ProducerRepositoryRowSet.updateJdbcRowSet(Producer.builder().id(id).name(name).build());
            }
        }
    }
}
