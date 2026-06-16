package by.javaguru.jdmik12.bookingservice.outbox.impl.factory;

import by.javaguru.jdmik12.common.base.RequestType;
import by.javaguru.jdmik12.common.security.message.command.CheckSecurityCommand;
import by.javaguru.jdmik12.bookingservice.model.enums.OutboxStatus;
import by.javaguru.jdmik12.bookingservice.outbox.CommandOutboxFactory;
import by.javaguru.jdmik12.bookingservice.outbox.mapper.OutboxMapper;
import by.javaguru.jdmik12.bookingservice.outbox.model.Outbox;
import by.javaguru.jdmik12.bookingservice.repository.OutboxRepository;
import by.javaguru.jdmik12.bookingservice.model.Bookings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static by.javaguru.jdmik12.bookingservice.outbox.model.enums.PayloadType.SECURITY;

@Component
@Slf4j
public class SecurityCommandOutboxFactory extends CommandOutboxFactory {

    public SecurityCommandOutboxFactory(
            OutboxRepository outboxRepository,
            OutboxMapper outboxMapper) {
        super(outboxRepository, outboxMapper);
    }

    @Override
    @Transactional
    public void buildSecurityCommandOutbox(Bookings request, Outbox outbox, OutboxStatus outboxStatus) {
        CheckSecurityCommand command = CheckSecurityCommand.builder()
                .withRequestId(request.getId())
                .withName(request.getName())
                .withSurname(request.getSurname())
                .withType(RequestType.RECRUITMENT_PR)
                .build();

        outboxMapper.toOutboxCheckSecurityCommand(
                outbox, command, SECURITY,
                outboxStatus, outbox.getRetryCount()
        );
        outboxRepository.save(outbox);
    }

}
