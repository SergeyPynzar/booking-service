package by.javaguru.jdmik12.bookingservice.outbox.job;

import by.javaguru.jdmik12.bookingservice.outbox.OutboxScheduledService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enabled", havingValue = "true", matchIfMissing = true)
public class OutboxJob {
    private final OutboxScheduledService outboxScheduledService;
    private final ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

    @Scheduled(fixedDelayString = "${outbox.processing.fixed-delay:5s}")
    public void publishPendingMessages() {
        virtualThreadExecutor.submit(outboxScheduledService::startProcessSendToOutbox);
    }

    @PreDestroy
    void shutdown() {
        virtualThreadExecutor.close();
    }
}
