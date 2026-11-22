# Sistema de Gestión de Puntos de Reciclaje y Recompensa — Progreso 2

Proyecto de **ISWZ2102 - Programación III** (UDLA). Sistema en Java con interfaz gráfica Swing
(GUI Designer de IntelliJ) que aplica estructuras de datos y algoritmos sobre un programa de
reciclaje de Quito: los ciudadanos entregan material en los puntos limpios, ganan puntos y los
canjean por recompensas.

Docente: Ing. Bernarda Sandoval.

## Integrantes

| Integrante | Módulo |
|---|---|
| Sophia Pérez (líder) | Módulo 1 - Ciudadanos |
| Naomi Jaya | Módulo 2 - Entregas |
| Tabatha Flores | Módulo 3 - Recompensas |
| Génesis Barreiro | Módulo 4 - Canjes y reportes |
| Anderson Tocagón | Módulo 5 - Rutas de recolección |

## Alcance del Progreso 2

Módulos principales implementados por completo:

- **Módulo 2 - Entregas:** fila de atención con **cola** (`ArrayDeque`, FIFO) y deshacer la última
  entrega con **pila** (`ArrayDeque`, LIFO).
- **Módulo 4 - Canjes y reportes:** solicitudes atendidas con **cola de prioridad**
  (`PriorityQueue`) según el nivel del ciudadano (`SolicitudCanje` implementa `Comparable`).

Módulos de apoyo: el 1 (búsqueda binaria por cédula, ranking y filtros) y el 3 (catálogo con
`Comparable` y `Comparator`). El módulo 5 tiene el grafo (lista de adyacencia) con sus puntos y
tramos; la ruta más corta queda para la entrega final.

## Cómo ejecutar (IntelliJ IDEA)

1. Abrir la carpeta `SistemaReciclaje_Progreso2` como proyecto.
2. Si aparece "Cannot resolve symbol": clic derecho en `src` → **Mark Directory as** → **Sources Root**.
3. Abrir `src/interfaz/FormLogin.java` y ejecutar su `main`.

Los datos de prueba se cargan al iniciar (no hay base de datos), así que lo que se registre
durante la ejecución se pierde al cerrar el programa.

## Cuentas de prueba

| Rol | Usuario | Contraseña | Nivel inicial |
|---|---|---|---|
| Administrador (gestor del punto limpio) | `admin` | `admin1234` | - |
| Ciudadana | `gene_barreiro` | `gene1234` | PLATINO |
| Ciudadana | `taba_flores` | `taba1234` | ORO |
| Ciudadana | `nao_jaya` | `nao1234` | ORO |
| Ciudadana | `soph_perez` | `soph123` | PLATA |
| Ciudadano | `juan_castillo` | `juan123` | BRONCE |

## Qué puede hacer cada rol

**Administrador:** gestionar ciudadanos (consultar, ordenar, filtrar, dar de baja, restablecer
contraseña), asignar turnos, atender la fila y deshacer entregas, consultar y actualizar el
catálogo (puntos y stock), solicitar y procesar canjes, ver el reporte general y administrar el mapa.

**Ciudadano:** ver su perfil, tomar turno (ticket 0001, 0002...) y ver cuántas personas tiene
antes, consultar su historial e impacto ambiental, ver el catálogo y lo que puede canjear, solicitar
un canje y ver el estado de sus solicitudes (en espera, atendida o rechazada y el motivo).

Un canje tiene dos pasos: el ciudadano lo solicita (todavía no se descuentan puntos) y el
administrador procesa las solicitudes en orden de prioridad; en ese momento se descuentan los
puntos y baja el stock.

## Estructura

```
src/
├── modelo/     Usuario, Ciudadano, Administrador, Material, Entrega, Recompensa,
│               Canje, SolicitudCanje, Turno, PuntoRecoleccion, Tramo, Ruta
├── negocio/    GestorCiudadanos, GestorEntregas, GestorRecompensas, GestorCanjes,
│               GestorRutas y GestorReciclaje (fachada)
└── interfaz/   FormLogin, FormRegistro, FormPrincipal (.form + .java)
```

## Dónde está cada estructura y algoritmo

| Estructura / algoritmo | Clase y método |
|---|---|
| Cola (fila de atención) | `GestorEntregas.tomarTurno`, `atenderSiguiente` |
| Pila (deshacer) | `GestorEntregas.atenderSiguiente`, `deshacerUltimaEntrega` |
| Cola de prioridad + Comparable | `GestorCanjes.solicitarCanje`, `procesarSiguienteSolicitud`; `SolicitudCanje.compareTo` |
| Búsqueda binaria | `GestorCiudadanos.busquedaBinaria`, `GestorRecompensas.buscarPorId` |
| Ordenamiento (Merge Sort con `List.sort`) + Comparator | `GestorCiudadanos.listarOrdenados`, `GestorRecompensas.listarOrdenadas` |
| Grafo con lista de adyacencia | `GestorRutas.registrarPunto`, `conectarPuntos` |

## Nota sobre el GUI Designer

Cada `.form` es el diseño visual y su `.java` tiene los componentes (enlazados por nombre) y la
lógica. IntelliJ genera el código de la interfaz a partir del `.form` al compilar, por eso el
proyecto se ejecuta desde IntelliJ.
