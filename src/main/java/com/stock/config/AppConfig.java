package com.stock.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.dto.OrderDTO;
import org.springframework.util.backoff.FixedBackOff;


@Configuration
public class AppConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.dlq-topic:orders.events.DLT}")
    private String dlqTopic;

    @Bean
    public ConsumerFactory<String, Object> consumerFactory(ObjectMapper objectMapper) {

        Map<String, Class<?>> topicToClassMap = new HashMap<>();
        topicToClassMap.put("order-created", OrderDTO.class);
        topicToClassMap.put("orders.events", String.class);
        topicToClassMap.put("orders.events.DLT", String.class);

        DynamicJsonDeserializer dynamicJsonDeserializer =
                new DynamicJsonDeserializer(topicToClassMap, objectMapper);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // MUST HAVE
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        // required for JSON
        props.put("spring.json.trusted.packages", "*");

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(dynamicJsonDeserializer)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory,
            KafkaTemplate<String, Object> kafkaTemplate) {

        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        factory.setCommonErrorHandler(defaultErrorHandler(kafkaTemplate));

        return factory;
    }

    private DefaultErrorHandler defaultErrorHandler(
            KafkaTemplate<String, Object> kafkaTemplate) {

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate,
                        (rec, ex) -> new TopicPartition(dlqTopic, rec.partition()));

        return new DefaultErrorHandler(recoverer, new FixedBackOff(5000L, 2));
    }
}
