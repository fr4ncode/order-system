package dev.francode.ordersystem.service.impl;

import dev.francode.ordersystem.dto.category.CategoryRequest;
import dev.francode.ordersystem.dto.category.CategoryResponse;
import dev.francode.ordersystem.entity.Category;
import dev.francode.ordersystem.exceptions.custom.ValidationException;
import dev.francode.ordersystem.mapper.CategoryMapper;
import dev.francode.ordersystem.repository.CategoryRepository;
import dev.francode.ordersystem.service.interfaces.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ValidationException("Categoría no encontrada"));

        category.setName(request.getName());

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ValidationException("Categoría no encontrada"));
        categoryRepository.delete(category);
    }

    @Override
    public CategoryResponse getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ValidationException("Categoría no encontrada"));
        return categoryMapper.toResponse(category);
    }

    @Override
    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toResponse);
    }
}