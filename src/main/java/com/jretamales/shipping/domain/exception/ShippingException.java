package com.jretamales.shipping.domain.exception;

/**
 * Excepción base del dominio de shipping.
 * Todas las excepciones específicas del dominio heredan de esta clase.
 */
public class ShippingException extends RuntimeException {

    public ShippingException(String message) {
        super(message);
    }

    public ShippingException(String message, Throwable cause) {
        super(message, cause);
    }
}
