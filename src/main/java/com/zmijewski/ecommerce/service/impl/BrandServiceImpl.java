package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.dto.AuditDTO;
import com.zmijewski.ecommerce.dto.BrandDTO;
import com.zmijewski.ecommerce.exception.BrandNotFoundException;
import com.zmijewski.ecommerce.mapper.BrandMapper;
import com.zmijewski.ecommerce.model.entity.Brand;
import com.zmijewski.ecommerce.model.enums.BrandSortType;
import com.zmijewski.ecommerce.repository.BrandRepository;
import com.zmijewski.ecommerce.service.BrandService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {


    private static final String BRAND_ADDED_MESSAGE = "Added new brand: ";
    private static final String BRAND_MODIFY_MESSAGE = "Modified brand: ";

    private static final String AUDIT_QUEUE = "auditQueue";

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final RabbitTemplate rabbitTemplate;

    public BrandServiceImpl(BrandRepository brandRepository,
                            BrandMapper brandMapper, RabbitTemplate rabbitTemplate) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
        this.rabbitTemplate = rabbitTemplate;
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
        Brand savedBrand = brandRepository.save(brandToSave);
        AuditDTO auditDTO = new AuditDTO(BRAND_ADDED_MESSAGE + savedBrand);
        rabbitTemplate.convertAndSend(AUDIT_QUEUE, auditDTO);
        return savedBrand.getId();
    }

    @Override
    public void modifyBrand(Long brandId, BrandDTO brandDTO) {
        Brand brandToModify = brandRepository.findById(brandId)
                .orElseThrow(() -> new BrandNotFoundException("Could not find brand with id: " + brandId));
        brandMapper.mapDataToUpdate(brandToModify, brandDTO);
        Brand modifiedBrand = brandRepository.save(brandToModify);
        AuditDTO auditDTO = new AuditDTO(BRAND_MODIFY_MESSAGE + modifiedBrand);
        rabbitTemplate.convertAndSend(AUDIT_QUEUE, auditDTO);
    }
}
