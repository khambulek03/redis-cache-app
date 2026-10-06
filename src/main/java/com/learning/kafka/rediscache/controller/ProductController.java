package com.learning.kafka.rediscache.controller;

import com.learning.kafka.rediscache.dto.ProductRequest;
import com.learning.kafka.rediscache.dto.ProductResponse;
import com.learning.kafka.rediscache.service.ProductService;
import com.learning.kafka.rediscache.utils.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/test")
    public String test() {
        return "Products app works, you can test your Redis caching";
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> products(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {
        PageResponse<ProductResponse> productsResponse = productService.getProducts(page, size);
        return ResponseEntity.status(OK).body(productsResponse);
    }

    @PostMapping("/product/new")
    public ResponseEntity<ProductResponse> product(@Valid @RequestBody ProductRequest request) {
        ProductResponse addedProduct = productService.create(request);
        return ResponseEntity.status(CREATED).body(addedProduct);
    }

    @GetMapping("/name")
    public ResponseEntity<PageResponse<ProductResponse>> productsByName(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {
        PageResponse<ProductResponse> productsResponse = productService.productsByName(name, page, size);
        return ResponseEntity.status(OK).body(productsResponse);
    }

    @GetMapping("/model")
    public ResponseEntity<PageResponse<ProductResponse>> productsByModel(
            @RequestParam(name = "model", required = false) String model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {
        PageResponse<ProductResponse> productsResponse = productService.productsByModel(model, page, size);
        return ResponseEntity.status(OK).body(productsResponse);
    }
}
