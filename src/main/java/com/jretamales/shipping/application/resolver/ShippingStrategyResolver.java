package com.jretamales.shipping.application.resolver;

import com.jretamales.shipping.domain.exception.UnsupportedShipmentTypeException;
import com.jretamales.shipping.domain.model.ShipmentType;
import com.jretamales.shipping.domain.strategy.ShippingStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Resuelve dinámicamente la estrategia de cálculo según el tipo de envío.
 *
 * <p>Aprovecha la inyección de dependencias de Spring para auto-registrar
 * todas las implementaciones de {@link ShippingStrategy}. Al agregar un
 * nuevo módulo de envío, solo se necesita crear una nueva clase con
 * {@code @Component} — no se modifica este resolver (Open/Closed Principle).</p>
 */
@Component
public class ShippingStrategyResolver {

    private final Map<ShipmentType, ShippingStrategy> strategyMap;

    /**
     * Spring inyecta automáticamente TODAS las implementaciones de ShippingStrategy
     * registradas como @Component en el contexto.
     *
     * @param strategies Lista de todas las estrategias disponibles
     */
    public ShippingStrategyResolver(List<ShippingStrategy> strategies) {
        this.strategyMap = strategies.stream()
            .collect(Collectors.toMap(
                ShippingStrategy::getShipmentType,
                Function.identity()
            ));
    }

    /**
     * Resuelve la estrategia correspondiente al tipo de envío.
     *
     * @param type Tipo de envío a resolver
     * @return La estrategia de cálculo correspondiente
     * @throws UnsupportedShipmentTypeException si no existe estrategia para el tipo dado
     */
    public ShippingStrategy resolve(ShipmentType type) {
        return Optional.ofNullable(strategyMap.get(type))
            .orElseThrow(() -> new UnsupportedShipmentTypeException(type));
    }
}
