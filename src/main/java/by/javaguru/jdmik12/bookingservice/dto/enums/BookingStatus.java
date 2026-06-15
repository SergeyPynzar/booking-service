package by.javaguru.jdmik12.bookingservice.dto.enums;

public enum BookingStatus {
    CREATED("Создание заявки"),
    PENDING("Ожидает подтверждения"),
    CONFIRMED("Подтверждено"),
    CHECKED_IN("Заселился"),
    CHECKED_OUT("Выехал"),
    CANCELLED("Отменено"),
    SECURITY_FAILED("Проверка безопасности не пройдена"),
    NO_SHOW("Не явился");

    private final String description;

    BookingStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // Проверки состояний
    public boolean isActive() {
        return this == PENDING || this == CONFIRMED || this == CHECKED_IN;
    }

    public boolean canBeCancelled() {
        return this == PENDING || this == CONFIRMED;
    }

    public boolean isTerminal() {
        return this == CHECKED_OUT || this == CANCELLED || this == NO_SHOW;
    }
}
