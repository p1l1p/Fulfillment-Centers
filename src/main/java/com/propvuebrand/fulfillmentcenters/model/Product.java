package com.propvuebrand.fulfillmentcenters.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String productId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @NotNull
    private String fulfillmentCenter;

    @NotNull
    @Positive
    private Integer quantity;

    @NotNull
    @Positive
    private Double value;

    @Version
    private Long version;

    public Product() {
    }

    public Product(String productId, ProductStatus status, String fulfillmentCenter, Integer quantity, Double value) {
        this.productId = productId;
        this.status = status;
        this.fulfillmentCenter = fulfillmentCenter;
        this.quantity = quantity;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public String getFulfillmentCenter() {
        return fulfillmentCenter;
    }

    public void setFulfillmentCenter(String fulfillmentCenter) {
        this.fulfillmentCenter = fulfillmentCenter;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}