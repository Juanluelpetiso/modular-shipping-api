/* ══════════════════════════════════════════════════════════════
   Motor de Cálculo de Envíos — Application Logic
   Skill: skill-frontend-logic.md
   Endpoint: POST /api/v1/shipping/calculate-rate
   ══════════════════════════════════════════════════════════════ */

'use strict';

// ── Datos Estáticos ──

const PROVINCIAS_ARGENTINAS = [
  'Buenos Aires', 'CABA', 'Catamarca', 'Chaco', 'Chubut',
  'Córdoba', 'Corrientes', 'Entre Ríos', 'Formosa', 'Jujuy',
  'La Pampa', 'La Rioja', 'Mendoza', 'Misiones', 'Neuquén',
  'Río Negro', 'Salta', 'San Juan', 'San Luis', 'Santa Cruz',
  'Santa Fe', 'Santiago del Estero', 'Tierra del Fuego', 'Tucumán'
];

// Países del continente americano (excluye Argentina — siempre es destino)
const PAISES_AMERICANOS = [
  'Belice', 'Bolivia', 'Brasil', 'Canadá', 'Chile',
  'Colombia', 'Costa Rica', 'Cuba', 'Ecuador', 'El Salvador',
  'Estados Unidos', 'Guatemala', 'Guyana', 'Haití', 'Honduras',
  'Jamaica', 'México', 'Nicaragua', 'Panamá', 'Paraguay',
  'Perú', 'República Dominicana', 'Surinam',
  'Trinidad y Tobago', 'Uruguay', 'Venezuela'
];

// Subdivisiones por país — selector de segundo nivel (solo modo Internacional)
const SUBDIVISIONES_POR_PAIS = {
  'Estados Unidos': [
    'Alabama','Alaska','Arizona','Arkansas','California','Colorado','Connecticut',
    'Delaware','Florida','Georgia','Hawaii','Idaho','Illinois','Indiana','Iowa',
    'Kansas','Kentucky','Louisiana','Maine','Maryland','Massachusetts','Michigan',
    'Minnesota','Mississippi','Missouri','Montana','Nebraska','Nevada',
    'New Hampshire','New Jersey','New Mexico','New York','North Carolina',
    'North Dakota','Ohio','Oklahoma','Oregon','Pennsylvania','Rhode Island',
    'South Carolina','South Dakota','Tennessee','Texas','Utah','Vermont',
    'Virginia','Washington','West Virginia','Wisconsin','Wyoming'
  ],
  'México': [
    'Aguascalientes','Baja California','Baja California Sur','Campeche','Chiapas',
    'Chihuahua','Ciudad de México','Coahuila','Colima','Durango','Guanajuato',
    'Guerrero','Hidalgo','Jalisco','México','Michoacán','Morelos','Nayarit',
    'Nuevo León','Oaxaca','Puebla','Querétaro','Quintana Roo','San Luis Potosí',
    'Sinaloa','Sonora','Tabasco','Tamaulipas','Tlaxcala','Veracruz','Yucatán','Zacatecas'
  ],
  'Canadá': [
    'Alberta','British Columbia','Manitoba','New Brunswick','Newfoundland and Labrador',
    'Northwest Territories','Nova Scotia','Nunavut','Ontario','Prince Edward Island',
    'Quebec','Saskatchewan','Yukon'
  ],
  'Brasil': [
    'Acre','Alagoas','Amapá','Amazonas','Bahía','Ceará','Distrito Federal',
    'Espírito Santo','Goiás','Maranhão','Mato Grosso','Mato Grosso do Sul',
    'Minas Gerais','Pará','Paraíba','Paraná','Pernambuco','Piauí',
    'Rio de Janeiro','Rio Grande do Norte','Rio Grande do Sul','Rondônia',
    'Roraima','Santa Catarina','São Paulo','Sergipe','Tocantins'
  ],
  'Chile': [
    'Arica y Parinacota','Tarapacá','Antofagasta','Atacama','Coquimbo',
    'Valparaíso','Metropolitana de Santiago','O\'Higgins','Maule','Ñuble',
    'Biobío','La Araucanía','Los Ríos','Los Lagos','Aysén','Magallanes'
  ],
  'Colombia': [
    'Amazonas','Antioquia','Arauca','Atlántico','Bogotá D.C.','Bolívar','Boyacá',
    'Caldas','Caquetá','Casanare','Cauca','Cesar','Chocó','Córdoba','Cundinamarca',
    'Guainía','Guaviare','Huila','La Guajira','Magdalena','Meta','Nariño',
    'Norte de Santander','Putumayo','Quindío','Risaralda','San Andrés','Santander',
    'Sucre','Tolima','Valle del Cauca','Vaupés','Vichada'
  ],
  'Perú': [
    'Amazonas','Áncash','Apurímac','Arequipa','Ayacucho','Cajamarca','Callao',
    'Cusco','Huancavelica','Huánuco','Ica','Junín','La Libertad','Lambayeque',
    'Lima','Loreto','Madre de Dios','Moquegua','Pasco','Piura','Puno',
    'San Martín','Tacna','Tumbes','Ucayali'
  ],
  'Uruguay': [
    'Artigas','Canelones','Cerro Largo','Colonia','Durazno','Flores',
    'Florida','Lavalleja','Maldonado','Montevideo','Paysandú','Río Negro',
    'Rivera','Rocha','Salto','San José','Soriano','Tacuarembó','Treinta y Tres'
  ],
  'Paraguay': [
    'Alto Paraguay','Alto Paraná','Amambay','Asunción','Boquerón','Caaguazú',
    'Caazapá','Canindeyú','Central','Concepción','Cordillera','Guairá',
    'Itapúa','Misiones','Ñeembucú','Paraguarí','Presidente Hayes','San Pedro'
  ],
  'Bolivia': [
    'Beni','Chuquisaca','Cochabamba','La Paz','Oruro',
    'Pando','Potosí','Santa Cruz','Tarija'
  ],
  'Ecuador': [
    'Azuay','Bolívar','Cañar','Carchi','Chimborazo','Cotopaxi','El Oro',
    'Esmeraldas','Galápagos','Guayas','Imbabura','Loja','Los Ríos','Manabí',
    'Morona Santiago','Napo','Orellana','Pastaza','Pichincha','Santa Elena',
    'Santo Domingo de los Tsáchilas','Sucumbíos','Tungurahua','Zamora-Chinchipe'
  ],
  'Venezuela': [
    'Amazonas','Anzoátegui','Apure','Aragua','Barinas','Bolívar','Carabobo',
    'Cojedes','Delta Amacuro','Distrito Capital','Falcón','Guárico','Lara',
    'Mérida','Miranda','Monagas','Nueva Esparta','Portuguesa','Sucre',
    'Táchira','Trujillo','Vargas','Yaracuy','Zulia'
  ],
  'Costa Rica': [
    'Alajuela','Cartago','Guanacaste','Heredia','Limón','Puntarenas','San José'
  ],
  'Panamá': [
    'Bocas del Toro','Chiriquí','Coclé','Colón','Darién','Herrera',
    'Los Santos','Panamá','Panamá Oeste','Veraguas'
  ],
  'Guatemala': [
    'Alta Verapaz','Baja Verapaz','Chimaltenango','Chiquimula','El Progreso',
    'Escuintla','Guatemala','Huehuetenango','Izabal','Jalapa','Jutiapa',
    'Petén','Quetzaltenango','Quiché','Retalhuleu','Sacatepéquez',
    'San Marcos','Santa Rosa','Sololá','Suchitepéquez','Totonicapán','Zacapa'
  ],
  'Honduras': [
    'Atlántida','Choluteca','Colón','Comayagua','Copán','Cortés',
    'El Paraíso','Francisco Morazán','Gracias a Dios','Intibucá',
    'Islas de la Bahía','La Paz','Lempira','Ocotepeque','Olancho',
    'Santa Bárbara','Valle','Yoro'
  ],
  'Nicaragua': [
    'Boaco','Carazo','Chinandega','Chontales','Costa Caribe Norte',
    'Costa Caribe Sur','Estelí','Granada','Jinotega','León',
    'Madriz','Managua','Masaya','Matagalpa','Nueva Segovia',
    'Río San Juan','Rivas'
  ],
  'El Salvador': [
    'Ahuachapán','Cabañas','Chalatenango','Cuscatlán','La Libertad',
    'La Paz','La Unión','Morazán','San Miguel','San Salvador',
    'San Vicente','Santa Ana','Sonsonate','Usulután'
  ],
  'Cuba': [
    'Artemisa','Camagüey','Ciego de Ávila','Cienfuegos','Granma',
    'Guantánamo','Holguín','Isla de la Juventud','La Habana',
    'Las Tunas','Matanzas','Mayabeque','Pinar del Río',
    'Sancti Spíritus','Santiago de Cuba','Villa Clara'
  ],
  'República Dominicana': [
    'Azua','Bahoruco','Barahona','Dajabón','Distrito Nacional','Duarte',
    'El Seibo','Elías Piña','Espaillat','Hato Mayor','Hermanas Mirabal',
    'Independencia','La Altagracia','La Romana','La Vega','María Trinidad Sánchez',
    'Monseñor Nouel','Monte Cristi','Monte Plata','Pedernales','Peravia',
    'Puerto Plata','Samaná','San Cristóbal','San José de Ocoa',
    'San Juan','San Pedro de Macorís','Sánchez Ramírez',
    'Santiago','Santiago Rodríguez','Santo Domingo','Valverde'
  ],
  'Haití': [
    'Artibonite','Centre','Grand\'Anse','Nippes','Nord',
    'Nord-Est','Nord-Ouest','Ouest','Sud','Sud-Est'
  ],
  'Jamaica': [
    'Clarendon','Hanover','Kingston','Manchester','Portland',
    'Saint Andrew','Saint Ann','Saint Catherine','Saint Elizabeth',
    'Saint James','Saint Mary','Saint Thomas','Trelawny','Westmoreland'
  ],
  'Trinidad y Tobago': [
    'Arima','Chaguanas','Couva-Tabaquite-Talparo','Diego Martin',
    'Mayaro-Rio Claro','Penal-Debe','Point Fortin','Port of Spain',
    'Princes Town','San Fernando','San Juan-Laventille',
    'Sangre Grande','Siparia','Tobago','Tunapuna-Piarco'
  ],
  'Guyana': [
    'Barima-Waini','Cuyuni-Mazaruni','Demerara-Mahaica',
    'East Berbice-Corentyne','Essequibo Islands-West Demerara',
    'Mahaica-Berbice','Pomeroon-Supenaam','Potaro-Siparuni',
    'Upper Demerara-Berbice','Upper Takutu-Upper Essequibo'
  ],
  'Surinam': [
    'Brokopondo','Commewijne','Coronie','Marowijne','Nickerie',
    'Para','Paramaribo','Saramacca','Sipaliwini','Wanica'
  ],
  'Belice': [
    'Belice','Cayo','Corozal','Orange Walk','Stann Creek','Toledo'
  ]
};

const API_URL = '/api/v1/shipping/calculate-rate';

// ── DOM References ──

const form               = document.getElementById('shipping-form');
const originTypeRadios   = document.querySelectorAll('input[name="origin-type"]');
const expressWrapper     = document.getElementById('express-wrapper');
const expressCheckbox    = document.getElementById('express-check');
const originSelect       = document.getElementById('origin-select');
const subdivisionWrapper = document.getElementById('subdivision-wrapper');
const subdivisionSelect  = document.getElementById('origin-subdivision');
const destinationSelect  = document.getElementById('destination-select');
const weightInput        = document.getElementById('weight-input');
const submitBtn          = document.getElementById('submit-btn');
const resultPanel        = document.getElementById('result-panel');
const errorPanel         = document.getElementById('error-panel');

// Result DOM
const resultCost     = document.getElementById('result-cost');
const resultCurrency = document.getElementById('result-currency');
const resultDays     = document.getElementById('result-days');
const resultType     = document.getElementById('result-type');
const breakdownBase  = document.getElementById('breakdown-base');
const breakdownDist  = document.getElementById('breakdown-dist');
const breakdownTax   = document.getElementById('breakdown-tax');

// Error DOM
const errorTitle   = document.getElementById('error-title');
const errorMessage = document.getElementById('error-message');

// ── State ──

let currentOriginType = 'national';

// ── Initialization ──

document.addEventListener('DOMContentLoaded', () => {
  populateDestination();
  populateOrigin('national');
  bindEvents();
});

// ── Populate Selectors ──

/**
 * Llena un <select> con opciones a partir de un array de strings.
 */
function populateSelect(selectEl, items, placeholder) {
  selectEl.innerHTML = '';

  const defaultOpt = document.createElement('option');
  defaultOpt.value = '';
  defaultOpt.disabled = true;
  defaultOpt.selected = true;
  defaultOpt.textContent = placeholder;
  selectEl.appendChild(defaultOpt);

  items.forEach(item => {
    const opt = document.createElement('option');
    opt.value = item;
    opt.textContent = item;
    selectEl.appendChild(opt);
  });
}

/**
 * Destino: siempre provincias argentinas (spec §7 — destino bloqueado a Argentina).
 */
function populateDestination() {
  populateSelect(destinationSelect, PROVINCIAS_ARGENTINAS, 'Seleccioná una provincia');
}

/**
 * Origen: cambia según tipo de envío (spec §7 — lógica de origen).
 * @param {'national'|'international'} type
 */
function populateOrigin(type) {
  if (type === 'national') {
    populateSelect(originSelect, PROVINCIAS_ARGENTINAS, 'Seleccioná una provincia');
  } else {
    populateSelect(originSelect, PAISES_AMERICANOS, 'Seleccioná un país');
  }
}

/**
 * Puebla el selector de subdivisión según el país seleccionado.
 * Skill: skill-frontend-logic.md — Selector de 2 niveles.
 */
function populateSubdivision(country) {
  const subdivisions = SUBDIVISIONES_POR_PAIS[country];
  if (subdivisions && subdivisions.length > 0) {
    populateSelect(subdivisionSelect, subdivisions, 'Seleccioná estado/región');
    subdivisionWrapper.classList.add('visible');
  } else {
    subdivisionWrapper.classList.remove('visible');
    subdivisionSelect.innerHTML = '<option value="" disabled selected>Sin subdivisiones</option>';
  }
}

// ── Event Binding ──

function bindEvents() {
  // Cambio de tipo de origen → actualizar selector y visibilidad de express
  originTypeRadios.forEach(radio => {
    radio.addEventListener('change', handleOriginTypeChange);
  });

  // Cambio de país (internacional) → poblar subdivisiones
  originSelect.addEventListener('change', handleOriginSelectChange);

  // Submit del formulario
  form.addEventListener('submit', handleSubmit);
}

/**
 * Maneja el cambio de tipo de origen (Nacional / Internacional).
 * Skill: skill-frontend-logic.md — Pattern 1.
 */
function handleOriginTypeChange(e) {
  const type = e.target.value; // 'national' | 'international'
  currentOriginType = type;

  populateOrigin(type);

  if (type === 'national') {
    expressWrapper.classList.add('visible');
    // Ocultar subdivisión (solo aplica a internacional)
    subdivisionWrapper.classList.remove('visible');
  } else {
    expressWrapper.classList.remove('visible');
    expressCheckbox.checked = false;
    // Resetear subdivisión hasta que elijan un país
    subdivisionWrapper.classList.remove('visible');
    subdivisionSelect.innerHTML = '<option value="" disabled selected>Primero seleccioná un país</option>';
  }

  // Ocultar paneles anteriores
  hideResult();
  hideError();
}

/**
 * Maneja el cambio en el selector de origen.
 * En modo internacional, puebla las subdivisiones del país seleccionado.
 */
function handleOriginSelectChange() {
  if (currentOriginType === 'international' && originSelect.value) {
    populateSubdivision(originSelect.value);
  }
}

// ── Shipment Type Inference ──

/**
 * Infiere el shipmentType según el estado actual del formulario.
 * Skill: skill-frontend-logic.md — Pattern 3.
 * @returns {'NATIONAL'|'INTERNATIONAL'|'EXPRESS'}
 */
function inferShipmentType() {
  if (currentOriginType === 'international') {
    return 'INTERNATIONAL';
  }
  return expressCheckbox.checked ? 'EXPRESS' : 'NATIONAL';
}

/**
 * Construye el valor de "origin" para el payload.
 * Nacional: provincia directamente.
 * Internacional: "Subdivisión, País" (ej: "California, Estados Unidos").
 */
function buildOriginValue() {
  if (currentOriginType === 'national') {
    return originSelect.value;
  }
  // Internacional: concatenar subdivisión + país
  const country = originSelect.value;
  const subdivision = subdivisionSelect.value;
  if (subdivision) {
    return `${subdivision}, ${country}`;
  }
  return country; // Fallback si no hay subdivisión
}

// ── Form Submission ──

/**
 * Maneja el submit del formulario.
 * Skill: skill-frontend-logic.md — Pattern 4.
 */
async function handleSubmit(e) {
  e.preventDefault();

  // Validación básica del lado del cliente
  if (!originSelect.value || !destinationSelect.value || !weightInput.value) {
    showError('Campos incompletos', 'Por favor completá todos los campos antes de calcular.');
    return;
  }

  // En modo internacional, validar que se seleccionó subdivisión
  if (currentOriginType === 'international') {
    const subdivisions = SUBDIVISIONES_POR_PAIS[originSelect.value];
    if (subdivisions && subdivisions.length > 0 && !subdivisionSelect.value) {
      showError('Campos incompletos', 'Seleccioná el estado o región de origen.');
      return;
    }
  }

  const weight = parseFloat(weightInput.value);
  if (isNaN(weight) || weight <= 0) {
    showError('Peso inválido', 'El peso debe ser un número mayor a 0.');
    return;
  }

  const shipmentType = inferShipmentType();

  const payload = {
    weight: weight,
    origin: buildOriginValue(),
    destination: destinationSelect.value,
    shipmentType: shipmentType
  };

  await sendRequest(payload);
}

/**
 * Envía el request al backend y maneja la respuesta.
 * Skill: skill-frontend-logic.md — Patterns 4 & 5.
 */
async function sendRequest(payload) {
  setLoading(true);
  hideResult();
  hideError();

  try {
    const response = await fetch(API_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => null);
      handleApiError(response.status, errorData);
      return;
    }

    const data = await response.json();
    showResult(data, payload.shipmentType);

  } catch (err) {
    showError(
      'Error de conexión',
      'No se pudo conectar con el servidor. Verificá que la API esté corriendo en el puerto 8080.'
    );
  } finally {
    setLoading(false);
  }
}

// ── UI Updates ──

/**
 * Muestra el panel de resultados con los datos de la API.
 */
function showResult(data, shipmentType) {
  // Costo total formateado
  resultCost.textContent = formatCurrency(data.totalCost);
  resultCurrency.textContent = data.currency;

  // Días estimados
  const daysText = data.estimatedDeliveryDays === 1
    ? '1 día hábil'
    : `${data.estimatedDeliveryDays} días hábiles`;
  resultDays.textContent = `🕐 ${daysText}`;

  // Tipo de envío
  const typeLabels = {
    'NATIONAL': '🇦🇷 Nacional',
    'INTERNATIONAL': '🌎 Internacional',
    'EXPRESS': '⚡ Express 24hs'
  };
  resultType.textContent = typeLabels[shipmentType] || shipmentType;

  // Desglose
  breakdownBase.textContent = formatCurrency(data.breakdown.baseRate);
  breakdownDist.textContent = formatCurrency(data.breakdown.distanceFee);
  breakdownTax.textContent = formatCurrency(data.breakdown.customsTax);

  resultPanel.classList.add('visible');
  resultPanel.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

function hideResult() {
  resultPanel.classList.remove('visible');
}

/**
 * Maneja errores de la API según el status code.
 * Skill: skill-frontend-logic.md — Pattern 5.
 */
function handleApiError(status, errorData) {
  let title, message;

  switch (status) {
    case 400:
      title = 'Datos inválidos';
      if (errorData?.details) {
        const fieldErrors = Object.entries(errorData.details)
          .map(([field, msg]) => `• ${field}: ${msg}`)
          .join('\n');
        message = fieldErrors;
      } else {
        message = errorData?.error || 'Verificá que todos los campos estén completos y sean válidos.';
      }
      break;
    case 422:
      title = 'Error de negocio';
      message = errorData?.error || 'La operación solicitada no es válida.';
      break;
    case 500:
      title = 'Error interno';
      message = 'Ocurrió un error inesperado en el servidor. Intentá nuevamente.';
      break;
    default:
      title = `Error ${status}`;
      message = errorData?.error || 'Ocurrió un error inesperado.';
  }

  showError(title, message);
}

function showError(title, message) {
  errorTitle.textContent = title;
  errorMessage.textContent = message;
  errorPanel.classList.add('visible');

  // Auto-ocultar después de 8 segundos
  if (showError._timeout) clearTimeout(showError._timeout);
  showError._timeout = setTimeout(hideError, 8000);
}

function hideError() {
  errorPanel.classList.remove('visible');
}

function setLoading(isLoading) {
  submitBtn.disabled = isLoading;
  if (isLoading) {
    submitBtn.classList.add('loading');
  } else {
    submitBtn.classList.remove('loading');
  }
}

// ── Utilities ──

/**
 * Formatea un número como moneda con separador de miles argentino.
 */
function formatCurrency(value) {
  if (value === 0) return '$0,00';
  return '$' + value.toLocaleString('es-AR', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  });
}
