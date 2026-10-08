package by.javaguru.jdmik12.bookingservice.outbox.impl;

import by.javaguru.jdmik12.bookingservice.outbox.CommandOutboxFactory;
import by.javaguru.jdmik12.bookingservice.outbox.OutboxScheduledService;
import by.javaguru.jdmik12.bookingservice.outbox.cache.OutboxLeaseCache;
import by.javaguru.jdmik12.bookingservice.messaging.StreamingCommand;
import by.javaguru.jdmik12.bookingservice.outbox.kafka.clients.OutboxProducerClient;
import by.javaguru.jdmik12.bookingservice.outbox.model.Outbox;
import by.javaguru.jdmik12.bookingservice.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus.ERROR;
import static by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus.IN_PROGRESS;
import static by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus.NEW;
import static by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus.SUCCESS;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxScheduledServiceImpl implements OutboxScheduledService {
    private final OutboxProducerClient outboxProducerClient;
    private final OutboxRepository outboxRepository;
    private final OutboxLeaseCache outboxLeaseCache;

    @Value("${outbox.cache.lease-duration:30s}")
    private Duration leaseDuration;

    @Value("${outbox.processing.batch-size:20}")
    private int batchSize;

    @Value("${outbox.processing.lookback-days:7}")
    private int lookbackDays;

    @Override
    @Transactional(transactionManager = "transactionManager")
    public void startProcessSendToOutbox() {
        List<Outbox> outboxes = outboxRepository.findBatchForProcessing(
                NEW.name(),
                Instant.now().minus(Duration.ofDays(lookbackDays)),
                batchSize);

        if (outboxes.isEmpty()) {
            log.debug("No NEW outbox records found for processing");
            return;
        }

        outboxes.stream()
                .filter(outbox -> outboxLeaseCache.tryAcquire(outbox.getId(), effectiveLeaseDuration()))
                .filter(this::claim)
                .forEach(this::processOutbox);
    }

    private void processOutbox(Outbox outbox) {
        try {
            outboxProducerClient.sendMessageWithKey(
                    "booking-" + outbox.getRequestMessageId(),
                    outbox.getPayloadType(),
                    StreamingCommand.of(outbox.getPayload())).join();
            outboxRepository.changeStatusIfCurrent(outbox.getId(), IN_PROGRESS, SUCCESS);
        } catch (Exception e) {
            log.error("Error while processing outbox id={}", outbox.getId(), e);
            outboxRepository.changeStatusIfCurrent(outbox.getId(), IN_PROGRESS, ERROR);
        } finally {
            outboxLeaseCache.release(outbox.getId());
        }
    }

    private boolean claim(Outbox outbox) {
        return outboxRepository.changeStatusIfCurrent(outbox.getId(), NEW, IN_PROGRESS) == 1;
    }

    private Duration effectiveLeaseDuration() {
        return leaseDuration == null ? Duration.ofSeconds(30) : leaseDuration;
    }

}
