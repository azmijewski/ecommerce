package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.RoleDTO;
import com.zmijewski.ecommerce.mapper.RoleMapper;
import com.zmijewski.ecommerce.model.entity.Role;
import com.zmijewski.ecommerce.repository.RoleRepository;
import com.zmijewski.ecommerce.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @InjectMocks
    RoleServiceImpl roleService;
    @Mock
    RoleRepository roleRepository;
    @Mock
    RoleMapper roleMapper;

    @Test
    void shouldGetAll() {
        //given
        when(roleRepository.findAll()).thenReturn(Collections.singletonList(new Role()));
        when(roleMapper.mapToRoleDTO(any())).thenReturn(new RoleDTO());
        //when
        List<RoleDTO> result = roleService.getAll();
        //then
        assertFalse(result.isEmpty());
    }

}