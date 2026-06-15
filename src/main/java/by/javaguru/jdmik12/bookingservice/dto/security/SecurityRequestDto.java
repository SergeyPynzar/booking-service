package by.javaguru.jdmik12.bookingservice.dto.security;

public record SecurityRequestDto(long requestId,
                                 String type,
                                 String name,
                                 String surname) {
}
