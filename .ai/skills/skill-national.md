---
name: national-shipping-logic
description: >
  Maneja la lógica de cálculo para envíos dentro del territorio nacional (Argentina).
  Trigger: Al trabajar con el ShipmentService, al procesar pedidos con shipmentType NATIONAL o EXPRESS.
metadata:
  author: Juanluelpetiso
  version: "1.1"
---

## When to Use

Load this skill when:
- [cite_start]El sistema detecta un pedido con origen y destino dentro de Argentina [cite: 756-758].
- [cite_start]Se requiere calcular el `totalCost` para tipos de envío NATIONAL o EXPRESS [cite: 627-628].
- Se está implementando o refactorizando el `NationalShippingStrategy`.

## Critical Patterns

### Pattern 1: Immutability with Java 21 Records

[cite_start]Para garantizar que los datos de cálculo no cambien durante el proceso y aprovechar la performance de Java 21[cite: 436, 479].

```java
public record ShippingCalculation(
    double baseRate,
    double distanceFee,
    double totalCost
) {}

Pattern 2: National Rate Formula
La lógica debe seguir estrictamente la fórmula de negocio actualizada:
Total = 10000.0 + (Peso * 5.0)

// Logic implementation
public double calculate(double weight) {
    double base = 10000.0; // Precio base nacional
    double variable = weight * 5.0; // Factor de distancia nacional
    return base + variable;
}

