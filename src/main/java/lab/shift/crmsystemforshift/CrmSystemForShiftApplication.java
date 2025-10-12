package lab.shift.crmsystemforshift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.EnumSet;
import java.util.Set;

@EnableJpaAuditing
@SpringBootApplication
public class CrmSystemForShiftApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmSystemForShiftApplication.class, args);
    }
}
