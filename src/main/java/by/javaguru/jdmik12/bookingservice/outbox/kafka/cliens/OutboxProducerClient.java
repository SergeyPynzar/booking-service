package by.javaguru.jdmik12.bookingservice.outbox.kafka.cliens;

import by.javaguru.jdmik12.bookingservice.outbox.cash.OutboxCache;
import by.javaguru.jdmik12.bookingservice.outbox.executor.RequestIdExtractor;
import by.javaguru.jdmik12.bookingservice.outbox.model.enums.PayloadType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus.PENDING;
import static by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus.PROCESSING;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxProducerClient {

    @Value("${integration.kafka.producer.outbox.topic.name}")
    private String outboxTopic;

    @Value("${integration.kafka.consumer.recruitment.topic.name}")
    private String replyTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RequestIdExtractor requestIdExtractor;

    public CompletableFuture<SendResult<String, Object>> sendMessageWithKey(String key, PayloadType payloadType, Object message) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(outboxTopic, key, message);
        record.headers().add("reply_topic", replyTopic.getBytes());
        record.headers().add("service_type", payloadType.name().getBytes());
        record.headers().add("payload_type", payloadType.getPayloadType().getBytes());

        return kafkaTemplate.send(record).whenComplete(this::handleSendResult);
    }

    private void handleSendResult(SendResult<String, Object> result, Throwable throwable) {
        Long requestMessageId = requestIdExtractor.extract(result.getProducerRecord().value());
        if (throwable == null) {
            OutboxCache.put(requestMessageId, PROCESSING);
            log.info("Send message : {}", result.getProducerRecord().topic());
        } else {
            OutboxCache.put(requestMessageId, PENDING);
            log.error("Failed to send message", throwable);
        }
    }

}

