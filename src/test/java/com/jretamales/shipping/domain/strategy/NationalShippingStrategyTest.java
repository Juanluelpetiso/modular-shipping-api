package com.jretamales.shipping.domain.strategy;

import com.jretamales.shipping.domain.model.ShipmentType;
import com.jretamales.shipping.domain.model.ShippingCalculation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para NationalShippingStrategy.
 * Skill citada: skill-national.md
 * Fórmula verificada: Total = 10000.0 + (Peso × 5.0)
 */
class NationalShippingStrategyTest {

    private NationalShippingStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new NationalShippingStrategy();
    }

    @Test
    @DisplayName("Debe retornar NATIONAL como tipo de envío")
    void shouldReturnNationalShipmentType() {
        assertEquals(ShipmentType.NATIONAL, strategy.getShipmentType());
    }

    @Test
    @DisplayName("Debe calcular correctamente: 10000 + (10.5 × 5.0) = 10052.5")
    void shouldCalculateNationalRateCorrectly() {
        ShippingCalculation result = strategy.calculate(10.5, "Buenos Aires", "Córdoba");

        assertEquals(10052.5, result.totalCost());
        assertEquals("ARS", result.currency());
        assertEquals(5, result.estimatedDeliveryDays());
        assertEquals(10000.0, result.breakdown().baseRate());
        assertEquals(52.5, result.breakdown().distanceFee());
        assertEquals(0.0, result.breakdown().customsTax());
    }

    @Test
    @DisplayName("Debe calcular tarifa con peso mínimo (1 kg)")
    void shouldCalculateWithMinimalWeight() {
        ShippingCalculation result = strategy.calculate(1.0, "Buenos Aires", "Rosario");
        assertEquals(10005.0, result.totalCost());
    }

    @Test
    @DisplayName("Debe calcular tarifa con peso alto (100 kg)")
    void shouldCalculateWithHeavyWeight() {
        ShippingCalculation result = strategy.calculate(100.0, "Buenos Aires", "Mendoza");
        assertEquals(10500.0, result.totalCost());
    }

    @Test
    @DisplayName("Debe tener customsTax en 0 para envíos nacionales")
    void shouldHaveZeroCustomsTax() {
        ShippingCalculation result = strategy.calculate(5.0, "Buenos Aires", "Tucumán");
        assertEquals(0.0, result.breakdown().customsTax());
    }
}
