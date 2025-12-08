package com.stock.dto;

import lombok.Data;

@Data
public class KafkaEvent<T> {
    private String eventId;
    private String eventType;
    private String eventVersion;
    private String correlationId;
    private String traceId;
    private String timestamp;
    private T payload;
}