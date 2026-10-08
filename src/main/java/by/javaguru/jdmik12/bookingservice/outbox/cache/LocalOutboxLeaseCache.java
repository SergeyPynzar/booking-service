package by.javaguru.jdmik12.bookingservice.outbox.cache;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LocalOutboxLeaseCache {
    private final ConcurrentHashMap<UUID, Instant> leases = new ConcurrentHashMap<>();

    public boolean tryAcquire(UUID outboxId, Duration leaseDuration) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(leaseDuration);
        return leases.compute(outboxId, (id, currentExpiry) ->
                currentExpiry == null || !currentExpiry.isAfter(now) ? expiresAt : currentExpiry) == expiresAt;
    }

    public void release(UUID outboxId) {
        leases.remove(outboxId);
    }
}
