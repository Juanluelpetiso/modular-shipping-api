package com.jretamales.shipping.domain.strategy;

import com.jretamales.shipping.domain.exception.ShippingException;
import com.jretamales.shipping.domain.model.ShipmentType;
import com.jretamales.shipping.domain.model.ShippingCalculation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para ExpressShippingStrategy.
 * Skill citada: skill-express.md
 * Fórmula verificada: Total = (20000.0 + (Peso × 5.0)) × 1.50
 */
class ExpressShippingStrategyTest {

    private ExpressShippingStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new ExpressShippingStrategy();
    }

    @Test
    @DisplayName("Debe retornar EXPRESS como tipo de envío")
    void shouldReturnExpressShipmentType() {
        assertEquals(ShipmentType.EXPRESS, strategy.getShipmentType());
    }

    @Test
    @DisplayName("Debe calcular correctamente: (20000 + (10.5 × 5.0)) × 1.50 = 30078.75")
    void shouldCalculateExpressRateCorrectly() {
        ShippingCalculation result = strategy.calculate(10.5, "Buenos Aires", "Córdoba");

        double expectedSubtotal = 20000.0 + (10.5 * 5.0);    // 20052.5
        double expectedTotal = expectedSubtotal * 1.50;        // 30078.75

        assertEquals(expectedTotal, result.totalCost());
        assertEquals("ARS", result.currency());
        assertEquals(1, result.estimatedDeliveryDays());
    }

    @Test
    @DisplayName("Debe garantizar entrega en 1 día (24hs)")
    void shouldGuaranteeOneDayDelivery() {
        ShippingCalculation result = strategy.calculate(5.0, "Buenos Aires", "La Plata");
        assertEquals(1, result.estimatedDeliveryDays());
    }

    @Test
    @DisplayName("Debe aplicar recargo del 50% sobre el subtotal")
    void shouldApply50PercentSurcharge() {
        ShippingCalculation result = strategy.calculate(0.0, "Buenos Aires", "Rosario");

        // Sin peso: subtotal = 20000, total = 30000 (50% recargo)
        assertEquals(30000.0, result.totalCost());
    }

    @Test
    @DisplayName("Debe lanzar excepción si origen es nulo")
    void shouldThrowExceptionForNullOrigin() {
        assertThrows(ShippingException.class, () ->
            strategy.calculate(5.0, null, "Córdoba")
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción si destino está vacío")
    void shouldThrowExceptionForEmptyDestination() {
        assertThrows(ShippingException.class, () ->
            strategy.calculate(5.0, "Buenos Aires", "")
        );
    }

    @Test
    @DisplayName("Debe usar ARS como moneda (envío nacional)")
    void shouldUseArsAsCurrency() {
        ShippingCalculation result = strategy.calculate(1.0, "Buenos Aires", "Mendoza");
        assertEquals("ARS", result.currency());
    }
}
