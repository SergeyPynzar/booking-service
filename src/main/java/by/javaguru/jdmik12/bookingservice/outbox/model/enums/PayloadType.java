package by.javaguru.jdmik12.bookingservice.outbox.model.enums;

import lombok.Getter;

@Getter
public enum PayloadType {
    SECURITY("by.javaguru.jdmik12.bookingservice.messaging.CheckSecurityCommand");

    private final String payloadType;

    PayloadType(String payloadType) {
        this.payloadType = payloadType;
    }
}
