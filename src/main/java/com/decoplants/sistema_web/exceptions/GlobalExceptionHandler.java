package com.decoplants.sistema_web.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. CAPTURAR ERRORES DE VALIDACIÓN EN LA API REST (Ej: @Valid en Producto)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                "Validación Fallida",
                "Los datos enviados no cumplen con los requisitos mínimos.",
                request.getRequestURI()
        );

        // Mapear qué campo falló y qué mensaje de @NotBlank o @NotNull se activó
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        errorResponse.setValidations(errors);
        return new ResponseEntity<>(errorResponse, status);
    }

    // 2. MANEJO GENÉRICO PARA CUALQUIER OTRA EXCEPCIÓN DEL SISTEMA
    @ExceptionHandler(Exception.class)
    public Object handleAllExceptions(Exception ex, HttpServletRequest request) {
        
        // Si la petición va dirigida a la API REST, respondemos con JSON
        if (request.getRequestURI().startsWith("/api")) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            ErrorResponse errorResponse = new ErrorResponse(
                    status.value(),
                    "Error Interno del Servidor",
                    ex.getMessage(),
                    request.getRequestURI()
            );
            return new ResponseEntity<>(errorResponse, status);
        }

        // Si la petición viene de la interfaz web, redirigimos a una vista amigable de Thymeleaf
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMsg", "Ha ocurrido un error inesperado en nuestra plataforma.");
        modelAndView.addObject("detalle", ex.getMessage());
        modelAndView.setViewName("error-sistema"); // Nombre del archivo HTML
        return modelAndView;
    }
}