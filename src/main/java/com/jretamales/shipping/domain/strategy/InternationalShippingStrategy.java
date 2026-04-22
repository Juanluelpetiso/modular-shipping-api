package com.jretamales.shipping.domain.strategy;

import com.jretamales.shipping.domain.model.CostBreakdown;
import com.jretamales.shipping.domain.model.ShipmentType;
import com.jretamales.shipping.domain.model.ShippingCalculation;
import org.springframework.stereotype.Component;

/**
 * Estrategia de cálculo para envíos internacionales.
 *
 * <p><b>Skill citada:</b> {@code .ai/skills/skill-international.md}</p>
 * <p><b>Fórmula:</b> {@code Total = (20000.0 + (Peso × 15.0)) × 1.25}</p>
 *
 * <ul>
 *   <li>Base Rate: $20,000</li>
 *   <li>Factor de peso: 15.0 por kg</li>
 *   <li>Recargo aduanero obligatorio: 25%</li>
 *   <li>Moneda: USD</li>
 *   <li>Entrega estimada: 15 días</li>
 * </ul>
 */
@Component
public class InternationalShippingStrategy implements ShippingStrategy {

    private static final double BASE_RATE = 20000.0;
    private static final double WEIGHT_FACTOR = 15.0;
    private static final double CUSTOMS_TAX_RATE = 0.25;
    private static final String CURRENCY = "USD";
    private static final int ESTIMATED_DAYS = 15;

    @Override
    public ShipmentType getShipmentType() {
        return ShipmentType.INTERNATIONAL;
    }

    @Override
    public ShippingCalculation calculate(double weight, String origin, String destination) {
        double distanceFee = weight * WEIGHT_FACTOR;
        double subtotal = BASE_RATE + distanceFee;
        double customsTax = subtotal * CUSTOMS_TAX_RATE;
        double totalCost = subtotal + customsTax;

        return new ShippingCalculation(
            totalCost,
            CURRENCY,
            ESTIMATED_DAYS,
            new CostBreakdown(BASE_RATE, distanceFee, customsTax)
        );
    }
}
