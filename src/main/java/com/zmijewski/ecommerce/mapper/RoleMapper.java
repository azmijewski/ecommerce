package com.zmijewski.ecommerce.mapper;

import com.zmijewski.ecommerce.dto.RoleDTO;
import com.zmijewski.ecommerce.model.entity.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO mapToRoleDTO(Role role);
}
