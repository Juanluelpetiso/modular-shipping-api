package com.jretamales.shipping.domain.model;

/**
 * Tipos de envío soportados por el sistema.
 * Origen: shipping-spec.md § 5 — campo "shipmentType".
 */
public enum ShipmentType {
    NATIONAL,
    INTERNATIONAL,
    EXPRESS
}
