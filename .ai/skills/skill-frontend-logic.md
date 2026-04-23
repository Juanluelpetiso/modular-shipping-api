---
name: frontend-logic
description: >
  Orquesta la interfaz de usuario del Motor de Cálculo de Envíos.
  Define la lógica de selectores dinámicos, inferencia de shipmentType,
  y validaciones de origen/destino antes de enviar al backend.
  Trigger: Al trabajar con archivos en src/main/resources/static/.
metadata:
  author: Juanluelpetiso
  version: "1.0"
---

## When to Use

Load this skill when:
- Se está creando o modificando la interfaz web del calculador de envíos.
- Se necesita entender la lógica de formularios dinámicos (origen/destino).
- Se está depurando la comunicación entre el frontend y el endpoint `POST /api/v1/shipping/calculate-rate`.

## Critical Patterns

### Pattern 1: Lógica Dinámica de Selectores de Origen

El selector de origen cambia su contenido según el tipo de envío seleccionado por el usuario:

```
Regla:
  SI tipo_origen == "Nacional":
      → Poblar selector de origen con las 24 Provincias Argentinas
      → Mostrar checkbox "Envío Express (24hs)"
      → shipmentType = EXPRESS si checkbox activo, sino NATIONAL

  SI tipo_origen == "Internacional":
      → Poblar selector de origen con países del continente Americano
      → Ocultar y desmarcar checkbox Express (no disponible)
      → shipmentType = INTERNATIONAL
```

### Pattern 2: Destino Bloqueado a Argentina

El país de destino está siempre fijo en Argentina. El usuario solo puede seleccionar
la provincia de destino entre las 24 provincias argentinas. Este selector nunca cambia
independientemente del tipo de origen seleccionado.

### Pattern 3: Inferencia Automática de shipmentType

El campo `shipmentType` del request JSON NO lo elige el usuario directamente.
Se infiere automáticamente con esta lógica:

```
SI tipo_origen == "Internacional"   → shipmentType = "INTERNATIONAL"
SI tipo_origen == "Nacional" Y express_checked → shipmentType = "EXPRESS"
SI tipo_origen == "Nacional" Y NOT express_checked → shipmentType = "NATIONAL"
```

### Pattern 4: Payload de Envío al Backend

```javascript
// Estructura del fetch al endpoint
fetch('/api/v1/shipping/calculate-rate', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
        weight: Number,        // Peso en kg, positivo
        origin: String,        // Nacional: provincia | Internacional: "Subdivisión, País"
        destination: String,   // Siempre una provincia argentina
        shipmentType: String   // "NATIONAL" | "INTERNATIONAL" | "EXPRESS"
    })
});
```

### Pattern 5: Selector Internacional de 2 Niveles

En modo Internacional, el origen se compone de dos selecciones cascada:

```
Al seleccionar un País en #origin-select:
  → Poblar #origin-subdivision con las subdivisiones del país
    (estados, regiones, departamentos según corresponda)
  → Mostrar el selector de subdivisión con animación
  → Al enviar: origin = "Subdivisión, País" (ej: "California, Estados Unidos")

Al cambiar de país:
  → Resetear y repoblar subdivisiones

Al volver a modo Nacional:
  → Ocultar selector de subdivisión
  → origin = provincia directamente
```

### Pattern 6: Manejo de Respuesta y Errores

```
Respuesta exitosa (200):
  → Mostrar: totalCost, currency, estimatedDeliveryDays
  → Mostrar desglose: baseRate, distanceFee, customsTax

Errores de la API:
  → 400: Validación fallida (campos vacíos, peso inválido, tipo no soportado)
  → 422: Error de negocio (ej: Express para envío internacional)
  → 500: Error interno del servidor
  → Mostrar mensaje descriptivo en panel de error visible
```

### Pattern 7: Datos Estáticos

**Provincias Argentinas (24):** Buenos Aires, CABA, Catamarca, Chaco, Chubut,
Córdoba, Corrientes, Entre Ríos, Formosa, Jujuy, La Pampa, La Rioja, Mendoza,
Misiones, Neuquén, Río Negro, Salta, San Juan, San Luis, Santa Cruz, Santa Fe,
Santiago del Estero, Tierra del Fuego, Tucumán.

**Países Americanos (origen internacional):** Lista completa de países del
continente americano (Norte, Centro y Sudamérica), excluyendo Argentina ya que
el destino siempre es Argentina.
