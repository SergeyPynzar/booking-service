package by.javaguru.jdmik12.bookingservice.outbox;

import by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus;
import by.javaguru.jdmik12.bookingservice.model.Bookings;
import by.javaguru.jdmik12.bookingservice.outbox.mapper.OutboxMapper;
import by.javaguru.jdmik12.bookingservice.outbox.model.Outbox;
import by.javaguru.jdmik12.bookingservice.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
public abstract class CommandOutboxFactory {
    protected final OutboxRepository outboxRepository;
    protected final OutboxMapper outboxMapper;

    public abstract void buildBookingCommandOutbox(Bookings booking, OutboxStatus outboxStatus);

    @Transactional(transactionManager = "transactionManager")
    public void updateStatusOutbox(Outbox outbox, OutboxStatus outboxStatus) {
        if (outboxStatus == OutboxStatus.ERROR) {
            outbox.setRetryCount(outbox.getRetryCount() + 1);
        }
        outboxMapper.toOutboxUpdate(outbox, outboxStatus);
        outboxRepository.save(outbox);
    }
}
