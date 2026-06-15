package by.javaguru.jdmik12.bookingservice.outbox.impl.factory;

import by.javaguru.jdmik12.common.accounting.message.command.AllocateBudgetCommand;
import by.javaguru.jdmik12.common.base.RequestType;
import by.javaguru.jdmik12.bookingservice.model.enums.OutboxStatus;
import by.javaguru.jdmik12.bookingservice.outbox.CommandOutboxFactory;
import by.javaguru.jdmik12.bookingservice.outbox.mapper.OutboxMapper;
import by.javaguru.jdmik12.bookingservice.outbox.model.Outbox;
import by.javaguru.jdmik12.bookingservice.repository.OutboxRepository;
import by.javaguru.jdmik12.bookingservice.model.Bookings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static by.javaguru.jdmik12.bookingservice.outbox.model.enums.PayloadType.ACCOUNTING;

@Component
@Slf4j
@Primary
public class AllocateBudgetCommandOutboxFactory extends CommandOutboxFactory {

    public AllocateBudgetCommandOutboxFactory(
            OutboxRepository outboxRepository,
            OutboxMapper outboxMapper) {
        super(outboxRepository, outboxMapper);
    }

    @Override
    @Transactional
    public void buildAllocateBudgetCommandOutbox(Bookings request, OutboxStatus outboxStatus) {
        AllocateBudgetCommand command = AllocateBudgetCommand.builder()
                .withRequestId(request.getId())
                .withBudget(request.getBudget())
                .withType(RequestType.RECRUITMENT_PR)
                .build();

        Outbox outbox = outboxMapper.toOutboxBookingCreate(
                command, ACCOUNTING, outboxStatus
        );

        outboxRepository.save(outbox);

    }

}
