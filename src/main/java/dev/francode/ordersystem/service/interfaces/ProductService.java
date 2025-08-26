package dev.francode.ordersystem.service.interfaces;

import dev.francode.ordersystem.dto.product.ProductFilter;
import dev.francode.ordersystem.dto.product.ProductRequest;
import dev.francode.ordersystem.dto.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(Long productId, ProductRequest request);

    void deleteProduct(Long productId);

    ProductResponse getProductById(Long productId);

    Page<ProductResponse> getProducts(ProductFilter filter, Pageable pageable);
}