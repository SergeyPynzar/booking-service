package by.javaguru.jdmik12.bookingservice.outbox.impl;

import by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus;
import by.javaguru.jdmik12.bookingservice.outbox.cache.OutboxLeaseCache;
import by.javaguru.jdmik12.bookingservice.outbox.kafka.clients.OutboxProducerClient;
import by.javaguru.jdmik12.bookingservice.outbox.model.Outbox;
import by.javaguru.jdmik12.bookingservice.outbox.model.enums.PayloadType;
import by.javaguru.jdmik12.bookingservice.repository.OutboxRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OutboxScheduledServiceImplTest {

    @Mock private OutboxProducerClient outboxProducerClient;
    @Mock private OutboxRepository outboxRepository;
    @Mock private OutboxLeaseCache outboxLeaseCache;
    @InjectMocks private OutboxScheduledServiceImpl service;

    @Test
    void startProcessSendToOutbox_marksRecordSuccessAfterKafkaAcknowledgement() {
        Outbox outbox = new Outbox();
        outbox.setId(UUID.randomUUID());
        outbox.setRequestMessageId(12L);
        outbox.setPayloadType(PayloadType.SECURITY);
        outbox.setPayload("{\"requestId\":12}");
        outbox.setRetryCount(0);
        when(outboxRepository.findBatchForProcessing(any(), any(), anyInt())).thenReturn(List.of(outbox));
        when(outboxLeaseCache.tryAcquire(any(), any())).thenReturn(true);
        when(outboxRepository.changeStatusIfCurrent(outbox.getId(), OutboxStatus.NEW, OutboxStatus.IN_PROGRESS)).thenReturn(1);
        when(outboxProducerClient.sendMessageWithKey(any(), eq(PayloadType.SECURITY), any()))
                .thenReturn(CompletableFuture.completedFuture(null));

        service.startProcessSendToOutbox();

        verify(outboxRepository).changeStatusIfCurrent(outbox.getId(), OutboxStatus.IN_PROGRESS, OutboxStatus.SUCCESS);
    }

    @Test
    void startProcessSendToOutbox_marksRecordErrorAfterKafkaFailure() {
        Outbox outbox = new Outbox();
        outbox.setId(UUID.randomUUID());
        outbox.setRequestMessageId(12L);
        outbox.setPayloadType(PayloadType.SECURITY);
        outbox.setPayload("{\"requestId\":12}");
        outbox.setRetryCount(0);
        when(outboxRepository.findBatchForProcessing(any(), any(), anyInt())).thenReturn(List.of(outbox));
        when(outboxLeaseCache.tryAcquire(any(), any())).thenReturn(true);
        when(outboxRepository.changeStatusIfCurrent(outbox.getId(), OutboxStatus.NEW, OutboxStatus.IN_PROGRESS)).thenReturn(1);
        when(outboxProducerClient.sendMessageWithKey(any(), any(), any()))
                .thenReturn(CompletableFuture.failedFuture(new IllegalStateException("Kafka unavailable")));

        service.startProcessSendToOutbox();

        verify(outboxRepository).changeStatusIfCurrent(outbox.getId(), OutboxStatus.IN_PROGRESS, OutboxStatus.ERROR);
    }
}
