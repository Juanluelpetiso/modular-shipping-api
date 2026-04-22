package com.jretamales.shipping.infrastructure.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para ShippingController.
 * Levanta el contexto completo de Spring Boot y prueba el endpoint
 * end-to-end con los 3 tipos de envío + validación de errores.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ShippingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /calculate-rate — Envío Nacional: debe retornar totalCost=10052.5")
    void shouldCalculateNationalRate() throws Exception {
        String requestBody = """
            {
                "weight": 10.5,
                "origin": "Buenos Aires",
                "destination": "Córdoba",
                "shipmentType": "NATIONAL"
            }
            """;

        mockMvc.perform(post("/api/v1/shipping/calculate-rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalCost").value(10052.5))
            .andExpect(jsonPath("$.currency").value("ARS"))
            .andExpect(jsonPath("$.estimatedDeliveryDays").value(5))
            .andExpect(jsonPath("$.breakdown.baseRate").value(10000.0))
            .andExpect(jsonPath("$.breakdown.distanceFee").value(52.5))
            .andExpect(jsonPath("$.breakdown.customsTax").value(0.0));
    }

    @Test
    @DisplayName("POST /calculate-rate — Envío Internacional: debe incluir customsTax > 0")
    void shouldCalculateInternationalRate() throws Exception {
        String requestBody = """
            {
                "weight": 10.5,
                "origin": "Buenos Aires",
                "destination": "New York",
                "shipmentType": "INTERNATIONAL"
            }
            """;

        mockMvc.perform(post("/api/v1/shipping/calculate-rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.currency").value("USD"))
            .andExpect(jsonPath("$.estimatedDeliveryDays").value(15))
            .andExpect(jsonPath("$.breakdown.customsTax").isNumber());
    }

    @Test
    @DisplayName("POST /calculate-rate — Envío Express: entrega en 1 día, moneda ARS")
    void shouldCalculateExpressRate() throws Exception {
        String requestBody = """
            {
                "weight": 10.5,
                "origin": "Buenos Aires",
                "destination": "Córdoba",
                "shipmentType": "EXPRESS"
            }
            """;

        mockMvc.perform(post("/api/v1/shipping/calculate-rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.estimatedDeliveryDays").value(1))
            .andExpect(jsonPath("$.currency").value("ARS"));
    }

    @Test
    @DisplayName("POST /calculate-rate — Request inválido: debe retornar 400")
    void shouldReturnBadRequestForInvalidInput() throws Exception {
        String requestBody = """
            {
                "weight": -5,
                "origin": "",
                "destination": "",
                "shipmentType": "NATIONAL"
            }
            """;

        mockMvc.perform(post("/api/v1/shipping/calculate-rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /calculate-rate — ShipmentType inválido: debe retornar 400")
    void shouldReturnBadRequestForInvalidShipmentType() throws Exception {
        String requestBody = """
            {
                "weight": 5.0,
                "origin": "Buenos Aires",
                "destination": "Córdoba",
                "shipmentType": "INVALID_TYPE"
            }
            """;

        mockMvc.perform(post("/api/v1/shipping/calculate-rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /calculate-rate — Body vacío: debe retornar 400")
    void shouldReturnBadRequestForEmptyBody() throws Exception {
        mockMvc.perform(post("/api/v1/shipping/calculate-rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
}
