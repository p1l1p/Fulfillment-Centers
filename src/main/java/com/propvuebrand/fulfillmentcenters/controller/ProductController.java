package com.propvuebrand.fulfillmentcenters.controller;

import com.propvuebrand.fulfillmentcenters.model.Product;
import com.propvuebrand.fulfillmentcenters.model.ProductStatus;
import com.propvuebrand.fulfillmentcenters.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return productService.getProduct(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/product-id/{productId}")
    public List<Product> getProductByProductId(@PathVariable String productId) {
        return productService.getProductByProductId(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@Valid @RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
                                                 @Valid @RequestBody Product product) {
        return productService.updateProduct(id, product)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/status/{status}")
    public List<Product> getProductsByStatus(@PathVariable String status) {
        return productService.getProductsByStatus(ProductStatus.valueOf(status.toUpperCase()));
    }

    @GetMapping("/sellable/total-value")
    public Double getTotalSellableValue() {
        return productService.getTotalSellableValue();
    }

    @GetMapping("/center/{center}/total-value")
    public Double getTotalValueByFulfillmentCenter(@PathVariable String center) {
        return productService.getTotalValueByFulfillmentCenter(center);
    }
}