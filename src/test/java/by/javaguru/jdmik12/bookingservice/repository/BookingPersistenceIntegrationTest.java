package by.javaguru.jdmik12.bookingservice.repository;

import by.javaguru.jdmik12.bookingservice.model.Bookings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers(disabledWithoutDocker = true)
class BookingPersistenceIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:17-alpine")
            .withDatabaseName("booking_test")
            .withUsername("booking")
            .withPassword("booking");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.liquibase.enabled", () -> true);
    }

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void liquibaseSchemaPersistsBookingOnPostgreSql() {
        Bookings booking = new Bookings();
        booking.setUserId(1L);
        booking.setRoomId(2L);
        booking.setCheckInDate(LocalDate.now().plusDays(1));
        booking.setCheckOutDate(LocalDate.now().plusDays(2));
        booking.setGuestsCount(2);
        booking.setTotalPrice(new BigDecimal("199.99"));
        booking.setStatus("CREATED");

        Bookings saved = bookingRepository.saveAndFlush(booking);

        assertThat(saved.getId()).isPositive();
        assertThat(bookingRepository.findById(saved.getId())).isPresent();
    }
}
