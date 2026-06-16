package by.javaguru.jdmik12.bookingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;

@Configuration
public class JpaConfig {

    @Bean
    @Primary
    JpaTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }
}
