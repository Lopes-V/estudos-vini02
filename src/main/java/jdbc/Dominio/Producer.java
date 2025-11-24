package jdbc.Dominio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;

@Value
@Builder
@Log4j2
public class Producer {
    Integer id;
    String name;
}