package com.jretamales.shipping.domain.strategy;

import com.jretamales.shipping.domain.model.ShipmentType;
import com.jretamales.shipping.domain.model.ShippingCalculation;

/**
 * Interfaz central del patrón Strategy para el cálculo modular de tarifas de envío.
 *
 * <p>Cada implementación representa un módulo de cálculo específico, registrado
 * en el Skills Registry ({@code .ai/skills/}). El método {@link #getShipmentType()}
 * permite al {@code ShippingStrategyResolver} identificar automáticamente qué
 * estrategia corresponde a cada tipo sin lógica condicional manual.</p>
 *
 * <p><b>Escalabilidad:</b> Para agregar un nuevo módulo de envío, basta con crear
 * una nueva clase que implemente esta interfaz con {@code @Component}. No se
 * requiere modificar código existente (Open/Closed Principle).</p>
 */
public interface ShippingStrategy {

    /**
     * Identifica el tipo de envío que maneja esta estrategia.
     * Usado por el ShippingStrategyResolver para el auto-registro.
     *
     * @return el tipo de envío asociado a esta estrategia
     */
    ShipmentType getShipmentType();

    /**
     * Calcula la tarifa de envío según la lógica de negocio del módulo.
     *
     * @param weight      Peso del paquete en kg
     * @param origin      Ciudad/país de origen
     * @param destination Ciudad/país de destino
     * @return ShippingCalculation con el resultado completo del cálculo
     */
    ShippingCalculation calculate(double weight, String origin, String destination);
}
