package com.jretamales.shipping.application.service;

import com.jretamales.shipping.application.resolver.ShippingStrategyResolver;
import com.jretamales.shipping.domain.model.ShipmentType;
import com.jretamales.shipping.domain.model.ShippingCalculation;
import com.jretamales.shipping.domain.strategy.ShippingStrategy;
import org.springframework.stereotype.Service;

/**
 * Caso de uso principal: cálculo de tarifas de envío.
 *
 * <p>Orquesta la resolución de la estrategia correcta y la ejecución del cálculo.
 * No contiene lógica de negocio — solo coordina el flujo entre el resolver
 * y la estrategia seleccionada.</p>
 */
@Service
public class ShippingService {

    private final ShippingStrategyResolver resolver;

    public ShippingService(ShippingStrategyResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * Calcula la tarifa de envío delegando al módulo de cálculo apropiado.
     *
     * @param type        Tipo de envío (NATIONAL, INTERNATIONAL, EXPRESS)
     * @param weight      Peso del paquete en kg
     * @param origin      Ciudad/país de origen
     * @param destination Ciudad/país de destino
     * @return Resultado completo del cálculo con desglose de costos
     */
    public ShippingCalculation calculateRate(ShipmentType type, double weight,
                                              String origin, String destination) {
        ShippingStrategy strategy = resolver.resolve(type);
        return strategy.calculate(weight, origin, destination);
    }
}
