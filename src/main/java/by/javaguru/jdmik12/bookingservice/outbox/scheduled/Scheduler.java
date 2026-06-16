package by.javaguru.jdmik12.bookingservice.outbox.scheduled;

import by.javaguru.jdmik12.bookingservice.outbox.OutboxScheduledService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enabled", havingValue = "true", matchIfMissing = true)
public class Scheduler {
    private final OutboxScheduledService outboxScheduledService;

    @Scheduled(fixedDelay = 5000)
    public void mainProcessScheduling() {
        outboxScheduledService.startProcessSendToOutbox();
    }

    @Scheduled(fixedDelay = 10000)
    public void updateProcessScheduling() {
        outboxScheduledService.startProcessUpdateStatusInCash();
    }

}
