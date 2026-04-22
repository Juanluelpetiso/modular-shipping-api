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
 * Fórmula verificada: Total = (20000.0 + (Peso × 15.0)) × 1.25
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
    @DisplayName("Debe calcular correctamente: (20000 + (10.5 × 15)) × 1.25 = 25196.875")
    void shouldCalculateInternationalRateCorrectly() {
        ShippingCalculation result = strategy.calculate(10.5, "Buenos Aires", "New York");

        double expectedSubtotal = 20000.0 + (10.5 * 15.0);     // 20157.5
        double expectedCustomsTax = expectedSubtotal * 0.25;     // 5039.375
        double expectedTotal = expectedSubtotal + expectedCustomsTax; // 25196.875

        assertEquals(expectedTotal, result.totalCost());
        assertEquals("USD", result.currency());
        assertEquals(15, result.estimatedDeliveryDays());
        assertEquals(20000.0, result.breakdown().baseRate());
        assertEquals(10.5 * 15.0, result.breakdown().distanceFee());
        assertEquals(expectedCustomsTax, result.breakdown().customsTax());
    }

    @Test
    @DisplayName("Debe incluir recargo aduanero del 25% incluso con peso 0")
    void shouldAlwaysIncludeCustomsTax() {
        ShippingCalculation result = strategy.calculate(0.0, "Buenos Aires", "Madrid");

        // Peso 0: subtotal = 20000, customs = 5000, total = 25000
        assertEquals(25000.0, result.totalCost());
        assertEquals(5000.0, result.breakdown().customsTax());
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
