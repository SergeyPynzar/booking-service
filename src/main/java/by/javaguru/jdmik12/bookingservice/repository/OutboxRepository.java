package by.javaguru.jdmik12.bookingservice.repository;

import by.javaguru.jdmik12.bookingservice.outbox.model.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox, UUID> {

    Optional<Outbox> findByRequestMessageId(Long requestMessageId);

    @Query(value = "SELECT * FROM outbox " +
            "WHERE status = :status " +
            "AND created_at >= :createdAfter " +
            "ORDER BY created_at " +
            "FOR UPDATE SKIP LOCKED " +
            "LIMIT :limit", nativeQuery = true)
    List<Outbox> findBatchForProcessing(@Param("status") String status,
                                        @Param("createdAfter") Instant createdAfter,
                                        @Param("limit") int limit);

    @Modifying
    @Query("UPDATE Outbox outbox SET outbox.status = :targetStatus, outbox.processedAt = CURRENT_TIMESTAMP " +
            "WHERE outbox.id = :id AND outbox.status = :expectedStatus")
    int changeStatusIfCurrent(@Param("id") UUID id,
                              @Param("expectedStatus") by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus expectedStatus,
                              @Param("targetStatus") by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus targetStatus);

}
