package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.dto.CategoryDTO;
import com.zmijewski.ecommerce.exception.CategoryCannotBeRemovedException;
import com.zmijewski.ecommerce.exception.CategoryNameAlreadyExistException;
import com.zmijewski.ecommerce.exception.CategoryNotFoundException;
import com.zmijewski.ecommerce.mapper.CategoryMapper;
import com.zmijewski.ecommerce.model.entity.Category;
import com.zmijewski.ecommerce.repository.CategoryRepository;
import com.zmijewski.ecommerce.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryDTO> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Could not find category with id: " + categoryId));
        return categoryMapper.mapToDTO(category);
    }

    @Override
    public Long saveCategory(CategoryDTO categoryDTO) {
        Category categoryToSave = categoryMapper.mapToCategory(categoryDTO);
        try {
            return categoryRepository.save(categoryToSave)
                    .getId();
        } catch (ConstraintViolationException e) {
            throw new CategoryNameAlreadyExistException("Category with name: " + categoryDTO.getName() + " already exist");
        }
    }

    @Override
    public void modifyCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category categoryToModify = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Could not find category with id: " + categoryId));
        categoryMapper.mapDataToUpdate(categoryToModify, categoryDTO);
        try {
             categoryRepository.save(categoryToModify);
        } catch (ConstraintViolationException e) {
            throw new CategoryNameAlreadyExistException("Category with name: " + categoryDTO.getName() + " already exist");
        }
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if (categoryRepository.hasAnyNoProduct(categoryId)) {
            throw new CategoryCannotBeRemovedException("Category cannot be deleted in due tu products");
        }
        Category categoryToDelete = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Could not find category with id: " + categoryId));
        categoryRepository.delete(categoryToDelete);
    }
}
