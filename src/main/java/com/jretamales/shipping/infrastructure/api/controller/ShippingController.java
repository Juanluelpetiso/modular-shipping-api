package com.jretamales.shipping.infrastructure.api.controller;

import com.jretamales.shipping.application.service.ShippingService;
import com.jretamales.shipping.domain.model.ShippingCalculation;
import com.jretamales.shipping.infrastructure.api.dto.ShippingRequest;
import com.jretamales.shipping.infrastructure.api.dto.ShippingResponse;
import com.jretamales.shipping.infrastructure.api.mapper.ShippingMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller para el cálculo de tarifas de envío.
 * Endpoint: POST /api/v1/shipping/calculate-rate
 * Origen: shipping-spec.md § 5.
 */
@RestController
@RequestMapping("/api/v1/shipping")
@Tag(name = "Shipping", description = "API de cálculo de tarifas de envío")
public class ShippingController {

    private final ShippingService shippingService;
    private final ShippingMapper mapper;

    public ShippingController(ShippingService shippingService, ShippingMapper mapper) {
        this.shippingService = shippingService;
        this.mapper = mapper;
    }

    @PostMapping("/calculate-rate")
    @Operation(
        summary = "Calcular tarifa de envío",
        description = "Calcula el costo total de un envío según su tipo (NATIONAL, INTERNATIONAL, EXPRESS). "
            + "Utiliza el patrón Strategy para delegar al módulo de cálculo correspondiente."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cálculo exitoso",
            content = @Content(schema = @Schema(implementation = ShippingResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Request inválido o tipo de envío no soportado"),
        @ApiResponse(responseCode = "422", description = "Error de negocio (ej: Express para destino internacional)")
    })
    public ResponseEntity<ShippingResponse> calculateRate(
            @Valid @RequestBody ShippingRequest request) {

        ShippingCalculation result = shippingService.calculateRate(
            request.shipmentType(),
            request.weight(),
            request.origin(),
            request.destination()
        );

        return ResponseEntity.ok(mapper.toResponse(result));
    }
}
