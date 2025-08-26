package dev.francode.ordersystem.service.impl;

import dev.francode.ordersystem.dto.product.ProductFilter;
import dev.francode.ordersystem.dto.product.ProductRequest;
import dev.francode.ordersystem.dto.product.ProductResponse;
import dev.francode.ordersystem.entity.Category;
import dev.francode.ordersystem.entity.Product;
import dev.francode.ordersystem.exceptions.custom.ValidationException;
import dev.francode.ordersystem.mapper.ProductMapper;
import dev.francode.ordersystem.repository.CategoryRepository;
import dev.francode.ordersystem.repository.ProductRepository;
import dev.francode.ordersystem.service.interfaces.ProductService;
import dev.francode.ordersystem.service.spec.ProductSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ValidationException("Categoría no encontrada"));

        Product product = productMapper.toEntity(request);
        product.setCategory(category);

        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long productId, ProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ValidationException("Producto no encontrado"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ValidationException("Categoría no encontrada"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setPriceDiscount(request.getPriceDiscount());
        product.setStock(request.getStock());
        product.setBrandName(request.getBrandName());
        product.setCategory(category);

        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ValidationException("Producto no encontrado"));
        productRepository.delete(product);
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ValidationException("Producto no encontrado"));
        return productMapper.toResponse(product);
    }

    @Override
    public Page<ProductResponse> getProducts(ProductFilter filter, Pageable pageable) {
        Specification<Product> spec = ProductSpecifications.filterBy(filter);
        return productRepository.findAll(spec, pageable)
                .map(productMapper::toResponse);
    }
}