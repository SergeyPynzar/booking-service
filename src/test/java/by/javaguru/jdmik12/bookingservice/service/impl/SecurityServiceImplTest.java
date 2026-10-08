package by.javaguru.jdmik12.bookingservice.service.impl;

import by.javaguru.jdmik12.bookingservice.dto.CheckSecurityEvent;
import by.javaguru.jdmik12.bookingservice.dto.enums.BookingStatus;
import by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus;
import by.javaguru.jdmik12.bookingservice.model.Bookings;
import by.javaguru.jdmik12.bookingservice.outbox.CommandOutboxFactory;
import by.javaguru.jdmik12.bookingservice.outbox.model.Outbox;
import by.javaguru.jdmik12.bookingservice.repository.BookingRepository;
import by.javaguru.jdmik12.bookingservice.repository.OutboxRepository;
import by.javaguru.jdmik12.bookingservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityServiceImplTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private OutboxRepository outboxRepository;
    @Mock private CommandOutboxFactory commandOutboxFactory;
    @Mock private NotificationService notificationService;
    @InjectMocks private SecurityServiceImpl securityService;

    @Test
    void handleSecurityCheckProcess_marksBookingPendingWhenPassed() {
        Bookings booking = new Bookings();
        Outbox outbox = new Outbox();
        outbox.setStatus(OutboxStatus.IN_PROGRESS);
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));
        when(outboxRepository.findByRequestMessageId(10L)).thenReturn(Optional.of(outbox));

        securityService.handleSecurityCheckProcess(new CheckSecurityEvent(10L, true));

        assertThat(booking.getStatus()).isEqualTo(BookingStatus.PENDING.name());
        verify(commandOutboxFactory).updateStatusOutbox(outbox, OutboxStatus.SUCCESS);
        verify(notificationService, never()).sendMessage(booking);
    }

    @Test
    void handleSecurityCheckProcess_marksBookingFailedAndNotifiesWhenRejected() {
        Bookings booking = new Bookings();
        Outbox outbox = new Outbox();
        outbox.setStatus(OutboxStatus.IN_PROGRESS);
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));
        when(outboxRepository.findByRequestMessageId(10L)).thenReturn(Optional.of(outbox));

        securityService.handleSecurityCheckProcess(new CheckSecurityEvent(10L, false));

        assertThat(booking.getStatus()).isEqualTo(BookingStatus.SECURITY_FAILED.name());
        verify(notificationService).sendMessage(booking);
    }
}
