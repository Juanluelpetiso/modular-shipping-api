# 📦 Modular Shipping API

API REST modular para cálculo de tarifas de envío, construida con **Java 21**, **Spring Boot 3.4** y **Clean Architecture**.

## 🏗️ Arquitectura

El proyecto sigue **Clean Architecture** con el patrón **Strategy** para desacoplar los módulos de cálculo:

```
src/main/java/com/jretamales/shipping/
├── domain/          → Lógica de negocio pura (Strategy, Records, Exceptions)
├── application/     → Casos de uso (Service, Resolver)
└── infrastructure/  → Adaptadores (REST Controller, DTOs, Config)
```

### Módulos de Cálculo (Skills)

| Módulo | Fórmula | Moneda | Entrega |
|--------|---------|--------|---------|
| **Nacional** | `10000 + (peso × 5)` | ARS | 5 días |
| **Internacional** | `(20000 + (peso × 15)) × 1.25` | USD | 15 días |
| **Express** | `(20000 + (peso × 5)) × 1.50` | ARS | 1 día |

## 🚀 Requisitos

- **Java 21** (obligatorio)
- **Maven 3.9+**

## ▶️ Ejecución

```bash
# Compilar
mvn clean compile

# Ejecutar tests
mvn test

# Levantar la aplicación
mvn spring-boot:run
```

## 📡 Endpoint

### `POST /api/v1/shipping/calculate-rate`

**Request:**
```json
{
  "weight": 10.5,
  "origin": "Buenos Aires",
  "destination": "Córdoba",
  "shipmentType": "NATIONAL"
}
```

**Response:**
```json
{
  "totalCost": 10052.5,
  "currency": "ARS",
  "estimatedDeliveryDays": 5,
  "breakdown": {
    "baseRate": 10000.0,
    "distanceFee": 52.5,
    "customsTax": 0.0
  }
}
```

### Tipos de envío válidos
- `NATIONAL` — Envío nacional (Argentina)
- `INTERNATIONAL` — Envío internacional (incluye 25% recargo aduanero)
- `EXPRESS` — Entrega garantizada 24hs (solo nacional, 50% recargo)

## 📚 Documentación API

Swagger UI disponible en: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 🗄️ H2 Console

Consola de base de datos en memoria: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **JDBC URL:** `jdbc:h2:mem:shippingdb`
- **User:** `sa`
- **Password:** *(vacío)*

## 🧪 Testing

```bash
# Unit + Integration tests
mvn test

# Tests con reporte de cobertura (JaCoCo)
mvn verify
```

## 🛠️ Stack Tecnológico

- Java 21 (Virtual Threads)
- Spring Boot 3.4
- H2 Database (en memoria)
- Jakarta Bean Validation
- springdoc-openapi (Swagger UI)
- JUnit 5 + Mockito
- JaCoCo (cobertura)
