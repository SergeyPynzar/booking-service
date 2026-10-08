package by.javaguru.jdmik12.bookingservice.outbox.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisOutboxLeaseCache implements OutboxLeaseCache {
    private static final String KEY_PREFIX = "booking-service:outbox:lease:";

    private final StringRedisTemplate redisTemplate;
    private final LocalOutboxLeaseCache localCache;

    @Override
    public boolean tryAcquire(UUID outboxId, Duration leaseDuration) {
        try {
            Boolean acquired = redisTemplate.opsForValue()
                    .setIfAbsent(key(outboxId), "locked", leaseDuration);
            return Boolean.TRUE.equals(acquired);
        } catch (DataAccessException exception) {
            log.warn("Redis unavailable; using local outbox lease cache", exception);
            return localCache.tryAcquire(outboxId, leaseDuration);
        }
    }

    @Override
    public void release(UUID outboxId) {
        try {
            redisTemplate.delete(key(outboxId));
        } catch (DataAccessException exception) {
            log.warn("Redis unavailable; releasing local outbox lease", exception);
        }
        localCache.release(outboxId);
    }

    private String key(UUID outboxId) {
        return KEY_PREFIX + outboxId;
    }
}
