package registration.uz.hgpuserregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HgpUserRegistrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(HgpUserRegistrationApplication.class, args);
    }
}
