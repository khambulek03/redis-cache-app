package com.learning.kafka.rediscache.service;

import com.learning.kafka.rediscache.domain.Product;
import com.learning.kafka.rediscache.dto.ProductRequest;
import com.learning.kafka.rediscache.dto.ProductResponse;
import com.learning.kafka.rediscache.utils.ApplicationResponseBuilder;
import com.learning.kafka.rediscache.utils.PageResponse;
import com.learning.kafka.rediscache.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ApplicationResponseBuilder builder;

    private static PageResponse<ProductResponse> buildPage(Page<ProductResponse> products) {
        return new PageResponse<>(
                products.getContent(),
                products.getNumber(),
                products.getSize(),
                products.getTotalElements(),
                products.getTotalPages()
        );
    }

    private String skuBuilder(String name, String model) {
        return format("PN%sPM%s", name.toUpperCase(), model.toUpperCase());
    }

    @Cacheable(value = "products", key = "#page + '-' + #size")
    public PageResponse<ProductResponse> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<ProductResponse> products = productRepository.findAll(pageable).map(builder::product);
        log.info("Database is hit for all products");
        return buildPage(products);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "productsByName", allEntries = true),
            @CacheEvict(value = "productsByModel", allEntries = true)
    })
    public ProductResponse create(ProductRequest request) {
        String productSku = skuBuilder(request.name(), request.model());
        Optional<Product> fromDatabase = productRepository.findBySku(productSku);
        if (fromDatabase.isPresent()) {
            Product prod = fromDatabase.get();
            long quantity = prod.getQuantity() + request.quantity();
            prod.setQuantity(quantity);
            prod.setAvailable(true);

            Product updated = productRepository.save(prod);
            return builder.product(updated);
        }

        Product product = Product.builder()
                .name(request.name())
                .model(request.model())
                .sku(productSku)
                .price(request.price())
                .quantity(request.quantity())
                .available(true)
                .build();

        Product saved = productRepository.save(product);
        log.info("Cache 'products/ByName/ByModel' is evicted");
        return builder.product(saved);
    }

    @Cacheable(value = "productsByName", key = "#name + '-' + #page + '-' + #size")
    public PageResponse<ProductResponse> productsByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<ProductResponse> products = productRepository.findAllByName(pageable, name).map(builder::product);
        log.info("Database is hit for products by name");
        return buildPage(products);
    }

    @Cacheable(value = "productsByModel", key = "#model + '-' + #page + '-' + #size")
    public PageResponse<ProductResponse> productsByModel(String model, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("model").ascending());
        Page<ProductResponse> products = productRepository.findAllByModel(pageable, model).map(builder::product);
        log.info("Database is hit for products by model");
        return buildPage(products);
    }
}
