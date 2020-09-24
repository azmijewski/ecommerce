package com.zmijewski.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserWithRoleDTO {
    private UserDTO user;
    private Long roleId;
    private String roleName;
}
