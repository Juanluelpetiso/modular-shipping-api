package com.jretamales.shipping.infrastructure.api.dto;

import com.jretamales.shipping.domain.model.ShipmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Record DTO para el request body del endpoint de cálculo de tarifa.
 * Origen: shipping-spec.md § 5 — Request Body.
 *
 * @param weight       Peso del paquete en kg (debe ser positivo)
 * @param origin       Ciudad/país de origen (obligatorio)
 * @param destination  Ciudad/país de destino (obligatorio)
 * @param shipmentType Tipo de envío: NATIONAL, INTERNATIONAL o EXPRESS
 */
public record ShippingRequest(

    @NotNull(message = "El peso es obligatorio")
    @Positive(message = "El peso debe ser mayor a 0")
    Double weight,

    @NotBlank(message = "El origen es obligatorio")
    String origin,

    @NotBlank(message = "El destino es obligatorio")
    String destination,

    @NotNull(message = "El tipo de envío es obligatorio")
    ShipmentType shipmentType

) {}
