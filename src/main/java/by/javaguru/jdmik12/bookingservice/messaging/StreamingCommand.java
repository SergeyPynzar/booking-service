package by.javaguru.jdmik12.bookingservice.messaging;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StreamingCommand(Object payload) {

    public static StreamingCommand of(Object payload) {
        return new StreamingCommand(payload);
    }
}
