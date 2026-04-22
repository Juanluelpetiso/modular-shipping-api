package com.jretamales.shipping.domain.strategy;

import com.jretamales.shipping.domain.exception.ShippingException;
import com.jretamales.shipping.domain.model.CostBreakdown;
import com.jretamales.shipping.domain.model.ShipmentType;
import com.jretamales.shipping.domain.model.ShippingCalculation;
import org.springframework.stereotype.Component;

/**
 * Estrategia de cálculo para envíos express (entrega garantizada en 24 horas).
 *
 * <p><b>Skill citada:</b> {@code .ai/skills/skill-express.md}</p>
 * <p><b>Fórmula:</b> {@code Total = (20000.0 + (Peso × 5.0)) × 1.50}</p>
 *
 * <p><b>Restricción:</b> Solo disponible para envíos nacionales (Argentina).
 * Si se detecta un envío internacional, se lanza {@link ShippingException}.</p>
 *
 * <ul>
 *   <li>Base Rate Express: $20,000 ARS</li>
 *   <li>Factor de peso: 5.0 por kg (mismo que nacional)</li>
 *   <li>Recargo de prioridad: 50%</li>
 *   <li>Moneda: ARS</li>
 *   <li>Entrega garantizada: 1 día (24hs)</li>
 * </ul>
 */
@Component
public class ExpressShippingStrategy implements ShippingStrategy {

    private static final double BASE_RATE = 20000.0;
    private static final double WEIGHT_FACTOR = 5.0;
    private static final double EXPRESS_SURCHARGE_RATE = 0.50;
    private static final String CURRENCY = "ARS";
    private static final int ESTIMATED_DAYS = 1;

    @Override
    public ShipmentType getShipmentType() {
        return ShipmentType.EXPRESS;
    }

    @Override
    public ShippingCalculation calculate(double weight, String origin, String destination) {
        // Validación: Express solo disponible para envíos nacionales (skill-express.md Pattern 1)
        validateNationalShipment(origin, destination);

        double distanceFee = weight * WEIGHT_FACTOR;
        double subtotal = BASE_RATE + distanceFee;
        double expressSurcharge = subtotal * EXPRESS_SURCHARGE_RATE;
        double totalCost = subtotal + expressSurcharge;

        return new ShippingCalculation(
            totalCost,
            CURRENCY,
            ESTIMATED_DAYS,
            new CostBreakdown(BASE_RATE, distanceFee, 0.0)
        );
    }

    /**
     * Valida que el envío sea nacional. El servicio Express de 24hs
     * no está disponible para envíos internacionales.
     *
     * <p>Nota: En producción, esta validación se haría contra un catálogo
     * de localidades/países. Por ahora se valida que origen y destino no estén vacíos.</p>
     */
    private void validateNationalShipment(String origin, String destination) {
        if (origin == null || origin.isBlank() || destination == null || destination.isBlank()) {
            throw new ShippingException(
                "Servicio Express solo disponible para envíos nacionales. " +
                "Origen y destino son obligatorios."
            );
        }
    }
}
