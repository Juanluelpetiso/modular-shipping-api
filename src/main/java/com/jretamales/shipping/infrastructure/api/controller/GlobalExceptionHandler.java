package com.jretamales.shipping.infrastructure.api.controller;

import com.jretamales.shipping.domain.exception.ShippingException;
import com.jretamales.shipping.domain.exception.UnsupportedShipmentTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones que traduce errores del dominio
 * a respuestas HTTP apropiadas con cuerpos JSON estandarizados.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Tipo de envío no soportado → 400 Bad Request.
     */
    @ExceptionHandler(UnsupportedShipmentTypeException.class)
    public ResponseEntity<Map<String, Object>> handleUnsupportedShipmentType(
            UnsupportedShipmentTypeException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Errores de lógica de negocio del dominio → 422 Unprocessable Entity.
     */
    @ExceptionHandler(ShippingException.class)
    public ResponseEntity<Map<String, Object>> handleShippingException(
            ShippingException ex) {
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    /**
     * Errores de validación de Jakarta Bean Validation (@Valid) → 400 Bad Request.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Errores de validación");
        body.put("details", fieldErrors);

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * JSON malformado o enum inválido → 400 Bad Request.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleMessageNotReadable(
            HttpMessageNotReadableException ex) {
        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Request body inválido. Verifique el formato JSON y los valores de los campos."
        );
    }

    /**
     * Cualquier error no esperado → 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(
            HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", message);

        return ResponseEntity.status(status).body(body);
    }
}
