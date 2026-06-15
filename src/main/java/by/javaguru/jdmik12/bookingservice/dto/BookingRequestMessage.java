package by.javaguru.jdmik12.bookingservice.dto;

import by.javaguru.jdmik12.bookingservice.dto.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookingRequestMessage(long id,
                                    Long userId,
                                    Long roomId,
                                    LocalDate checkInDate,
                                    LocalDate checkOutDate,
                                    int guestsCount,
                                    String specialRequests,
                                    BigDecimal totalPrice,
                                    BookingStatus status) {

    public BookingRequestMessage {
        if (status == null) status = BookingStatus.CREATED;
    }
}
