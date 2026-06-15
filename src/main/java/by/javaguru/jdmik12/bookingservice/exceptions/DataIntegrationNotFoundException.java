package by.javaguru.jdmik12.bookingservice.exceptions;

public class DataIntegrationNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Данные по запросу не найдены";

    public DataIntegrationNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public DataIntegrationNotFoundException(String message) {
        super(message);
    }
}
