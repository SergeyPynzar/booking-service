package by.javaguru.jdmik12.bookingservice.exceptions;

public class ForbiddenException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Доступ запрещен";

    public ForbiddenException() {
        super(DEFAULT_MESSAGE);
    }

    public ForbiddenException(String message) {
        super(message);
    }
}

