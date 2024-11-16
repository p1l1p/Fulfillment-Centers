package com.propvuebrand.fulfillmentcenters.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.propvuebrand.fulfillmentcenters.model.Product;
import com.propvuebrand.fulfillmentcenters.model.ProductStatus;
import com.propvuebrand.fulfillmentcenters.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setProductId("p1");
        testProduct.setStatus(ProductStatus.SELLABLE);
        testProduct.setFulfillmentCenter("fc1");
        testProduct.setQuantity(10);
        testProduct.setValue(100.0);
    }

    @Test
    void getAllProducts_ShouldReturnList() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(testProduct));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].productId").value("p1"))
                .andExpect(jsonPath("$[0].status").value("SELLABLE"))
                .andExpect(jsonPath("$[0].fulfillmentCenter").value("fc1"))
                .andExpect(jsonPath("$[0].quantity").value(10))
                .andExpect(jsonPath("$[0].value").value(100.0));
    }

    @Test
    void getProduct_WhenExists_ShouldReturnProduct() throws Exception {
        when(productService.getProduct(1L)).thenReturn(Optional.of(testProduct));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productId").value("p1"));
    }

    @Test
    void getProduct_WhenNotExists_ShouldReturn404() throws Exception {
        when(productService.getProduct(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProductByProductId_ShouldReturnList() throws Exception {
        when(productService.getProductByProductId("p1")).thenReturn(List.of(testProduct));

        mockMvc.perform(get("/api/products/product-id/p1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].productId").value("p1"));
    }

    @Test
    void createProduct_ShouldReturnCreated() throws Exception {
        when(productService.createProduct(any(Product.class))).thenReturn(testProduct);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productId").value("p1"));
    }

    @Test
    void updateProduct_WhenExists_ShouldReturnUpdated() throws Exception {
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(Optional.of(testProduct));

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productId").value("p1"));
    }

    @Test
    void updateProduct_WhenNotExists_ShouldReturn404() throws Exception {
        when(productService.updateProduct(eq(999L), any(Product.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_WhenExists_ShouldReturn200() throws Exception {
        when(productService.deleteProduct(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteProduct_WhenNotExists_ShouldReturn404() throws Exception {
        when(productService.deleteProduct(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProductsByStatus_ShouldReturnFilteredList() throws Exception {
        when(productService.getProductsByStatus(ProductStatus.SELLABLE))
                .thenReturn(List.of(testProduct));

        mockMvc.perform(get("/api/products/status/SELLABLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("SELLABLE"));
    }

    @Test
    void getTotalSellableValue_ShouldReturnValue() throws Exception {
        when(productService.getTotalSellableValue()).thenReturn(1000.0);

        mockMvc.perform(get("/api/products/sellable/total-value"))
                .andExpect(status().isOk())
                .andExpect(content().string("1000.0"));
    }

    @Test
    void getTotalValueByFulfillmentCenter_ShouldReturnValue() throws Exception {
        when(productService.getTotalValueByFulfillmentCenter("fc1")).thenReturn(2000.0);

        mockMvc.perform(get("/api/products/center/fc1/total-value"))
                .andExpect(status().isOk())
                .andExpect(content().string("2000.0"));
    }
}