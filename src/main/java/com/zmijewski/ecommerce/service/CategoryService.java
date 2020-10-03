package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAll();
    CategoryDTO getById(Long categoryId);
    Long saveCategory(CategoryDTO categoryDTO);
    void modifyCategory(Long categoryId, CategoryDTO categoryDTO);
    void deleteCategory(Long categoryId);
}
