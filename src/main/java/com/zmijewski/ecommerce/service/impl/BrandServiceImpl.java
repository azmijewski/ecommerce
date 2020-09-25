package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.dto.BrandDTO;
import com.zmijewski.ecommerce.enums.BrandSortType;
import com.zmijewski.ecommerce.exception.BrandNotFoundException;
import com.zmijewski.ecommerce.mapper.BrandMapper;
import com.zmijewski.ecommerce.model.Brand;
import com.zmijewski.ecommerce.repository.BrandRepository;
import com.zmijewski.ecommerce.service.BrandService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    public BrandServiceImpl(BrandRepository brandRepository,
                            BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }

    @Override
    public List<BrandDTO> findAll(BrandSortType brandSortType) {
        Sort sort = Sort.by(Sort.Direction.fromString(brandSortType.getSortType()), brandSortType.getField());
        return brandRepository.findAll(sort)
                .stream()
                .map(brandMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BrandDTO findById(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new BrandNotFoundException("Could not find brand with id: " + brandId));
        return brandMapper.mapToDTO(brand);
    }

    @Override
    public Long saveBrand(BrandDTO brandDTO) {
        Brand brandToSave = brandMapper.mapToBrand(brandDTO);
        return brandRepository.save(brandToSave)
                .getId();
    }

    @Override
    public void modifyBrand(Long brandId, BrandDTO brandDTO) {
        Brand brandToModify = brandRepository.findById(brandId)
                .orElseThrow(() -> new BrandNotFoundException("Could not find brand with id: " + brandId));
        brandMapper.mapDataToUpdate(brandToModify, brandDTO);
        brandRepository.save(brandToModify);
    }
}
