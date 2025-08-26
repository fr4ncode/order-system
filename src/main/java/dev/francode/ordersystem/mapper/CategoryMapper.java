package dev.francode.ordersystem.mapper;

import dev.francode.ordersystem.dto.category.CategoryRequest;
import dev.francode.ordersystem.dto.category.CategoryResponse;
import dev.francode.ordersystem.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryRequest request);

    CategoryResponse toResponse(Category category);
}
