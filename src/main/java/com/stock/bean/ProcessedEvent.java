package com.stock.bean;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name="processed_events")
@Data
public class ProcessedEvent {
    @Id
    private String eventId;
    private Instant processedAt = Instant.now();
    public ProcessedEvent() {}
    public ProcessedEvent(String eventId){
        this.eventId=eventId;
    }
}
