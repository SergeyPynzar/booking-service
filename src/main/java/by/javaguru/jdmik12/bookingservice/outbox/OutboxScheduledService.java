package by.javaguru.jdmik12.bookingservice.outbox;

public interface OutboxScheduledService {

    void startProcessSendToOutbox();

    void startProcessUpdateStatusInCash();

}