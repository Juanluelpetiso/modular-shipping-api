# Spec-Driven Development: Modular Shipping API (Java 21)

## 1. Objetivo del Proyecto
[cite_start]Construir una API REST modular para el cálculo de tarifas de envío, diseñada bajo el paradigma **AI-First** [cite: 47-48]. [cite_start]El sistema delega la lógica de negocio a un **Skills Registry** para evitar el **Context Overload** y permitir una escalabilidad técnica superior [cite: 168, 211-213].

## 2. Stack Tecnológico
* **Lenguaje**: Java 21 (Uso obligatorio de **Virtual Threads** para alta concurrencia).
* **Framework**: Spring Boot 3.4+.
* **Arquitectura**: Clean Architecture con el patrón **Strategy** para desacoplar los módulos de envío.
* [cite_start]**Metodología**: Spec-Driven Development (SDD) [cite: 740-741].

## 3. Arquitectura Cognitiva (Agentes y Skills)
[cite_start]El desarrollo es liderado por un **Orquestador** que coordina sub-agentes especializados [cite: 255-259]:
* [cite_start]**Agente de Diseño**: Propone la estructura de clases y paquetes antes de codear (**Plan Mode**) [cite: 66-67, 156].
* [cite_start]**Agentes de Módulo**: Implementan la lógica específica basándose en las **Skills** cargadas [cite: 122-128, 187].
* [cite_start]**Agente de Testing**: Genera automáticamente pruebas unitarias y de integración con JUnit 5 [cite: 710-714, 846].

## [cite_start]4. Skills Registry (Lógica Modular) [cite: 211-213]
La inteligencia del sistema reside en archivos Markdown dentro de `.ai/skills/`:
* `skill-national.md`: Cálculo de tarifas para territorio nacional (Argentina).
* `skill-international.md`: Gestión de aduanas y factores de distancia global.
* `skill-express.md`: Reglas de prioridad para entregas en 24 horas (exclusivo nacional).

## 5. Especificación de API

### Endpoint: `POST /api/v1/shipping/calculate-rate`
**Request Body (JSON)**:
```json
{
  "weight": 10.5,
  "origin": "string",
  "destination": "string",
  "shipmentType": "string" // NATIONAL, INTERNATIONAL, EXPRESS
}

Response Body (JSON):
{
  "totalCost": 1500.50,
  "currency": "ARS / USD",
  "estimatedDeliveryDays": 1,
  "breakdown": {
    "baseRate": 10000.0,
    "distanceFee": 300.0,
    "customsTax": 500.0
  }
}

6. Reglas de Negocio (Core Logic)Módulo Nacional: 10000.0 + (Peso * 5.0).Módulo Internacional: (25.0 + (Peso * 15.0)) * 1.25 (Incluye 25% de recargo aduanero obligatorio, moneda: USD).Módulo Express:Restricción: Solo disponible para envíos Nacionales.Garantía: Entrega en 24 horas.Recargo: 50% extra sobre el total del cálculo nacional base (Base: 20000.0).

7. Protocolo Human in the Loop (HITL) Punto de Aprobación 1: El agente debe presentar un plan de arquitectura detallado antes de crear archivos.Punto de Aprobación 2: Los tests generados deben ser validados manualmente antes de integrarse al repositorio .Transparencia: El agente debe citar qué Skill utilizó para cada componente generado .