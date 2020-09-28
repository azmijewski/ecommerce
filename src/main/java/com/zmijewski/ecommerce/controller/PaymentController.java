package com.zmijewski.ecommerce.controller;

import com.zmijewski.ecommerce.dto.PaymentDTO;
import com.zmijewski.ecommerce.model.enums.PaymentType;
import com.zmijewski.ecommerce.service.PaymentService;
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
public class PaymentController {
    private final PaymentService paymentService;
    private final ResponseUriBuilder uriBuilder;

    public PaymentController(PaymentService paymentService, ResponseUriBuilder uriBuilder) {
        this.paymentService = paymentService;
        this.uriBuilder = uriBuilder;
    }

    @GetMapping("payments")
    public ResponseEntity<List<PaymentDTO>> getAll() {
        log.info("Getting all payments");
        List<PaymentDTO> result = paymentService.getAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("payments/activeByType")
    public ResponseEntity<List<PaymentDTO>> getAllActiveByType(@RequestParam(name = "type") PaymentType type) {
        log.info("Getting all active payments by type: {}", type);
        List<PaymentDTO> result = paymentService.getAllActiveByType(type);
        return ResponseEntity.ok(result);
    }
    @GetMapping("payments/{paymentId}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable(name = "paymentId") Long paymentId) {
        log.info("Getting payment with id: {}", paymentId);
        PaymentDTO result = paymentService.getById(paymentId);
        return ResponseEntity.ok(result);
    }
    @PostMapping("payments")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> savePayment(@RequestBody @Valid PaymentDTO paymentDTO) {
        log.info("Trying to save new payment with name: {}, type: {}", paymentDTO.getName(), paymentDTO.getPaymentType());
        Long result = paymentService.savePayment(paymentDTO);
        URI location = uriBuilder.buildUriWithAppendedId(result);
        return ResponseEntity.created(location).build();
    }
    @PutMapping("payments/{paymentId}/enabling")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> enablePayment(@PathVariable(name = "paymentId") Long paymentId) {
        log.info("Trying to enable payment with id: {}", paymentId);
        paymentService.enablePayment(paymentId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("payments/{paymentId}/disabling")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> disablePayment(@PathVariable(name = "paymentId") Long paymentId) {
        log.info("Trying to disable payment with id: {}", paymentId);
        paymentService.disablePayment(paymentId);
        return ResponseEntity.noContent().build();
    }
}
