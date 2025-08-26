package dev.francode.ordersystem.service.interfaces;

import dev.francode.ordersystem.dto.category.CategoryRequest;
import dev.francode.ordersystem.dto.category.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(Long categoryId, CategoryRequest request);

    void deleteCategory(Long categoryId);

    CategoryResponse getCategoryById(Long categoryId);

    Page<CategoryResponse> getAllCategories(Pageable pageable);
}
