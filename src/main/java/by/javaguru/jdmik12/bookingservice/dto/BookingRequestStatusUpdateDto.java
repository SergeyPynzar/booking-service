package by.javaguru.jdmik12.bookingservice.dto;

import by.javaguru.jdmik12.bookingservice.dto.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;

public record BookingRequestStatusUpdateDto(
        @NotNull(message = "Статус бронирования обязателен")
        BookingStatus status
) {
}
