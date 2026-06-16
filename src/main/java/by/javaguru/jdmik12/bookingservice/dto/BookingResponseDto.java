package by.javaguru.jdmik12.bookingservice.dto;

import by.javaguru.jdmik12.bookingservice.dto.enums.BookingStatus;
import java.math.BigDecimal;
import java.time.LocalDate;


public record BookingResponseDto(long id,
                                 long userId,
                                 long roomId,
                                 LocalDate checkInDate,
                                 LocalDate checkOutDate,
                                 int guestsCount,
                                 String specialRequests,
                                 BigDecimal totalPrice,
                                 BookingStatus status) {

    public BookingResponseDto {
        if (status == null) status = BookingStatus.CREATED;
    }
}
