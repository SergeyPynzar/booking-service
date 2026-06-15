package by.javaguru.jdmik12.bookingservice.dto;

import by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus;
import by.javaguru.jdmik12.bookingservice.outbox.model.enums.PayloadType;

import java.time.Instant;
import java.util.UUID;

public record OutboxDto(UUID id,
                        PayloadType payloadType,
                        Object payload,
                        OutboxStatus status,
                        Instant createdAt,
                        Instant processedAt,
                        Integer retryCount) {
}
