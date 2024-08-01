package registration.uz.hgpuserregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class HgpUserRegistrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(HgpUserRegistrationApplication.class, args);
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(120);
        taskExecutor.setQueueCapacity(120);
        return taskExecutor;
    }
}
