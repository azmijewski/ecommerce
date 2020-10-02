package com.zmijewski.ecommerce.controller;

import com.zmijewski.ecommerce.dto.RoleDTO;
import com.zmijewski.ecommerce.service.RoleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
@Log4j2
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    @GetMapping("roles")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<List<RoleDTO>> getAll() {
        log.info("Getting roles");
        List<RoleDTO> result = roleService.getAll();
        return ResponseEntity.ok(result);
    }
}
