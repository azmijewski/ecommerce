package com.zmijewski.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserWithRoleDTO {
    @Valid
    private UserDTO user;
    @NotNull
    private Long roleId;
    private String roleName;
}
