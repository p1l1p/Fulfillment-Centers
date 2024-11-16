package com.propvuebrand.fulfillmentcenters.service;

import com.propvuebrand.fulfillmentcenters.model.Product;
import com.propvuebrand.fulfillmentcenters.model.ProductStatus;
import com.propvuebrand.fulfillmentcenters.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
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
        testProduct.setVersion(1L);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(testProduct));

        List<Product> result = productService.getAllProducts();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).usingRecursiveComparison().isEqualTo(testProduct);
        verify(productRepository).findAll();
    }

    @Test
    void getProduct_WhenExists_ShouldReturnProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Optional<Product> result = productService.getProduct(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(testProduct);
        verify(productRepository).findById(1L);
    }

    @Test
    void getProductByProductId_ShouldReturnProducts() {
        when(productRepository.findByProductId("p1")).thenReturn(List.of(testProduct));

        List<Product> result = productService.getProductByProductId("p1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(testProduct);
        verify(productRepository).findByProductId("p1");
    }

    @Test
    void createProduct_ShouldSaveAndReturnProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product result = productService.createProduct(testProduct);

        assertThat(result).usingRecursiveComparison().isEqualTo(testProduct);
        verify(productRepository).save(testProduct);
    }

    @Test
    void updateProduct_WhenExists_ShouldUpdateAndReturnProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Optional<Product> result = productService.updateProduct(1L, testProduct);

        assertThat(result).isPresent();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(testProduct);
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_WhenNotExists_ShouldReturnEmpty() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.updateProduct(1L, testProduct);

        assertThat(result).isEmpty();
        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProduct_WhenExists_ShouldReturnTrue() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        boolean result = productService.deleteProduct(1L);

        assertThat(result).isTrue();
        verify(productRepository).existsById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProduct_WhenNotExists_ShouldReturnFalse() {
        when(productRepository.existsById(1L)).thenReturn(false);

        boolean result = productService.deleteProduct(1L);

        assertThat(result).isFalse();
        verify(productRepository).existsById(1L);
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    void getProductsByStatus_ShouldReturnFilteredProducts() {
        when(productRepository.findByStatus(ProductStatus.SELLABLE))
                .thenReturn(List.of(testProduct));

        List<Product> result = productService.getProductsByStatus(ProductStatus.SELLABLE);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(testProduct);
        verify(productRepository).findByStatus(ProductStatus.SELLABLE);
    }

    @Test
    void getTotalSellableValue_ShouldReturnCorrectValue() {
        Double expectedValue = 1000.0;
        when(productRepository.getTotalSellableValue(ProductStatus.SELLABLE)).thenReturn(expectedValue);

        Double result = productService.getTotalSellableValue();

        assertThat(result).isEqualTo(expectedValue);
        verify(productRepository).getTotalSellableValue(ProductStatus.SELLABLE);
    }

    @Test
    void getTotalValueByFulfillmentCenter_ShouldReturnCorrectValue() {
        Double expectedValue = 1000.0;
        String center = "fc1";
        when(productRepository.getTotalValueByFulfillmentCenter(center)).thenReturn(expectedValue);

        Double result = productService.getTotalValueByFulfillmentCenter(center);

        assertThat(result).isEqualTo(expectedValue);
        verify(productRepository).getTotalValueByFulfillmentCenter(center);
    }

    @Test
    void whenConcurrentUpdate_ShouldThrowOptimisticLockingException() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class)))
                .thenThrow(new ObjectOptimisticLockingFailureException(Product.class, "p1"));

        assertThatThrownBy(() -> productService.updateProduct(1L, testProduct))
                .isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }
}