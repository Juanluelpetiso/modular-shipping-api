package com.jretamales.shipping.infrastructure.api.dto;

/**
 * Record DTO para el response body del endpoint de cálculo de tarifa.
 * Origen: shipping-spec.md § 5 — Response Body.
 *
 * @param totalCost             Costo total calculado
 * @param currency              Moneda del resultado (ARS / USD)
 * @param estimatedDeliveryDays Días estimados de entrega
 * @param breakdown             Desglose detallado de costos
 */
public record ShippingResponse(
    double totalCost,
    String currency,
    int estimatedDeliveryDays,
    BreakdownResponse breakdown
) {

    /**
     * Desglose de costos del envío.
     *
     * @param baseRate    Tarifa base del módulo
     * @param distanceFee Recargo por peso/distancia
     * @param customsTax  Tasa aduanera (0.0 si no aplica)
     */
    public record BreakdownResponse(
        double baseRate,
        double distanceFee,
        double customsTax
    ) {}
}
