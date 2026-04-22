package com.jretamales.shipping.domain.strategy;

import com.jretamales.shipping.domain.model.CostBreakdown;
import com.jretamales.shipping.domain.model.ShipmentType;
import com.jretamales.shipping.domain.model.ShippingCalculation;
import org.springframework.stereotype.Component;

/**
 * Estrategia de cálculo para envíos nacionales (Argentina).
 *
 * <p><b>Skill citada:</b> {@code .ai/skills/skill-national.md}</p>
 * <p><b>Fórmula:</b> {@code Total = 10000.0 + (Peso × 5.0)}</p>
 *
 * <ul>
 *   <li>Base Rate: $10,000 ARS</li>
 *   <li>Factor de peso: 5.0 por kg</li>
 *   <li>Moneda: ARS</li>
 *   <li>Entrega estimada: 5 días</li>
 * </ul>
 */
@Component
public class NationalShippingStrategy implements ShippingStrategy {

    private static final double BASE_RATE = 10000.0;
    private static final double WEIGHT_FACTOR = 5.0;
    private static final String CURRENCY = "ARS";
    private static final int ESTIMATED_DAYS = 5;

    @Override
    public ShipmentType getShipmentType() {
        return ShipmentType.NATIONAL;
    }

    @Override
    public ShippingCalculation calculate(double weight, String origin, String destination) {
        double distanceFee = weight * WEIGHT_FACTOR;
        double totalCost = BASE_RATE + distanceFee;

        return new ShippingCalculation(
            totalCost,
            CURRENCY,
            ESTIMATED_DAYS,
            new CostBreakdown(BASE_RATE, distanceFee, 0.0)
        );
    }
}
