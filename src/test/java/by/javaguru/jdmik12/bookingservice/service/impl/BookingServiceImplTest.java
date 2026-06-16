package by.javaguru.jdmik12.bookingservice.service.impl;

import by.javaguru.jdmik12.bookingservice.dto.BookingRequest;
import by.javaguru.jdmik12.bookingservice.dto.BookingRequestStatusUpdateDto;
import by.javaguru.jdmik12.bookingservice.dto.enums.BookingStatus;
import by.javaguru.jdmik12.bookingservice.exceptions.DataIntegrationNotFoundException;
import by.javaguru.jdmik12.bookingservice.mapper.BookingMapper;
import by.javaguru.jdmik12.bookingservice.model.Bookings;
import by.javaguru.jdmik12.bookingservice.outbox.impl.factory.BookingCommandOutboxFactory;
import by.javaguru.jdmik12.bookingservice.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private BookingCommandOutboxFactory commandOutboxFactory;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void createBooking_persistsAndEnqueuesOutbox() {
        BookingRequest request = BookingRequest.builder()
                .userId(10L)
                .roomId(5L)
                .checkInDate(LocalDate.now().plusDays(1))
                .checkOutDate(LocalDate.now().plusDays(3))
                .guestsCount(2)
                .totalPrice(new BigDecimal("199.99"))
                .build();

        Bookings entity = new Bookings();
        entity.setId(42L);

        when(bookingMapper.toBooking(request, BookingStatus.CREATED)).thenReturn(entity);
        when(bookingRepository.save(entity)).thenReturn(entity);

        var response = bookingService.createBooking(request);

        assertThat(response.id()).isEqualTo(42L);
        verify(commandOutboxFactory).buildBookingCommandOutbox(entity, PENDING);
    }

    @Test
    void getBookingByRequestId_throwsWhenMissing() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getBookingByRequestId(99L))
                .isInstanceOf(DataIntegrationNotFoundException.class);
    }

    @Test
    void updateBookingByRequestId_updatesStatus() {
        Bookings entity = new Bookings();
        entity.setId(1L);
        entity.setStatus(BookingStatus.CREATED.name());

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(bookingRepository.save(entity)).thenReturn(entity);
        when(bookingMapper.toDto(entity)).thenReturn(
                new by.javaguru.jdmik12.bookingservice.dto.BookingResponseDto(
                        1L, 10L, 5L,
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),
                        2, null, new BigDecimal("100"),
                        BookingStatus.CONFIRMED
                )
        );

        var dto = new BookingRequestStatusUpdateDto(BookingStatus.CONFIRMED);
        var result = bookingService.updateBookingByRequestId(1L, dto);

        assertThat(entity.getStatus()).isEqualTo(BookingStatus.CONFIRMED.name());
        assertThat(result.status()).isEqualTo(BookingStatus.CONFIRMED);
        verify(bookingRepository).save(entity);
    }
}
