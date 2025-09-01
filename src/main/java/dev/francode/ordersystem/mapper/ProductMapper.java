package dev.francode.ordersystem.mapper;

import dev.francode.ordersystem.dto.product.ProductRequest;
import dev.francode.ordersystem.dto.product.ProductResponse;
import dev.francode.ordersystem.entity.Image;
import dev.francode.ordersystem.entity.Product;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequest request);

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(target = "storeName", constant = "Hotel_Formula_1")
    @Mapping(source = "images", target = "images", qualifiedByName = "mapImageUrls")
    ProductResponse toResponse(Product product);

    @Named("mapImageUrls")
    default List<String> mapImageUrls(List<Image> images) {
        if (images == null) return null;
        return images.stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());
    }
}
