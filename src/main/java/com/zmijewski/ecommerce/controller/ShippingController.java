package com.zmijewski.ecommerce.controller;

import com.zmijewski.ecommerce.dto.ShippingDTO;
import com.zmijewski.ecommerce.model.enums.PaymentType;
import com.zmijewski.ecommerce.service.ShippingService;
import com.zmijewski.ecommerce.util.ResponseUriBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/")
@Log4j2
public class ShippingController {
    private final ShippingService shippingService;
    private final ResponseUriBuilder uriBuilder;

    public ShippingController(ShippingService shippingService, ResponseUriBuilder uriBuilder) {
        this.shippingService = shippingService;
        this.uriBuilder = uriBuilder;
    }

    @GetMapping("shipments")
    public ResponseEntity<List<ShippingDTO>> getAll() {
        log.info("Getting all shipments");
        List<ShippingDTO> result = shippingService.getAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("shipments/activeByType")
    public ResponseEntity<List<ShippingDTO>> getAllActiveByType(@RequestParam(name = "type") PaymentType type) {
        log.info("Getting all active shipments by type: {}", type);
        List<ShippingDTO> result = shippingService.getAllActiveByType(type);
        return ResponseEntity.ok(result);
    }
    @GetMapping("shipments/{shippingId}")
    public ResponseEntity<ShippingDTO> getShippingById(@PathVariable(name = "shippingId") Long shippingId) {
        log.info("Getting shipment with id: {}", shippingId);
        ShippingDTO result = shippingService.getById(shippingId);
        return ResponseEntity.ok(result);
    }
    @PostMapping("shipments")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> saveShipping(@RequestBody @Valid ShippingDTO shippingDTO) {
        log.info("Trying to save new shipment with name: {}, type: {}", shippingDTO.getName(), shippingDTO.getPaymentType());
        Long result = shippingService.saveShipping(shippingDTO);
        URI location = uriBuilder.buildUriWithAppendedId(result);
        return ResponseEntity.created(location).build();
    }
    @PutMapping("shipments/{shippingId}/enabling")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> enableShipping(@PathVariable(name = "shippingId") Long shippingId) {
        log.info("Trying to enable shipment with id: {}", shippingId);
        shippingService.enableShipping(shippingId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("shipments/{shippingId}/disabling")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> disableShipping(@PathVariable(name = "shippingId") Long shippingId) {
        log.info("Trying to disable shipment with id: {}", shippingId);
        shippingService.disableShipping(shippingId);
        return ResponseEntity.noContent().build();
    }
}
