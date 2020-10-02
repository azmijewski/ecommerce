package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.dto.RoleDTO;
import com.zmijewski.ecommerce.mapper.RoleMapper;
import com.zmijewski.ecommerce.repository.RoleRepository;
import com.zmijewski.ecommerce.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository,
                           RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public List<RoleDTO> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::mapToRoleDTO)
                .collect(Collectors.toList());
    }
}
