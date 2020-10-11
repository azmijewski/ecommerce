package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.dto.AuditDTO;
import com.zmijewski.ecommerce.dto.CategoryDTO;
import com.zmijewski.ecommerce.exception.CategoryCannotBeRemovedException;
import com.zmijewski.ecommerce.exception.CategoryNameAlreadyExistException;
import com.zmijewski.ecommerce.exception.CategoryNotFoundException;
import com.zmijewski.ecommerce.mapper.CategoryMapper;
import com.zmijewski.ecommerce.model.entity.Category;
import com.zmijewski.ecommerce.model.entity.Image;
import com.zmijewski.ecommerce.repository.CategoryRepository;
import com.zmijewski.ecommerce.repository.ImageRepository;
import com.zmijewski.ecommerce.service.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CategoryServiceImpl implements CategoryService {

    private static final String CATEGORY_ADDED_MESSAGE = "Added new category: ";
    private static final String CATEGORY_MODIFY_MESSAGE = "Modified category: ";
    private static final String CATEGORY_DELETE_MESSAGE = "Delete category: ";

    private static final String AUDIT_QUEUE = "auditQueue";

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ImageRepository imageRepository;
    private final RabbitTemplate rabbitTemplate;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryMapper categoryMapper,
                               ImageRepository imageRepository,
                               RabbitTemplate rabbitTemplate) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.imageRepository = imageRepository;
        this.rabbitTemplate = rabbitTemplate;
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
    @Transactional
    public Long saveCategory(CategoryDTO categoryDTO, MultipartFile image) {
        Category categoryToSave = categoryMapper.mapToCategory(categoryDTO);
        Image imageToSave = new Image();
        imageToSave.setName(image.getOriginalFilename());
        try {
            imageToSave.setPhoto(image.getBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not add category without photo");
        }
        Image savedImage = imageRepository.save(imageToSave);
        categoryToSave.setImage(savedImage);
        return saveCategoryAndSendAuditLog(categoryToSave, CATEGORY_ADDED_MESSAGE).getId();

    }

    @Override
    public void modifyCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category categoryToModify = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Could not find category with id: " + categoryId));
        categoryMapper.mapDataToUpdate(categoryToModify, categoryDTO);
        saveCategoryAndSendAuditLog(categoryToModify, CATEGORY_MODIFY_MESSAGE);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if (categoryRepository.hasAnyNoProduct(categoryId)) {
            throw new CategoryCannotBeRemovedException("Category cannot be deleted in due tu products");
        }
        Category categoryToDelete = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Could not find category with id: " + categoryId));
        categoryRepository.delete(categoryToDelete);
        AuditDTO auditDTO = new AuditDTO(CATEGORY_DELETE_MESSAGE + categoryToDelete);
        rabbitTemplate.convertAndSend(AUDIT_QUEUE, auditDTO);
    }

    private Category saveCategoryAndSendAuditLog(Category categoryToSave, String auditMessage) {
        Category savedCategory;
        try {
            savedCategory =  categoryRepository.save(categoryToSave);
        } catch (ConstraintViolationException e) {
            throw new CategoryNameAlreadyExistException("Category with name: " + categoryToSave.getName() + " already exist");
        }
        AuditDTO auditDTO = new AuditDTO(auditMessage + savedCategory);
        rabbitTemplate.convertAndSend(AUDIT_QUEUE, auditDTO);
        return savedCategory;
    }
}
