package com.stock.bean;

import java.util.Date;

import com.stock.generator.PrefixSequenceGenerator;
import com.stock.generator.PrefixSequenceIdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.IdGeneratorType;
import org.hibernate.annotations.Parameter;

@Entity
@Getter
@Setter
public class Stock {
	/*@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "stock_seq")
	@SequenceGenerator(name = "stock_seq", sequenceName = "stock_seq", allocationSize = 1)
	@Setter(AccessLevel.NONE)
	@Column(name = "stock_id")*/
    /*@Id
    @PrefixSequenceGenerator(
            prefix = "STOCK",
            sequence = "stock_id",
            initialValue = 1,
            incrementSize = 1
    )*/
    @Id
    @PrefixSequenceGenerator(
            prefix = "STOCK",
            sequence = "stock_id",
            initial_Value = 1,
            incrementSize = 1
    )
    @Column(name = "stock_id", unique = true, nullable = false, length = 20)
    private String id;

	private String name;

	private Long quantity;

	private Double sellPrice;

	private Double buyPrice;

	private String description;

	private Date purchaseDate;

	private Date expiryDate;

	private String code;

    private String brand;

    private Float costPerPiece;

    private Float totalCost;
}
