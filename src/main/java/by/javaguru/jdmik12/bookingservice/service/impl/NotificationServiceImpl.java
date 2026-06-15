package by.javaguru.jdmik12.bookingservice.service.impl;

import by.javaguru.jdmik12.bookingservice.dto.NotificationCommand;
import by.javaguru.jdmik12.bookingservice.model.Bookings;
import by.javaguru.jdmik12.bookingservice.kafka.clients.NotificationProducerClient;
import by.javaguru.jdmik12.bookingservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationProducerClient notificationProducerClient;

    @Override
    public void sendMessage(Bookings bookings) {
        NotificationCommand command = NotificationCommand.builder()
                .message(bookings.getStatus())
                .email("test@email.ru")
                .requestType("BOOKING")
                .build();

        notificationProducerClient.sendMessageWithKey(null, command);
    }

}
