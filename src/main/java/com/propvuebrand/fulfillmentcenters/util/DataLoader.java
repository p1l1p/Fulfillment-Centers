package com.propvuebrand.fulfillmentcenters.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.propvuebrand.fulfillmentcenters.model.Product;
import com.propvuebrand.fulfillmentcenters.model.ProductStatus;
import com.propvuebrand.fulfillmentcenters.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductService productService;

    @Autowired
    public DataLoader(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/data.csv"))))) {

            // Пропускаем заголовок
            reader.readNext();

            // Читаем данные
            String[] line;
            while ((line = reader.readNext()) != null) {
                Product product = new Product();
                product.setProductId(line[0]);
                product.setStatus(ProductStatus.valueOf(line[1].toUpperCase()));
                product.setFulfillmentCenter(line[2]);
                product.setQuantity(Integer.parseInt(line[3]));
                product.setValue(Double.parseDouble(line[4]));

                productService.createProduct(product);
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }
}