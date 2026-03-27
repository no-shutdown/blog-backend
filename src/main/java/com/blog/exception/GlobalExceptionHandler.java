package com.blog.exception;

import com.blog.dto.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleValidation(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String msg = fieldError != null ? fieldError.getDefaultMessage() : "Bad request";
        return ApiResponse.error(400, msg);
    }

    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class, ConstraintViolationException.class})
    public ApiResponse<Void> handleBadRequest(Exception ex) {
        return ApiResponse.error(400, ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ApiResponse<Void> handleNotFound(EntityNotFoundException ex) {
        return ApiResponse.error(404, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleServerError(Exception ex) {
        return ApiResponse.error(500, "Internal server error");
    }
}
