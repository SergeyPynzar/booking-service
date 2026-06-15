package by.javaguru.jdmik12.bookingservice.exceptions;

public class ResponseStatusException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Ошибка ответа от сервиса";

    public ResponseStatusException() {
        super(DEFAULT_MESSAGE);
    }

    public ResponseStatusException(String message) {
        super(message);
    }
}
