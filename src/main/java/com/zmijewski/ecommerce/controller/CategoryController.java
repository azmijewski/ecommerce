package com.zmijewski.ecommerce.controller;

import com.zmijewski.ecommerce.dto.CategoryDTO;
import com.zmijewski.ecommerce.service.CategoryService;
import com.zmijewski.ecommerce.util.ResponseUriBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/")
@Log4j2
public class CategoryController {
    private final CategoryService categoryService;
    private final ResponseUriBuilder uriBuilder;

    public CategoryController(CategoryService categoryService, ResponseUriBuilder uriBuilder) {
        this.categoryService = categoryService;
        this.uriBuilder = uriBuilder;
    }

    @GetMapping("categories")
    public ResponseEntity<List<CategoryDTO>> getAll() {
        log.info("Getting all categories");
        List<CategoryDTO> result = categoryService.getAll();
        return ResponseEntity.ok(result);
    }
    @GetMapping("categories/{categoryId}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable(name = "categoryId") Long categoryId) {
        log.info("Getting category with id: {}", categoryId);
        CategoryDTO result = categoryService.getById(categoryId);
        return ResponseEntity.ok(result);
    }
    @PostMapping("categories")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> saveCategory(@RequestPart(name = "data") @Valid CategoryDTO categoryDTO,
                                         @RequestPart(name = "file") MultipartFile file) {
        log.info("Trying to save category with name: {}", categoryDTO.getName());
        Long result = categoryService.saveCategory(categoryDTO, file);
        log.info("Category saved successfully");
        URI location = uriBuilder.buildUriWithAppendedId(result);
        return ResponseEntity.created(location).build();
    }
    @PutMapping("categories/{categoryId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> modifyCategory(@PathVariable(name = "categoryId") Long categoryId,
                                           @RequestBody @Valid CategoryDTO categoryDTO) {
        log.info("Trying to modify category with id: {}", categoryId);
        categoryService.modifyCategory(categoryId, categoryDTO);
        log.info("Category modified successfully");
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("categories/{categoryId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteCategory(@PathVariable(name = "categoryId") Long categoryId) {
        log.info("Trying to delete category with id: {}", categoryId);
        categoryService.deleteCategory(categoryId);
        log.info("Category deleted successfully");
        return ResponseEntity.noContent().build();
    }
}
