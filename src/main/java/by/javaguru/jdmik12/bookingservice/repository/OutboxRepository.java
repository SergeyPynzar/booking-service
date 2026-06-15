package by.javaguru.jdmik12.bookingservice.repository;

import by.javaguru.jdmik12.bookingservice.outbox.model.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    @Query(value = "SELECT * FROM outbox " +
            "WHERE status = :status " +
            "FOR UPDATE SKIP LOCKED " +
            "LIMIT 20"
            , nativeQuery = true)
    List<Outbox> findAllByEventTypeAndStatusBlocked(String status);

    Optional<Outbox> findByRequestMessageId(Long requestMessageId);

}
