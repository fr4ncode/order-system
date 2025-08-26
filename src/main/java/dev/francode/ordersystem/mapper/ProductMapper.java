package dev.francode.ordersystem.mapper;

import dev.francode.ordersystem.dto.product.ProductRequest;
import dev.francode.ordersystem.dto.product.ProductResponse;
import dev.francode.ordersystem.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequest request);

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(target = "storeName", constant = "Hotel Formula 1")
    ProductResponse toResponse(Product product);

}