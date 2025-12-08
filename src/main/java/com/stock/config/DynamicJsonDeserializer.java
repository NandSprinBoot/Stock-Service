package com.stock.config;

import com.fasterxml.jackson.databind.JavaType;
import com.stock.dto.KafkaEvent;
import com.stock.dto.OrderDTO;
import org.apache.kafka.common.serialization.Deserializer;
//import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;


public class DynamicJsonDeserializer implements Deserializer<Object> {

    private final ObjectMapper objectMapper;
    private final Map<String, Class<?>> topicToClassMap;

    public DynamicJsonDeserializer(Map<String, Class<?>> topicToClassMap,
                                   ObjectMapper objectMapper) {
        this.topicToClassMap = topicToClassMap;
        this.objectMapper = objectMapper;
    }

    @Override
    public Object deserialize(String topic, byte[] data) {
        Class<?> targetClass = topicToClassMap.get(topic);

        if (targetClass == null) {
            throw new IllegalArgumentException("No target class found for topic: " + topic);
        }

        try {
            // Special handling for KafkaEvent<OrderDTO>
            if (targetClass.equals(KafkaEvent.class)) {
                JavaType type = objectMapper.getTypeFactory()
                        .constructParametricType(KafkaEvent.class, OrderDTO.class);
                return objectMapper.readValue(data, type);
            }

            return objectMapper.readValue(data, targetClass);

        } catch (Exception e) {
            throw new RuntimeException("Deserialization failed for topic: " + topic, e);
        }
    }
}


