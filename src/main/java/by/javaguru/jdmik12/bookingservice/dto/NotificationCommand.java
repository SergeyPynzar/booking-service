package by.javaguru.jdmik12.bookingservice.dto;

import lombok.Builder;

@Builder
public record NotificationCommand(String message,
                                  String email,
                                  String requestType) {
}
