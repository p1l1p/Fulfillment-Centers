package com.propvuebrand.fulfillmentcenters.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ProductTest {

    private Validator validator;
    private Product product;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        product = new Product();
        product.setProductId("p1");
        product.setStatus(ProductStatus.SELLABLE);
        product.setFulfillmentCenter("fc1");
        product.setQuantity(10);
        product.setValue(100.0);
    }

    @Test
    void whenAllFieldsValid_thenNoValidationErrors() {
        var violations = validator.validate(product);
        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @NullSource
    void whenStatusIsNull_thenValidationFails(ProductStatus status) {
        product.setStatus(status);
        var violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("status");
    }

    @ParameterizedTest
    @ValueSource(strings = {"INVALID_STATUS", "OTHER_STATUS"})
    void whenStatusIsInvalid_thenValidationFails(String invalidStatus) {
        assertThatThrownBy(() -> product.setStatus(ProductStatus.valueOf(invalidStatus)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void whenQuantityIsInvalid_thenValidationFails(Integer quantity) {
        product.setQuantity(quantity);
        var violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("quantity");
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, 0.0})
    void whenValueIsInvalid_thenValidationFails(Double value) {
        product.setValue(value);
        var violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("value");
    }

    @Test
    void whenFulfillmentCenterIsNull_thenValidationFails() {
        product.setFulfillmentCenter(null);
        var violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("fulfillmentCenter");
    }
}