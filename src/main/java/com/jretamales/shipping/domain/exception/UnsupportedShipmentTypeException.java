package com.jretamales.shipping.domain.exception;

import com.jretamales.shipping.domain.model.ShipmentType;

/**
 * Se lanza cuando se recibe un tipo de envío no soportado o no registrado
 * en el Strategy Resolver.
 */
public class UnsupportedShipmentTypeException extends ShippingException {

    public UnsupportedShipmentTypeException(ShipmentType type) {
        super("Tipo de envío no soportado: " + type);
    }

    public UnsupportedShipmentTypeException(String typeName) {
        super("Tipo de envío no reconocido: " + typeName);
    }
}
