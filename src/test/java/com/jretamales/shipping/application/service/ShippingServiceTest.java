package com.jretamales.shipping.application.service;

import com.jretamales.shipping.application.resolver.ShippingStrategyResolver;
import com.jretamales.shipping.domain.model.CostBreakdown;
import com.jretamales.shipping.domain.model.ShipmentType;
import com.jretamales.shipping.domain.model.ShippingCalculation;
import com.jretamales.shipping.domain.strategy.ShippingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ShippingService.
 * Verifica la orquestación correcta: resolver → strategy → resultado.
 */
@ExtendWith(MockitoExtension.class)
class ShippingServiceTest {

    @Mock
    private ShippingStrategyResolver resolver;

    @Mock
    private ShippingStrategy strategy;

    private ShippingService service;

    @BeforeEach
    void setUp() {
        service = new ShippingService(resolver);
    }

    @Test
    @DisplayName("Debe delegar el cálculo a la estrategia resuelta por el resolver")
    void shouldDelegateToResolvedStrategy() {
        ShippingCalculation expected = new ShippingCalculation(
            10052.5, "ARS", 5,
            new CostBreakdown(10000.0, 52.5, 0.0)
        );

        when(resolver.resolve(ShipmentType.NATIONAL)).thenReturn(strategy);
        when(strategy.calculate(10.5, "Buenos Aires", "Córdoba")).thenReturn(expected);

        ShippingCalculation result = service.calculateRate(
            ShipmentType.NATIONAL, 10.5, "Buenos Aires", "Córdoba"
        );

        assertEquals(expected, result);
        verify(resolver).resolve(ShipmentType.NATIONAL);
        verify(strategy).calculate(10.5, "Buenos Aires", "Córdoba");
    }

    @Test
    @DisplayName("Debe invocar el resolver exactamente una vez por cálculo")
    void shouldInvokeResolverOnce() {
        ShippingCalculation mockResult = new ShippingCalculation(
            25000.0, "USD", 15,
            new CostBreakdown(20000.0, 0.0, 5000.0)
        );

        when(resolver.resolve(ShipmentType.INTERNATIONAL)).thenReturn(strategy);
        when(strategy.calculate(anyDouble(), anyString(), anyString())).thenReturn(mockResult);

        service.calculateRate(ShipmentType.INTERNATIONAL, 5.0, "Buenos Aires", "Madrid");

        verify(resolver, times(1)).resolve(ShipmentType.INTERNATIONAL);
    }
}
