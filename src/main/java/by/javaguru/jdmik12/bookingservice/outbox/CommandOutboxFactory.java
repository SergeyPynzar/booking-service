package by.javaguru.jdmik12.bookingservice.outbox;

import by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus;
import by.javaguru.jdmik12.bookingservice.outbox.mapper.OutboxMapper;
import by.javaguru.jdmik12.bookingservice.outbox.model.Outbox;
import by.javaguru.jdmik12.bookingservice.repository.OutboxRepository;
import by.javaguru.jdmik12.bookingservice.model.Bookings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
public abstract class CommandOutboxFactory {
    protected final OutboxRepository outboxRepository;
    protected final OutboxMapper outboxMapper;

    public void buildBookingCommandOutbox(Bookings request, OutboxStatus outboxStatus) {
    }

    public void buildSecurityCommandOutbox(Bookings request, Outbox outbox, OutboxStatus outboxStatus) {
    }

    @Transactional
    public void updateStatusOutbox(Outbox outbox, OutboxStatus outboxStatus) {
        outboxMapper.toOutboxUpdate(
                outbox, outbox.getRetryCount(), outboxStatus
        );
        outboxRepository.save(outbox);
    }

}