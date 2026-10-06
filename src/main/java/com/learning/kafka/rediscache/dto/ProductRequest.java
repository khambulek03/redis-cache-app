package com.learning.kafka.rediscache.dto;

import java.math.BigDecimal;

public record ProductRequest(
        String name,
        String model,
        BigDecimal price,
        Long quantity
) {
}
