package by.javaguru.jdmik12.bookingservice.outbox.cache;

import java.time.Duration;
import java.util.UUID;

public interface OutboxLeaseCache {

    boolean tryAcquire(UUID outboxId, Duration leaseDuration);

    void release(UUID outboxId);
}
