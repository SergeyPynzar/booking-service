package by.javaguru.jdmik12.bookingservice.outbox.impl.factory;

import by.javaguru.jdmik12.common.profiler.message.command.CurriculumVitaeCreateCommand;
import by.javaguru.jdmik12.bookingservice.model.enums.OutboxStatus;
import by.javaguru.jdmik12.bookingservice.outbox.CommandOutboxFactory;
import by.javaguru.jdmik12.bookingservice.outbox.mapper.OutboxMapper;
import by.javaguru.jdmik12.bookingservice.outbox.model.Outbox;
import by.javaguru.jdmik12.bookingservice.repository.OutboxRepository;
import by.javaguru.jdmik12.bookingservice.model.Bookings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static by.javaguru.jdmik12.common.base.RequestType.RECRUITMENT_PR;
import static by.javaguru.jdmik12.bookingservice.outbox.model.enums.PayloadType.PROFILER;

@Component
@Slf4j
public class CurriculumVitaeCreateCommandOutboxFactory extends CommandOutboxFactory {
    private final static String DEFAULT_CITY = "Moscow";

    public CurriculumVitaeCreateCommandOutboxFactory(
            OutboxRepository outboxRepository,
            OutboxMapper outboxMapper) {
        super(outboxRepository, outboxMapper);
    }


    @Override
    @Transactional
    public void buildCurriculumVitaeCreateCommandOutbox(Bookings request, Outbox outbox, OutboxStatus outboxStatus) {
        CurriculumVitaeCreateCommand command = CurriculumVitaeCreateCommand.builder()
                .withRequestId(request.getId())
                .withRequestType(RECRUITMENT_PR)
                .withName(request.getName())
                .withSurname(request.getSurname())
                .withPositionId(request.getPositionId())
                .withCountryId(request.getCountryId())
                .withIsReadyForRemoteWork(request.isReadyForRemoteWork())
                .withIsReadyToRelocate(request.isReadyToRelocate())
                .withCity(DEFAULT_CITY)
                .build();


        outboxMapper.toOutboxCurriculumVitaeCreateCommand(
                outbox, command, PROFILER,
                outboxStatus, outbox.getRetryCount()
        );
        outboxRepository.save(outbox);

    }

}
