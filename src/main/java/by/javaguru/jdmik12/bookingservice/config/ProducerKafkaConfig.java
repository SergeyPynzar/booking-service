package by.javaguru.jdmik12.bookingservice.config;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
public class ProducerKafkaConfig {

    @Bean
    ProducerFactory<String, Object> producerFactory(KafkaProperties kafkaProperties) {
        kafkaProperties.getProducer().setKeySerializer(StringSerializer.class);
        kafkaProperties.getProducer().setValueSerializer(JsonSerializer.class);
        var producerFactory = new DefaultKafkaProducerFactory<String, Object>(kafkaProperties.buildProducerProperties());
        producerFactory.setTransactionIdPrefix(kafkaProperties.getProducer().getTransactionIdPrefix());

        return producerFactory;
    }

    @Bean
    KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        var kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setObservationEnabled(true);

        return kafkaTemplate;
    }

}

