package by.javaguru.jdmik12.bookingservice.kafka.listeners;

import by.javaguru.jdmik12.bookingservice.dto.CheckSecurityEvent;
import by.javaguru.jdmik12.bookingservice.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics = "${integration.kafka.consumer.booking.topic.name}", containerFactory = "kafkaListenerContainerFactory")
public class KafkaConsumerListener {
    private final SecurityService securityService;

    @KafkaHandler
    public void consumeMessage(
            @Payload CheckSecurityEvent event) {
        securityService.handleSecurityCheckProcess(event);
    }

}

