package com.see.visal.account_service.rest.exception;

import com.see.visal.account_service.application.interceptor.AccountTypeValidationInterceptor.AccountTypeNotFoundException;
import com.see.visal.account_service.application.interceptor.AccountTypeValidationInterceptor.AccountTypeNotActiveException;
import com.see.visal.account_service.application.interceptor.AccountTypeValidationInterceptor.InsufficientInitialBalanceException;
import com.see.visal.account_service.application.interceptor.AccountTypeValidationInterceptor.InvalidInitialBalanceException;
import com.see.visal.account_service.application.interceptor.BranchValidationInterceptor.BranchNotFoundException;
import com.see.visal.account_service.application.interceptor.BranchValidationInterceptor.BranchNotActiveException;
import com.see.visal.account_service.application.interceptor.CustomerValidationInterceptor.CustomerNotFoundException;
import com.see.visal.account_service.application.interceptor.CustomerValidationInterceptor.CustomerServiceUnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class AccountExceptionHandler {

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ErrorResponse> handleAccountException(
            AccountException ex, HttpServletRequest request) {
        log.error("AccountException [{}]: {}", ex.getStatus(), ex.getMessage());
        return build(ex.getStatus(), ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFound(
            CustomerNotFoundException ex, HttpServletRequest request) {
        log.error("CustomerNotFoundException: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(CustomerServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleCustomerServiceUnavailable(
            CustomerServiceUnavailableException ex, HttpServletRequest request) {
        log.error("CustomerServiceUnavailableException: {}", ex.getMessage());
        return build(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(BranchNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBranchNotFound(
            BranchNotFoundException ex, HttpServletRequest request) {
        log.error("BranchNotFoundException: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(BranchNotActiveException.class)
    public ResponseEntity<ErrorResponse> handleBranchNotActive(
            BranchNotActiveException ex, HttpServletRequest request) {
        log.error("BranchNotActiveException: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(AccountTypeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountTypeNotFound(
            AccountTypeNotFoundException ex, HttpServletRequest request) {
        log.error("AccountTypeNotFoundException: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(AccountTypeNotActiveException.class)
    public ResponseEntity<ErrorResponse> handleAccountTypeNotActive(
            AccountTypeNotActiveException ex, HttpServletRequest request) {
        log.error("AccountTypeNotActiveException: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler({InsufficientInitialBalanceException.class, InvalidInitialBalanceException.class})
    public ResponseEntity<ErrorResponse> handleInitialBalanceException(
            RuntimeException ex, HttpServletRequest request) {
        log.error("InitialBalanceException: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(
            ResponseStatusException ex, HttpServletRequest request) {
        log.error("ResponseStatusException [{}]: {}", ex.getStatusCode(), ex.getReason());
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) status = HttpStatus.INTERNAL_SERVER_ERROR;
        String msg = ex.getReason() != null ? ex.getReason() : ex.getMessage();
        return build(status, msg, request.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(fe -> new ErrorResponse.FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();
        log.warn("Validation failed: {}", fieldErrors);
        return build(HttpStatus.BAD_REQUEST, "Validation failed", request.getRequestURI(), fieldErrors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {
        log.error("IllegalArgumentException: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(
            Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred", request.getRequestURI(), null);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message,
                                                 String path,
                                                 List<ErrorResponse.FieldError> fieldErrors) {
        return ResponseEntity.status(status).body(new ErrorResponse(
                status.value(), status.getReasonPhrase(), message, path,
                ZonedDateTime.now(), fieldErrors));
    }
}

