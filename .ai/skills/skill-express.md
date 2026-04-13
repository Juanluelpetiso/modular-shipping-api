---
name: express-shipping-logic
description: >
  Maneja la lógica de prioridad para entregas garantizadas en 24 horas.
  Trigger: Al trabajar con el ShipmentService o procesar pedidos con shipmentType EXPRESS.
metadata:
  author: Juanluelpetiso
  version: "1.0"
---

## When to Use

Load this skill when:
- [cite_start]El usuario requiere entrega garantizada en un plazo máximo de 24 horas [cite: 756-758].
- El envío es estrictamente NATIONAL (Origen y Destino dentro de Argentina).
- Se requiere aplicar el recargo de prioridad del 50%.

## Critical Patterns

### Pattern 1: Express Strategy Validation

Antes de calcular, se debe validar que el envío no sea internacional, ya que el servicio Express de 24hs no está disponible fuera del país.

```java
// Logic validation
if (shipmentType == EXPRESS && isInternational) {
    throw new IllegalArgumentException("Servicio Express solo disponible para envíos nacionales");
}

Pattern 2: Express Rate Formula
La lógica extiende la base nacional pero con una tarifa inicial mayor y un recargo final :
Total = (20000.0 + (Peso * 5.0)) * 1.50

// Logic implementation in Java 21
public double calculate(double weight) {
    double baseExpress = 20000.0; // Precio base para envíos express
    double nationalFactor = 5.0;  // Factor de distancia nacional
    double subtotal = baseExpress + (weight * nationalFactor);
    
    return subtotal * 1.50; // 50% de recargo por entrega en 24hs
}