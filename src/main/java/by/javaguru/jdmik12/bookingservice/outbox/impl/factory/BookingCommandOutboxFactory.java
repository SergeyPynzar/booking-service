package by.javaguru.jdmik12.bookingservice.outbox.impl.factory;

import by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus;
import by.javaguru.jdmik12.bookingservice.messaging.CheckSecurityCommand;
import by.javaguru.jdmik12.bookingservice.messaging.RequestType;
import by.javaguru.jdmik12.bookingservice.model.Bookings;
import by.javaguru.jdmik12.bookingservice.outbox.CommandOutboxFactory;
import by.javaguru.jdmik12.bookingservice.outbox.mapper.OutboxMapper;
import by.javaguru.jdmik12.bookingservice.outbox.model.Outbox;
import by.javaguru.jdmik12.bookingservice.outbox.model.enums.PayloadType;
import by.javaguru.jdmik12.bookingservice.repository.OutboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class BookingCommandOutboxFactory extends CommandOutboxFactory {

    public BookingCommandOutboxFactory(OutboxRepository outboxRepository, OutboxMapper outboxMapper) {
        super(outboxRepository, outboxMapper);
    }

    @Override
    @Transactional(transactionManager = "transactionManager")
    public void buildBookingCommandOutbox(Bookings booking, OutboxStatus outboxStatus) {
        CheckSecurityCommand command = new CheckSecurityCommand(
                booking.getId(),
                booking.getUserId(),
                booking.getRoomId(),
                RequestType.BOOKING
        );

        Outbox outbox = outboxMapper.newOutbox(
                booking.getId(),
                command,
                PayloadType.SECURITY,
                outboxStatus
        );
        outboxRepository.save(outbox);
        log.debug("Outbox record created for booking id={}", booking.getId());
    }
}
