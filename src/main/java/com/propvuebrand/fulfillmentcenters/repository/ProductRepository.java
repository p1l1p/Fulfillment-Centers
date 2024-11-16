package com.propvuebrand.fulfillmentcenters.repository;

import com.propvuebrand.fulfillmentcenters.model.Product;
import com.propvuebrand.fulfillmentcenters.model.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStatus(ProductStatus status);

    List<Product> findByProductId(String productId);

    @Query("SELECT SUM(p.value * p.quantity) FROM Product p WHERE p.status = :status")
    Double getTotalSellableValue(@Param("status") ProductStatus status);

    @Query("SELECT SUM(p.value * p.quantity) FROM Product p WHERE p.fulfillmentCenter = :center")
    Double getTotalValueByFulfillmentCenter(@Param("center") String center);
}