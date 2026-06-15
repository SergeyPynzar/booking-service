package by.javaguru.jdmik12.bookingservice.exceptions;

import java.time.ZonedDateTime;

public record ErrorResponse(int statusCode, String message, ZonedDateTime timeStamp) {
}
