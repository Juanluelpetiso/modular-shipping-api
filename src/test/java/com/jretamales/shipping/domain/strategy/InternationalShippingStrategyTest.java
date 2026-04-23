package com.jretamales.shipping.domain.strategy;

import com.jretamales.shipping.domain.model.ShipmentType;
import com.jretamales.shipping.domain.model.ShippingCalculation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para InternationalShippingStrategy.
 * Skill citada: skill-international.md
 * Fórmula verificada: Total = (25.0 + (Peso × 15.0)) × 1.25
 */
class InternationalShippingStrategyTest {

    private InternationalShippingStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new InternationalShippingStrategy();
    }

    @Test
    @DisplayName("Debe retornar INTERNATIONAL como tipo de envío")
    void shouldReturnInternationalShipmentType() {
        assertEquals(ShipmentType.INTERNATIONAL, strategy.getShipmentType());
    }

    @Test
    @DisplayName("Debe calcular correctamente: (25.0 + (10.5 × 15)) × 1.25 = 228.125")
    void shouldCalculateInternationalRateCorrectly() {
        ShippingCalculation result = strategy.calculate(10.5, "Buenos Aires", "New York");

        double expectedSubtotal = 25.0 + (10.5 * 15.0);              // 182.5
        double expectedCustomsTax = expectedSubtotal * 0.25;          // 45.625
        double expectedTotal = expectedSubtotal + expectedCustomsTax; // 228.125

        assertEquals(expectedTotal, result.totalCost());
        assertEquals("USD", result.currency());
        assertEquals(15, result.estimatedDeliveryDays());
        assertEquals(25.0, result.breakdown().baseRate());
        assertEquals(10.5 * 15.0, result.breakdown().distanceFee());
        assertEquals(expectedCustomsTax, result.breakdown().customsTax());
    }

    @Test
    @DisplayName("Debe incluir recargo aduanero del 25% incluso con peso 0")
    void shouldAlwaysIncludeCustomsTax() {
        ShippingCalculation result = strategy.calculate(0.0, "Buenos Aires", "Madrid");

        // Peso 0: subtotal = 25.0, customs = 6.25, total = 31.25
        assertEquals(31.25, result.totalCost());
        assertEquals(6.25, result.breakdown().customsTax());
    }

    @Test
    @DisplayName("Debe usar USD como moneda")
    void shouldUseDollarsAsCurrency() {
        ShippingCalculation result = strategy.calculate(1.0, "Buenos Aires", "London");
        assertEquals("USD", result.currency());
    }

    @Test
    @DisplayName("Debe estimar 15 días de entrega")
    void shouldEstimateFifteenDays() {
        ShippingCalculation result = strategy.calculate(1.0, "Buenos Aires", "Tokyo");
        assertEquals(15, result.estimatedDeliveryDays());
    }
}
