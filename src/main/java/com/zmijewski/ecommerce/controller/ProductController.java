package com.zmijewski.ecommerce.controller;

import com.zmijewski.ecommerce.dto.ProductDTO;
import com.zmijewski.ecommerce.dto.SearchDTO;
import com.zmijewski.ecommerce.dto.ShortProductDTO;
import com.zmijewski.ecommerce.enums.ProductSearchCriteria;
import com.zmijewski.ecommerce.enums.ProductSortType;
import com.zmijewski.ecommerce.service.ProductService;
import com.zmijewski.ecommerce.util.ResponseUriBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("api/v1/")
@Log4j2
public class ProductController {
    private final ProductService productService;
    private final ResponseUriBuilder uriBuilder;

    public ProductController(ProductService productService, ResponseUriBuilder uriBuilder) {
        this.productService = productService;
        this.uriBuilder = uriBuilder;
    }

    @GetMapping("products")
    public ResponseEntity<Page<ShortProductDTO>> findAll(@RequestParam(name = "page") @Min(0) int page,
                                                         @RequestParam(name = "size") @Min(1) int size,
                                                         @RequestParam(name = "sort", defaultValue = "ID_ASC") ProductSortType sortType) {
        log.info("Getting products page: {}, size: {}", page, size);
        Page<ShortProductDTO> result = productService.findAll(page, size, sortType);
        return ResponseEntity.ok(result);
    }

    @GetMapping("productsByCriteria")
    public ResponseEntity<Page<ShortProductDTO>> findAllByCriteria(@RequestParam(name = "page") @Min(0) int page,
                                                                   @RequestParam(name = "size") @Min(1) int size,
                                                                   @RequestParam(name = "sort", defaultValue = "ID_ASC") ProductSortType sortType,
                                                                   @RequestParam Map<ProductSearchCriteria, String> criteria) {
        log.info("Getting products page: {}, size: {} by criteria: {}", page, size, criteria);
        Page<ShortProductDTO> result = productService.findAllByCriteria(page, size, sortType, criteria);
        return ResponseEntity.ok(result);
    }

    @PostMapping("productsBySearch")
    public ResponseEntity<Page<ShortProductDTO>> searchProducts(@RequestParam(name = "page") @Min(0) int page,
                                                                @RequestParam(name = "size") @Min(1) int size,
                                                                @RequestParam(name = "sort", defaultValue = "ID_ASC") ProductSortType sortType,
                                                                @RequestBody @Valid SearchDTO searchDTO) {
        log.info("Looking for products page: {}, size: {} by words: {}", page, size, searchDTO.getSearchWords());
        Page<ShortProductDTO> result = productService.findAllBySearchWords(page, size, sortType, searchDTO.getSearchWords());
        return ResponseEntity.ok(result);
    }
    @GetMapping("products/{productId}")
    public ResponseEntity<ProductDTO> findProductById(@PathVariable(name = "productId") Long productId) {
        log.info("Getting product with id: {}", productId);
        ProductDTO result = productService.findById(productId);
        return ResponseEntity.ok(result);
    }
    @PostMapping("products")
    @Secured({"ADMIN"})
    public ResponseEntity<?> saveProduct(@RequestPart(name = "data") @Valid ProductDTO productDTO,
                                         @RequestPart(name = "files")MultipartFile[] files) {
        log.info("Trying to save new Product with name: {}", productDTO.getName());
        Long result = productService.saveProduct(productDTO, files);
        URI location = uriBuilder.buildUriWithAppendedId(result);
        return ResponseEntity.created(location).build();
    }
    @PutMapping("products/{productId}")
    @Secured({"ADMIN"})
    public ResponseEntity<?> modifyProduct(@PathVariable(name = "productId") Long productId,
                                           @RequestBody @Valid ProductDTO productDTO) {
        log.info("Modifying product with id: {}", productId);
        productService.updateProduct(productId, productDTO);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("products/{productId}")
    @Secured({"ADMIN"})
    public ResponseEntity<?> deleteProduct(@PathVariable(name = "productId") Long productId) {
        log.info("Trying to delete product with id: {}", productId);
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("products/increasingQuantity")
    @Secured({"ADMIN"})
    public ResponseEntity<?> increaseProductQuantity(@RequestParam(name = "productId") Long productId,
                                                     @RequestParam(name = "quantity") @Min(1) Integer quantity) {
        log.info("Trying to increase product with id: {}, quantity by {}", productId, quantity);
        productService.increaseProductQuantity(quantity, productId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("products/decreasingQuantity")
    @Secured({"ADMIN"})
    public ResponseEntity<?> decreaseProductQuantity(@RequestParam(name = "productId") Long productId,
                                                     @RequestParam(name = "quantity") @Min(1) Integer quantity) {
        log.info("Trying to increase product with id: {}, quantity by {}", productId, quantity);
        productService.decreaseProductQuantity(quantity, productId);
        return ResponseEntity.noContent().build();
    }

}
