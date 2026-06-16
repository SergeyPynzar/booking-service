package by.javaguru.jdmik12.bookingservice.outbox.model.enums;

import lombok.Getter;

@Getter
public enum RequestStatus {
    CREATED("Задача принята в обработку"),
    BUDGET_FAILED("Ошибка согласования бюджета"),
    BUDGET_ALLOCATED("Бюджет согласован"),
    SECURITY_FAILED("Проверка безопасности не пройдена"),
    SECURITY_PASSED("Проверка безопасности пройдена"),
    APPROVED("Заявка одобрена"),
    SUCCESS("Успешно выполнено"),
    CANCEL("Отменено"),
    FINISHED("Завершено");

    private final String requestStatusMessage;

    RequestStatus(String requestStatusMessage) {
        this.requestStatusMessage = requestStatusMessage;
    }
}
