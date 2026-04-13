Spec-Driven Development: Modular Shipping API (Java 21)

1. Objetivo del ProyectoConstruir una API REST modular para el cálculo de tarifas de envío, diseñada bajo el paradigma AI-First . El sistema debe delegar la lógica de negocio a un Skills Registry para evitar el Context Overload y permitir una escalabilidad limpia .

2. Stack TecnológicoLenguaje: Java 21 (Uso mandatorio de Virtual Threads para alta concurrencia).Framework: Spring Boot 3.4+.Arquitectura: Basada en el patrón Strategy para desacoplar los módulos de envío.Metodología: Spec-Driven Development (SDD) .

3. Orquestación Multi-Agente El desarrollo será liderado por un Orquestador que coordinará sub-agentes especializados:Agente de Diseño: Encargado de proponer la estructura de clases antes de codear (Plan Mode) .Agentes de Módulo: Especializados según las Skills cargadas .Agente de Testing: Generador autónomo de pruebas unitarias y de integración con JUnit 5 .

4. Skills Registry (Lógica Modular) La inteligencia del sistema reside en archivos Markdown dentro de .ai/skills/:skill-national.md: Cálculo base nacional.skill-international.md: Gestión de aduanas y factores de distancia global.skill-express.md: Reglas de prioridad para entregas en 24 horas.

5. Especificación de APIEndpoint: POST /api/v1/shipping/calculate-rateRequest Body:
{
  "weight": 10.5,           // en kg
  "origin": "Mendoza",      // Ciudad o Código Postal
  "destination": "Santiago", // Ciudad o Código Postal
  "shipmentType": "string"   // Valores: NATIONAL, INTERNATIONAL, EXPRESS
}
Response Body:
{
  "totalCost": 1500.50,
  "currency": "ARS / USD",
  "estimatedDeliveryDays": 1,
  "breakdown": {
    "baseRate": 1000.0,
    "distanceFee": 300.0,
    "customsTax": 200.5
  }
}

6. Reglas de Negocio (Core Logic)Módulo Nacional: TarifaBase + (Peso * FactorDistancia).Módulo Internacional: (TarifaBase + (Peso * FactorDistancia)) * 1.15 (15% de recargo aduanero obligatorio).Módulo Express: * Restricción: Solo disponible para envíos Nacionales.Garantía: Entrega en 24 horas.Recargo: 50% extra sobre el total del cálculo nacional.

7. Protocolo Human in the Loop (HITL) Para garantizar la calidad técnica y evitar alucinaciones:Punto de Aprobación 1: El agente debe presentar un plan de archivos y arquitectura antes de crear cualquier clase Java.Punto de Aprobación 2: Los tests generados deben ser validados por el humano antes de integrarse a la rama principal .Transparencia: El agente debe informar qué Skill específica está utilizando para cada bloque de código generado .