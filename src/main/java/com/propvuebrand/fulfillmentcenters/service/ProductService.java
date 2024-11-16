package com.propvuebrand.fulfillmentcenters.service;

import com.propvuebrand.fulfillmentcenters.model.Product;
import com.propvuebrand.fulfillmentcenters.model.ProductStatus;
import com.propvuebrand.fulfillmentcenters.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Product> getProduct(Long id) {
        return productRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductByProductId(String productId) {
        return productRepository.findByProductId(productId);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> updateProduct(Long id, Product product) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    product.setId(id);
                    product.setVersion(existingProduct.getVersion());
                    return productRepository.save(product);
                });
    }

    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByStatus(ProductStatus status) {
        return productRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public Double getTotalSellableValue() {
        return productRepository.getTotalSellableValue(ProductStatus.SELLABLE);
    }

    @Transactional(readOnly = true)
    public Double getTotalValueByFulfillmentCenter(String center) {
        return productRepository.getTotalValueByFulfillmentCenter(center);
    }
}