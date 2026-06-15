package by.javaguru.jdmik12.bookingservice.outbox.impl;

import by.javaguru.jdmik12.bookingservice.outbox.CommandOutboxFactory;
import by.javaguru.jdmik12.bookingservice.outbox.OutboxScheduledService;
import by.javaguru.jdmik12.bookingservice.outbox.cash.OutboxCache;
import by.javaguru.jdmik12.bookingservice.outbox.kafka.cliens.OutboxProducerClient;
import by.javaguru.jdmik12.bookingservice.outbox.model.Outbox;
import by.javaguru.jdmik12.bookingservice.repository.OutboxRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxScheduledServiceImpl implements OutboxScheduledService {
    private final static Integer DEFAULT_TIMEOUT_RETRY_COUNT = 20;
    private final static String TIMEOUT_ERROR_MESSAGE = "Время обработки запроса завершено! requestId: {}";
    private final OutboxProducerClient outboxProducerClient;
    private final CommandOutboxFactory commandOutboxFactory;
    private final OutboxRepository outboxRepository;
    private final Tracer tracer;

    @Override
    @Transactional
    public void startProcessSendToOutbox() {
        List<Outbox> outboxes = outboxRepository.findAllByEventTypeAndStatusBlocked(PENDING.name());

        for (Outbox outbox : outboxes) {
            if (isTimeoutExceeded(outbox)) {
                handleTimeout(outbox);
                continue;
            }
            processOutbox(outbox);
        }
    }

    private void processOutbox(Outbox outbox) {
        Span childSpan = null;

        try {
            TraceContext parentContext = buildParentContext(outbox);

            childSpan = tracer.spanBuilder()
                    .setParent(parentContext)
                    .name("outbox.process")
                    .start();

            try (var scope = tracer.withSpan(childSpan)) {
                outboxProducerClient.sendMessageWithKey(
                        StringUtils.EMPTY,
                        outbox.getPayloadType(),
                        StreamingCommand.builder()
                                .withPayload(outbox.getPayload())
                                .build());

                updateOutboxAfterSuccess(outbox, childSpan);
            }

        } catch (Exception e) {
            if (childSpan != null) {
                childSpan.error(e);
            }

            log.error("Error while processing outbox id={}", outbox.getId(), e);
            updateOutboxAfterError(outbox);

        } finally {
            if (childSpan != null) {
                childSpan.end();
            }
        }
    }

    @Override
    public void startProcessUpdateStatusInCash() {
        if (OutboxCache.size() > 0) {
            OutboxCache.getAll().keySet().forEach(key -> {
                outboxRepository.findByRequestMessageId(key)
                        .ifPresentOrElse(outbox -> {
                            if (outbox.getStatus() == PROCESSING)
                                commandOutboxFactory.updateStatusOutbox(outbox, OutboxCache.get(key));
                            OutboxCache.remove(key);
                        }, () -> {
                            OutboxCache.remove(key);
                        });
            });
        }

    }

    private TraceContext buildParentContext(Outbox outbox) {
        return tracer.traceContextBuilder()
                .traceId(outbox.getTraceId())
                .spanId(outbox.getSpanId())
                .sampled(true)
                .build();
    }

    private void updateOutboxAfterSuccess(Outbox outbox, Span childSpan) {
        outbox.setTraceId(childSpan.context().traceId());
        outbox.setSpanId(childSpan.context().spanId());
        commandOutboxFactory.updateStatusOutbox(outbox, PROCESSING);
    }

    private void updateOutboxAfterError(Outbox outbox) {
        commandOutboxFactory.updateStatusOutbox(outbox, PENDING);
    }

    private boolean isTimeoutExceeded(Outbox outbox) {
        return outbox.getRetryCount() >= DEFAULT_TIMEOUT_RETRY_COUNT;
    }

    private void handleTimeout(Outbox outbox) {
        log.error(TIMEOUT_ERROR_MESSAGE, outbox.getRequestMessageId());
        commandOutboxFactory.updateStatusOutbox(outbox, TIMEOUT);
    }

}
