package com.zmijewski.ecommerce.controller;

import com.zmijewski.ecommerce.dto.BrandDTO;
import com.zmijewski.ecommerce.enums.BrandSortType;
import com.zmijewski.ecommerce.service.BrandService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/")
@Log4j2
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("brands")
    public ResponseEntity<List<BrandDTO>> getBrands(@RequestParam(name = "sort", defaultValue = "ID_ASC") BrandSortType sortType) {
        log.info("Getting all brands");
        List<BrandDTO> brands = brandService.findAll(sortType);
        return ResponseEntity.ok(brands);
    }
    @GetMapping("brands/{brandId}")
    public ResponseEntity<BrandDTO> getBrandById(@PathVariable(name = "brandId") Long brandId) {
        log.info("Getting brand with id: {}", brandId);
        BrandDTO result = brandService.findById(brandId);
        return ResponseEntity.ok(result);
    }
    @PostMapping("brands")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> saveBrand(@RequestBody @Valid BrandDTO brandDTO) {
        log.info("Saving new brand with name: {}", brandDTO.getName());
        brandService.saveBrand(brandDTO);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("brands/{brandId}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> modifyBrand(@PathVariable(name = "brandId") Long brandId,
                                         @RequestBody @Valid BrandDTO brandDTO) {
        log.info("Modifying brand with id: {}", brandId);
        brandService.modifyBrand(brandId, brandDTO);
        return ResponseEntity.noContent().build();
    }
}
