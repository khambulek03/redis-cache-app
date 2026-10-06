package com.learning.kafka.rediscache.utils;

import com.learning.kafka.rediscache.domain.Product;
import com.learning.kafka.rediscache.dto.ProductResponse;
import org.springframework.stereotype.Service;

@Service
public class ApplicationResponseBuilder {

    public ProductResponse product(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .model(product.getModel())
                .price(product.getPrice())
                .available(product.isAvailable())
                .quantity(product.getQuantity())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .version(product.getVersion())
                .build();
    }
}
