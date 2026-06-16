package by.javaguru.jdmik12.bookingservice.outbox.model.enums;

import lombok.Getter;

@Getter
public enum PayloadType {
    SECURITY("by.javaguru.jdmik12.bookingservice.messaging.CheckSecurityCommand"),
    PAYMENT("by.javaguru.jdmik12.bookingservice.messaging.ReservePaymentCommand"),
    ROOM("by.javaguru.jdmik12.bookingservice.messaging.HoldRoomCommand"),
    NOTIFICATION("by.javaguru.jdmik12.bookingservice.messaging.NotificationDispatchCommand");

    private final String payloadType;

    PayloadType(String payloadType) {
        this.payloadType = payloadType;
    }
}
