package com.stock.generator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "id_sequence")
public class IdSequence {

    @Id
    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "last_value")
    private Long lastValue;
}