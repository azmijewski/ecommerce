package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.dto.ProductDTO;
import com.zmijewski.ecommerce.dto.ShortProductDTO;
import com.zmijewski.ecommerce.enums.ProductSearchCriteria;
import com.zmijewski.ecommerce.enums.ProductSortType;
import com.zmijewski.ecommerce.exception.BrandNotFoundException;
import com.zmijewski.ecommerce.exception.CategoryNotFoundException;
import com.zmijewski.ecommerce.exception.NotEnoughProductException;
import com.zmijewski.ecommerce.exception.ProductNotFoundException;
import com.zmijewski.ecommerce.mapper.ProductMapper;
import com.zmijewski.ecommerce.model.Brand;
import com.zmijewski.ecommerce.model.Category;
import com.zmijewski.ecommerce.model.Image;
import com.zmijewski.ecommerce.model.Product;
import com.zmijewski.ecommerce.repository.*;
import com.zmijewski.ecommerce.service.ProductService;
import com.zmijewski.ecommerce.specification.ProductSearchSpecification;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductSearchRepository productSearchRepository;
    private final ProductMapper productMapper;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProductSearchRepository productSearchRepository,
                              ProductMapper productMapper, ImageRepository imageRepository,
                              CategoryRepository categoryRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.productSearchRepository = productSearchRepository;
        this.productMapper = productMapper;
        this.imageRepository = imageRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
    }

    @Override
    public Page<ShortProductDTO> findAll(int page, int size, ProductSortType sortType) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortType.getSortType()), sortType.getField());
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAllAvailable(pageable)
                .map(productMapper::mapToShortProductDTO);
    }

    @Override
    public Page<ShortProductDTO> findAllByCriteria(int page, int size, ProductSortType sortType, Map<ProductSearchCriteria, String> searchType) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortType.getSortType()), sortType.getField());
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<Product> specification = new ProductSearchSpecification(searchType);
        return productRepository.findAll(specification, pageable)
                .map(productMapper::mapToShortProductDTO);
    }

    @Override
    public Page<ShortProductDTO> findAllBySearchWords(int page, int size, ProductSortType sortType, String searchWords) {
        return productSearchRepository.searchForProduct(searchWords, page, size, sortType)
                .map(productMapper::mapToShortProductDTO);
    }

    @Override
    public ProductDTO findById(Long productId) {
        return productRepository.findById(productId)
                .map(productMapper::mapToDTO)
                .orElseThrow(() -> new ProductNotFoundException("Could not find product with id: " + productId));
    }

    @Override
    @Transactional
    public Long saveProduct(ProductDTO productDTO, MultipartFile[] images) {
        Brand brand = brandRepository.findById(productDTO.getBrandId())
                .orElseThrow(() -> new BrandNotFoundException("Could not find brand with id: " + productDTO.getBrandId()));
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Could not find category with id: " + productDTO.getCategoryId()));
        Product productToSave = productMapper.mapToProduct(productDTO);
        productToSave.setBrand(brand);
        productToSave.setCategory(category);
        Product savedProduct = productRepository.save(productToSave);
        Arrays.stream(images).forEach(image -> {
            Image imageToSave = new Image();
            imageToSave.setName(image.getOriginalFilename());
            try {
                imageToSave.setPhoto(image.getBytes());
            } catch (IOException e) {
                log.error(e);
            }
            imageToSave.setProduct(savedProduct);
            imageRepository.save(imageToSave);
        });
        return savedProduct.getId();
    }

    @Override
    public void updateProduct(Long productId, ProductDTO productDTO) {
        Product productToUpdate = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Could not find product with id: " + productId));
        productMapper.mapProductToUpdate(productToUpdate, productDTO);
        productRepository.save(productToUpdate);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product productToDelete = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Could not find product with id: " + productId));
        productRepository.delete(productToDelete);
    }

    @Override
    public void increaseProductQuantity(Integer quantityToIncrease, Long productId) {
        productRepository.increaseProductQuantity(productId, quantityToIncrease);
    }

    @Override
    public void decreaseProductQuantity(Integer quantityToDecrease, Long productId) {
        try {
            productRepository.decreaseProductQuantity(productId, quantityToDecrease);
        } catch (ConstraintViolationException e) {
            log.error(e);
            throw new NotEnoughProductException("Product with id: " + productId + " has not enough quantity to decrease");
        }

    }
}
