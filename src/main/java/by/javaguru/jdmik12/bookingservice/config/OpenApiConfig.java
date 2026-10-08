package by.javaguru.jdmik12.bookingservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI bookingOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Booking Service API")
                        .description("REST API для управления бронированиями отеля")
                        .version("1.0.0")
                        .contact(new Contact().name("Booking Service Team")));
    }
}
