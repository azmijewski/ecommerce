package com.zmijewski.ecommerce.service;


import com.zmijewski.ecommerce.dto.ProductDTO;
import com.zmijewski.ecommerce.dto.ShortProductDTO;
import com.zmijewski.ecommerce.model.enums.ProductSearchCriteria;
import com.zmijewski.ecommerce.model.enums.ProductSortType;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ProductService {
    Page<ShortProductDTO> findAll(int page, int size, ProductSortType sortType);
    Page<ShortProductDTO> findAllByCriteria(int page, int size, ProductSortType sortType, Map<ProductSearchCriteria, String> searchType);
    Page<ShortProductDTO> findAllBySearchWords(int page, int size, ProductSortType sortType, String searchWords);
    ProductDTO findById(Long productId);
    Long saveProduct(ProductDTO productDTO, MultipartFile[] images);
    void updateProduct(Long productId, ProductDTO productDTO);
    void deleteProduct(Long productId);
    void increaseProductQuantity(Integer quantityToIncrease, Long productId);
    void decreaseProductQuantity(Integer quantityToDecrease, Long productId);
    void addImageToProduct(Long productId, MultipartFile image);
}
