package com.jretamales.shipping.domain.model;

/**
 * Record inmutable que encapsula el resultado completo de un cálculo de envío.
 * Origen: shipping-spec.md § 5 — response body + skill-national.md Pattern 1.
 *
 * @param totalCost             Costo total calculado
 * @param currency              Moneda del cálculo (ARS / USD)
 * @param estimatedDeliveryDays Días estimados de entrega
 * @param breakdown             Desglose detallado de costos
 */
public record ShippingCalculation(
    double totalCost,
    String currency,
    int estimatedDeliveryDays,
    CostBreakdown breakdown
) {}
