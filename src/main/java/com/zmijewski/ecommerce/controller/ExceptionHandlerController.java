package com.zmijewski.ecommerce.controller;

import com.zmijewski.ecommerce.dto.ApiError;
import com.zmijewski.ecommerce.exception.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
@Log4j2
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AddressNotFoundException.class)
    protected ResponseEntity<ApiError> handleAddressNotFound(AddressNotFoundException ex, HttpServletRequest request) {
        ApiError error = handleException(ex, request, HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CustomAuthenticationException.class)
    protected ResponseEntity<ApiError> handleCustomAuthenticationException(CustomAuthenticationException ex, HttpServletRequest request) {
        ApiError error = handleException(ex, request, HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
    @ExceptionHandler(BrandNotFoundException.class)
    protected ResponseEntity<ApiError> handleBrandNotFound(BrandNotFoundException ex, HttpServletRequest request) {
        ApiError error = handleException(ex, request, HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(CartNotFoundException.class)
    protected ResponseEntity<ApiError> handleCartNotFound(CartNotFoundException ex, HttpServletRequest request) {
        ApiError error = handleException(ex, request, HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(NotEnoughProductException.class)
    protected ResponseEntity<ApiError> handleNotEnoughProduct(NotEnoughProductException ex, HttpServletRequest request) {
        ApiError error = handleException(ex, request, HttpStatus.CONFLICT);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    @ExceptionHandler(ProductNotFoundException.class)
    protected ResponseEntity<ApiError> handleProductNotFound(ProductNotFoundException ex, HttpServletRequest request) {
        ApiError error = handleException(ex, request, HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(CategoryNotFoundException.class)
    protected ResponseEntity<ApiError> handleCategoryNotFound(CategoryNotFoundException ex, HttpServletRequest request) {
        ApiError error = handleException(ex, request, HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(EmailAlreadyExistException.class)
    protected ResponseEntity<ApiError> handleEmailAlreadyExist(EmailAlreadyExistException ex, HttpServletRequest request) {
        ApiError error = handleException(ex, request, HttpStatus.CONFLICT);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    @ExceptionHandler(ProductNotInCartException.class)
    protected ResponseEntity<ApiError> handleProductNotInCart(EmailAlreadyExistException ex, HttpServletRequest request) {
        ApiError error = handleException(ex, request, HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(RoleNotFoundException.class)
    protected ResponseEntity<ApiError> handleRoleNotFound(RoleNotFoundException ex, HttpServletRequest request) {
        ApiError error = handleException(ex, request, HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        ApiError error = handleException(ex, request, HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    private <T extends RuntimeException> ApiError handleException(T ex, HttpServletRequest request, HttpStatus httpStatus) {
        log.error(ex);
        return ApiError.builder()
                .message(ex.getMessage())
                .status(httpStatus)
                .timestamp(LocalDateTime.now())
                .path(request.getServletPath())
                .build();
    }

}
