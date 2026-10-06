package com.learning.kafka.rediscache.repository;

import com.learning.kafka.rediscache.domain.Product;
import io.lettuce.core.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findBySku(String productSku);

    Page<Product> findAllByName(Pageable pageable, String name);

    Page<Product> findAllByModel(Pageable pageable, String name);
}
