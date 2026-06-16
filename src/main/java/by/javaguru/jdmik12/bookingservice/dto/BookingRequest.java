package by.javaguru.jdmik12.bookingservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BookingRequest(@NotNull(message = "ID пользователя обязательно")
                             Long userId,

                             @NotNull(message = "ID отеля/номера обязательно")
                             Long roomId,

                             @NotNull(message = "Дата заезда обязательна")
                             @FutureOrPresent(message = "Дата заезда не может быть в прошлом")
                             LocalDate checkInDate,

                             @NotNull(message = "Дата выезда обязательна")
                             @Future(message = "Дата выезда должна быть в будущем")
                             LocalDate checkOutDate,

                             @Min(value = 1, message = "Количество гостей минимум 1")
                             @Max(value = 10, message = "Максимум 10 гостей")
                             int guestsCount,

                             @Size(max = 500, message = "Особые пожелания не более 500 символов")
                             String specialRequests,

                             @NotNull(message = "Стоимость проживания")
                             BigDecimal totalPrice
) {
    // Валидация: дата выезда после даты заезда
    public BookingRequest {
        if (checkOutDate != null && checkInDate != null &&
                checkOutDate.isBefore(checkInDate)) {
            throw new IllegalArgumentException("Дата выезда должна быть позже даты заезда");
        }
    }
}
