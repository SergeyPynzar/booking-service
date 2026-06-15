package by.javaguru.jdmik12.bookingservice.service.impl;

import by.javaguru.jdmik12.bookingservice.model.Bookings;
import by.javaguru.jdmik12.common.notification.message.command.NotificationCommand;
import by.javaguru.jdmik12.bookingservice.kafka.clients.NotificationProducerClient;
import by.javaguru.jdmik12.bookingservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static by.javaguru.jdmik12.common.base.RequestType.RECRUITMENT_PR;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationProducerClient notificationProducerClient;

    @Override
    public void sendMessage(Bookings bookings) {
        NotificationCommand command = NotificationCommand.builder()
                .withMessage(bookings.getStatus())
                .withEmail("test@email.ru")
                .withRequestType(RECRUITMENT_PR)
                .build();

        notificationProducerClient.sendMessageWithKey(null, command);
    }

}
