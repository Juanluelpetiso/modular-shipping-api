package com.jretamales.shipping.infrastructure.api.mapper;

import com.jretamales.shipping.domain.model.ShippingCalculation;
import com.jretamales.shipping.infrastructure.api.dto.ShippingResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper que convierte entre objetos de dominio y DTOs de la capa de infraestructura.
 * Mantiene la separación entre capas según Clean Architecture.
 */
@Component
public class ShippingMapper {

    /**
     * Convierte un resultado de cálculo del dominio a un DTO de respuesta HTTP.
     *
     * @param calculation Resultado del cálculo de dominio
     * @return DTO de respuesta listo para serialización JSON
     */
    public ShippingResponse toResponse(ShippingCalculation calculation) {
        return new ShippingResponse(
            calculation.totalCost(),
            calculation.currency(),
            calculation.estimatedDeliveryDays(),
            new ShippingResponse.BreakdownResponse(
                calculation.breakdown().baseRate(),
                calculation.breakdown().distanceFee(),
                calculation.breakdown().customsTax()
            )
        );
    }
}
