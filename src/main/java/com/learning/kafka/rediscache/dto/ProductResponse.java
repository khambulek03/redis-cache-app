package com.learning.kafka.rediscache.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class ProductResponse {

    private UUID id;

    private String name;

    private String model;

    private BigDecimal price;

    private Long quantity;

    private boolean available;

    private Instant createdAt;

    private Instant updatedAt;

    private Long version;
}
