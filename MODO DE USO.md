# 🚀 MODO DE USO - Motor de Cálculo de Envíos

Este documento es una guía rápida para compilar, levantar y probar de forma manual la API de envíos.

## 1. Requisitos Previos

Asegúrate de tener instalados:
- **Java 21**: Puedes verificarlo ejecutando `java -version`.
- **Maven**: Las instrucciones abajo asumen que usas el *wrapper* de Maven incluido en el proyecto (`mvnw.cmd`), por lo que no es estrictamente necesario tener Maven instalado globalmente, pero es recomendado.

---

## 2. Compilar y Ejecutar Test

Para asegurar que todo el código esté correcto y funcionando, abre tu terminal (PowerShell o CMD) en la raíz del proyecto (`d:\Proyectitos\Motor de Cálculo de Envíos`) y ejecuta los tests:

```bash
# Compilar el proyecto
.\mvnw.cmd clean compile

# Ejecutar todos los tests (Unitarios e Integración)
.\mvnw.cmd test
```

---

## 3. Levantar la Aplicación

Para iniciar la API localmente, ejecuta el siguiente comando:

```bash
.\mvnw.cmd spring-boot:run
```

Verás los logs de Spring Boot en la terminal. Cuando veas `Started ShippingApplication in ...`, la API ya estará escuchando en el puerto **8080**.

---

## 4. Cómo Probar la API

Una vez levantada la aplicación, tienes varías vías para interactuar y enviar solicitudes:

### Opción A: Usando Swagger UI (Recomendado)

La forma más interactiva de probar los endpoints es a través de la interfaz web que levanta la propia aplicación:
1. Abre tu navegador web.
2. Ingresa a la URL: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
3. Despliega el controlador de *Shipping*, selecciona el endpoint **POST /api/v1/shipping/calculate-rate**, haz clic en **"Try it out"**, edita el JSON con la petición de prueba y dale a **Execute**.

### Opción B: Usando cURL (Terminal)

Desde otra pestaña de la terminal, puedes enviar un json directo con curl:

```bash
curl -X POST http://localhost:8080/api/v1/shipping/calculate-rate ^
-H "Content-Type: application/json" ^
-d "{ \"weight\": 10.5, \"origin\": \"Buenos Aires\", \"destination\": \"Córdoba\", \"shipmentType\": \"NATIONAL\" }"
```

### Opción C: Usando Postman / Insomnia

1. **Método**: `POST`
2. **URL**: `http://localhost:8080/api/v1/shipping/calculate-rate`
3. **Headers**: `Content-Type: application/json`
4. **Body** (raw -> JSON):
```json
{
  "weight": 10.5,
  "origin": "Buenos Aires",
  "destination": "Córdoba",
  "shipmentType": "NATIONAL"
}
```

### Tipos de envíos habilitados (`shipmentType`):
Al cambiar el "shipmentType" en el JSON variarás el algoritmo y la moneda generada:
- `"NATIONAL"`: Tarifa estándar de 5 días en ARS.
- `"INTERNATIONAL"`: Tarifa en USD con 15 días y cálculo de impuestos.
- `"EXPRESS"`: Recargo aplicado, ARS, con entrega rápida en 1 día.

---

## 5. Acceder a la Base de Datos (H2)

Si necesitas ver cómo trabaja la DB de persistencia o revisar registros:
1. URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
2. **JDBC URL**: `jdbc:h2:mem:shippingdb`
3. **Username**: `sa`
4. **Password**: *(dejar en blanco)*
