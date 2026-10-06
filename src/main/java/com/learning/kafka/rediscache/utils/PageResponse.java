package com.learning.kafka.rediscache.utils;

import java.io.Serializable;
import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int number,
        int size,
        long totalElements,
        long totalPages
) implements Serializable {
}
