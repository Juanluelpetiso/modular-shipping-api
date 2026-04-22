package com.jretamales.shipping.domain.model;

/**
 * Record inmutable que representa el desglose de costos de un cálculo de envío.
 * Origen: shipping-spec.md § 5 — campo "breakdown" del response.
 *
 * @param baseRate    Tarifa base del módulo de envío
 * @param distanceFee Recargo por peso/distancia
 * @param customsTax  Tasa aduanera (0.0 para envíos nacionales/express)
 */
public record CostBreakdown(
    double baseRate,
    double distanceFee,
    double customsTax
) {}
