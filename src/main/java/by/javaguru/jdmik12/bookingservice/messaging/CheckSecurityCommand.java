package by.javaguru.jdmik12.bookingservice.messaging;

public record CheckSecurityCommand(
        Long requestId,
        Long userId,
        Long roomId,
        RequestType type
) {
}
