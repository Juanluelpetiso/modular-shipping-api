---
name: international-shipping-logic
description: >
  Maneja la lógica de cálculo para envíos fuera de Argentina, incluyendo tasas aduaneras.
  Trigger: Al trabajar con el ShipmentService o procesar pedidos con shipmentType INTERNATIONAL.
metadata:
  author: Juanluelpetiso
  version: "1.0"
---

## When to Use

Load this skill when:
- El destino del envío es un país diferente al origen (Tráfico Internacional).
- [cite_start]Se requiere aplicar el recargo aduanero obligatorio del 25% [cite: 627-628].
- Se está implementando o refactorizando la clase `InternationalShippingStrategy`.

## Critical Patterns

### Pattern 1: International Rate Formula

[cite_start]Para asegurar la precisión y evitar alucinaciones en el cálculo financiero [cite: 562, 597-598], se debe seguir esta fórmula estrictamente:
Total = (20000.0 + (Peso * 15.0)) * 1.25

```java
// Logic implementation in Java 21
public double calculate(double weight) {
    double baseRate = 20000.0;     // Tarifa base internacional
    double distanceFactor = 15.0;  // Factor de distancia internacional
    double subtotal = baseRate + (weight * distanceFactor);
    
    return subtotal * 1.25; // Aplicación del 15% de recargo aduanero
}

Pattern 2: 
Data Integrity with Records
Como se especifica en el AGENT.md, se deben usar Records para mantener la inmutabilidad de los resultados del cálculo.

// Good example
public record InternationalCalculation(
    double baseRate,
    double customsTax,
    double finalAmount
) {}