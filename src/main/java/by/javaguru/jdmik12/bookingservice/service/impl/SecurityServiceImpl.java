package by.javaguru.jdmik12.bookingservice.service.impl;

import by.javaguru.jdmik12.bookingservice.dto.CheckSecurityEvent;
import by.javaguru.jdmik12.bookingservice.dto.enums.BookingStatus;
import by.javaguru.jdmik12.bookingservice.exceptions.DataIntegrationNotFoundException;
import by.javaguru.jdmik12.bookingservice.exceptions.ServiceIntegrationException;
import by.javaguru.jdmik12.bookingservice.outbox.CommandOutboxFactory;
import by.javaguru.jdmik12.bookingservice.outbox.model.Outbox;
import by.javaguru.jdmik12.bookingservice.repository.OutboxRepository;
import by.javaguru.jdmik12.bookingservice.repository.BookingRepository;
import by.javaguru.jdmik12.bookingservice.model.Bookings;
import by.javaguru.jdmik12.bookingservice.service.NotificationService;
import by.javaguru.jdmik12.bookingservice.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus.TERMINAL;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {
    private final static String REQUEST_ID_ERROR_SECURITY = "Отсутствует идентификатор запроса requestId при проверке безопасности!";
    private final BookingRepository bookingRepository;
    private final CommandOutboxFactory commandOutboxFactory;
    private final NotificationService notificationService;
    private final OutboxRepository outboxRepository;

    @Override
    @Transactional
    public void handleSecurityCheckProcess(CheckSecurityEvent checkSecurityEvent) {
        validateSecurityEvent(checkSecurityEvent);

        Bookings request = getRecruitmentRequest(checkSecurityEvent.requestId());
        updateSecurityRequestStatus(request, checkSecurityEvent);
        bookingRepository.save(request);

    }

    private void validateSecurityEvent(CheckSecurityEvent event) {
        if (ObjectUtils.isEmpty(event) || event.requestId() == null) {
            throw new ServiceIntegrationException(REQUEST_ID_ERROR_SECURITY);
        }
    }

    private Bookings getRecruitmentRequest(Long requestId) {
        return bookingRepository.findById(requestId).orElseThrow(DataIntegrationNotFoundException::new);
    }

    private void updateSecurityRequestStatus(Bookings request, CheckSecurityEvent event) {
        Outbox outbox = outboxRepository.findByRequestMessageId(event.requestId()).orElseThrow();
        if (event.isPassed()) {
            log.debug("SECURITY_PASSED : {}", event.requestId());
            commandOutboxFactory.updateStatusOutbox(outbox, TERMINAL);
            request.setStatus(BookingStatus.CONFIRMED.name());
        } else {
            log.warn("SECURITY_FAILED : {}", event.requestId());
            commandOutboxFactory.updateStatusOutbox(outbox, TERMINAL);
            request.setStatus(BookingStatus.SECURITY_FAILED.name());
            notificationService.sendMessage(request);
        }
    }

}
