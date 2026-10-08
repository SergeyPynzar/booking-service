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
        return this == CHECKED_OUT || this == CANCELLED || this == NO_SHOW || this == SECURITY_FAILED;
    }

    public boolean canTransitionTo(BookingStatus target) {
        if (this == target) {
            return true;
        }

        return switch (this) {
            case CREATED -> target == PENDING || target == SECURITY_FAILED;
            case PENDING -> target == CONFIRMED || target == CANCELLED || target == NO_SHOW;
            case CONFIRMED -> target == CHECKED_IN || target == CANCELLED || target == NO_SHOW;
            case CHECKED_IN -> target == CHECKED_OUT;
            case CHECKED_OUT, CANCELLED, SECURITY_FAILED, NO_SHOW -> false;
        };
    }
}
