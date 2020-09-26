package com.zmijewski.ecommerce.controller;

import com.zmijewski.ecommerce.dto.AddressDTO;
import com.zmijewski.ecommerce.service.AddressService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/v1/")
@Log4j2
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }
    @GetMapping("addresses")
    @Secured({"ROLE_USER"})
    public ResponseEntity<List<AddressDTO>> getAddressesForUser(Principal principal) {
        log.info("Getting addresses for user: {}", principal.getName());
        List<AddressDTO> result = addressService.getAddressesForUser(principal.getName());
        return ResponseEntity.ok(result);
    }
    @PostMapping("addresses")
    @Secured({"ROLE_USER"})
    public ResponseEntity<?> addAddressForUser(Principal principal,
                                               @RequestBody @Valid AddressDTO addressDTO) {
        log.info("Adding address for user: {}", principal.getName());
        addressService.addAddressForUser(principal.getName(), addressDTO);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("addresses/{addressId}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<?> deleteAddress(@PathVariable(name = "addressId") Long addressId) {
        log.info("Deleting address with id: {}", addressId);
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("addresses/{addressId}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<?> modifyAddress(@PathVariable(name = "addressId") Long addressId,
                                               @RequestBody @Valid AddressDTO addressDTO) {
        log.info("Modifying address with id: {}", addressId);
        addressService.modifyAddress(addressId, addressDTO);
        return ResponseEntity.noContent().build();
    }


}
