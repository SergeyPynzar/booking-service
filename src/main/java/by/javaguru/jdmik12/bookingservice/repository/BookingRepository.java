package by.javaguru.jdmik12.bookingservice.repository;

import by.javaguru.jdmik12.bookingservice.model.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Bookings, Long> {
}
