package by.javaguru.jdmik12.bookingservice.exceptions;

public class ServiceIntegrationException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Ошибка при выполнении запроса";

    public ServiceIntegrationException() {
        super(DEFAULT_MESSAGE);
    }

    public ServiceIntegrationException(String message) {
        super(message);
    }
}
