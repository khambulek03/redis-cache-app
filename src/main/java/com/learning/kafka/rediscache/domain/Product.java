package com.learning.kafka.rediscache.domain;

import com.learning.kafka.rediscache.utils.EntityAuditing;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
        name = "products",
        uniqueConstraints = @UniqueConstraint(columnNames = "model"))
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class Product extends EntityAuditing {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private String name;

    @Column(nullable = false, unique = true, updatable = false)
    private String model;

    @Column(nullable = false, unique = true, updatable = false)
    private String sku;

    @DecimalMin("0.0")
    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal price;

    @Min(0)
    @Builder.Default
    @Column(nullable = false)
    private Long quantity = 1L;

    @Builder.Default
    @Column(nullable = false)
    private boolean available = true;
}
