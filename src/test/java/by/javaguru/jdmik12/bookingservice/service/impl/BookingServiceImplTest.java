package by.javaguru.jdmik12.bookingservice.service.impl;

import by.javaguru.jdmik12.common.accounting.message.command.AllocateBudgetCommand;
import by.javaguru.jdmik12.common.base.KafkaMessage;
import by.javaguru.jdmik12.common.base.RequestType;
import by.javaguru.jdmik12.bookingservice.base.KafkaIntegrationTest;
import by.javaguru.jdmik12.bookingservice.repository.OutboxRepository;
import by.javaguru.jdmik12.bookingservice.repository.BookingRepository;
import by.javaguru.jdmik12.bookingservice.service.BookingService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@KafkaIntegrationTest
@EmbeddedKafka(topics = {"${integration.kafka.producer.accounting.topic.name}"})
class BookingServiceImplTest {

    @Value("${integration.kafka.producer.accounting.topic.name}")
    private String accountingAllocateCommandTopic;

    @MockitoBean
    private BookingService bookingService;

    @MockitoBean
    private BookingRepository bookingRepository;

    @MockitoBean
    private OutboxRepository outboxRepository;

    private Consumer<String, KafkaMessage> consumer;

    @Autowired
    private KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    @MockitoBean
    private JpaTransactionManager transactionManager;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @BeforeEach
    void setup() {
        Map<String, Object> props = KafkaTestUtils.consumerProps("test-group", "true", embeddedKafka);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

        JsonDeserializer<KafkaMessage> jsonDeserializer = new JsonDeserializer<>(KafkaMessage.class);
        jsonDeserializer.addTrustedPackages("*");

        ConsumerFactory<String, KafkaMessage> cf = new DefaultKafkaConsumerFactory<>(
                props, new StringDeserializer(), jsonDeserializer);

        consumer = cf.createConsumer();
        embeddedKafka.consumeFromAnEmbeddedTopic(consumer, accountingAllocateCommandTopic);
    }

    @Test
    void allocationIsSentToAccountingTopic() {

        String key = UUID.randomUUID().toString();
        var command = AllocateBudgetCommand.builder()
                .withRequestId(1L)
                .withBudget(new BigDecimal("1000.00"))
                .withType(RequestType.RECRUITMENT_PR)
                .build();

        kafkaTemplate.executeInTransaction(operations -> {
            operations.send(accountingAllocateCommandTopic, key, command);
            return null;
        });

        ConsumerRecord<String, KafkaMessage> record =
                KafkaTestUtils.getSingleRecord(consumer, accountingAllocateCommandTopic);

        assertThat(record.key()).isEqualTo(key);
        assertThat(record.value()).isInstanceOf(AllocateBudgetCommand.class);
    }

}
