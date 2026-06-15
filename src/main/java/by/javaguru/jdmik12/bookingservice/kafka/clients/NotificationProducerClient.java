package by.javaguru.jdmik12.bookingservice.kafka.clients;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationProducerClient {

    @Value("${integration.kafka.producer.notification.topic.name}")
    private String notificationTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CompletableFuture<SendResult<String, Object>> sendMessageWithKey(String key, Object message) {
        return kafkaTemplate
                .send(notificationTopic, key, message)
                .whenComplete(((stringStringSendResult, throwable) -> {
                    if (throwable == null) {
                        log.info("Send message to notification: {}", stringStringSendResult.getProducerRecord().topic());
                    } else {
                        log.error("Failed to send message notification ", throwable);
                    }
                }));
    }
}

