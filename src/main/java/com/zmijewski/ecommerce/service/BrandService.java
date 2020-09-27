package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.BrandDTO;
import com.zmijewski.ecommerce.model.enums.BrandSortType;

import java.util.List;

public interface BrandService {
    List<BrandDTO> findAll(BrandSortType brandSortType);
    BrandDTO findById(Long brandId);
    Long saveBrand(BrandDTO brandDTO);
    void modifyBrand(Long brandId, BrandDTO brandDTO);
}
