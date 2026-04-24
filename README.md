# Smart Locker State Pattern

## 1. Nombre del proyecto

**Smart Locker State Pattern**

Proyecto académico que implementa el Patrón State en Java usando como caso de estudio un sistema de casillero inteligente para entrega y recogida de paquetes dentro de una universidad.

## 2. Descripción del caso de estudio

En una universidad, los estudiantes pueden recibir paquetes en casilleros inteligentes ubicados en puntos estratégicos del campus. El sistema debe controlar el ciclo de vida del casillero: disponible, reservado para un estudiante, paquete almacenado, listo para recoger, vencido o en mantenimiento.

El proyecto simula ese comportamiento con un backend en Java y una interfaz web sencilla para interactuar con el casillero desde el navegador.

## 3. Problema que resuelve

El sistema resuelve la necesidad de administrar correctamente las operaciones que puede realizar un casillero según su estado actual.

Por ejemplo:

- Un casillero disponible sí puede reservarse.
- Un casillero reservado sí puede almacenar un paquete.
- Un casillero con paquete listo sí puede entregar el paquete si el código es correcto.
- Un casillero en mantenimiento no debe permitir operaciones normales.

Si esta lógica se implementara con muchos `if/else` o `switch`, el código crecería rápidamente y sería más difícil de entender, mantener y explicar. El Patrón State permite organizar mejor ese comportamiento.

## 4. Por qué el Patrón State aplica en este caso

Este caso es apropiado para el Patrón State porque:

- El casillero cambia de comportamiento según su estado actual.
- Las mismas operaciones no siempre están permitidas.
- Cada transición debe ser clara y controlada.
- El sistema necesita evitar lógica condicional centralizada y compleja.

En este proyecto, el objeto `SmartLocker` delega su comportamiento al estado actual mediante la interfaz `LockerState`. Cada estado concreto decide qué operaciones permite, cuáles rechaza y hacia qué estado debe transicionar.

## 5. Estructura del proyecto

```text
SmartLockerStatePattern/
├── frontend/
│   └── index.html
├── src/
│   └── edu/
│       └── university/
│           └── locker/
│               ├── Main.java
│               ├── context/
│               │   └── SmartLocker.java
│               ├── model/
│               │   ├── OperationResult.java
│               │   └── PackageInfo.java
│               ├── server/
│               │   └── WebServer.java
│               ├── service/
│               │   ├── JsonResponseBuilder.java
│               │   ├── LockerAuditLogger.java
│               │   └── SecurityCodeGenerator.java
│               └── state/
│                   ├── AvailableState.java
│                   ├── LockerState.java
│                   ├── MaintenanceState.java
│                   ├── OverdueState.java
│                   ├── PackageStoredState.java
│                   ├── ReadyForPickupState.java
│                   └── ReservedState.java
└── README.md
```

## 6. Explicación de las clases principales

### Main

Es el punto de entrada del sistema. Crea las dependencias principales, instancia el objeto `SmartLocker`, crea el `WebServer` y arranca el servidor HTTP en el puerto `8080`.

### WebServer

Configura el servidor HTTP usando `com.sun.net.httpserver.HttpServer`. Sus responsabilidades son:

- Servir el archivo `frontend/index.html`.
- Exponer los endpoints REST básicos.
- Leer los datos enviados desde el frontend.
- Ejecutar operaciones sobre `SmartLocker`.
- Retornar respuestas JSON simples.

### SmartLocker

Es el **Context** del Patrón State. Mantiene:

- El estado actual del casillero.
- El nombre del estudiante.
- La información del paquete.
- El último mensaje mostrado al usuario.

Además, delega todas las operaciones al estado actual:

- `reserve(studentName)`
- `storePackage(packageCode)`
- `markReadyForPickup()`
- `pickupPackage(securityCode)`
- `markAsOverdue()`
- `startMaintenance()`
- `finishMaintenance()`
- `getStatus()`

### LockerState

Es la **interfaz State** del patrón. Define el contrato común que todos los estados deben implementar. Cada estado concreto decide cómo responder ante cada operación.

### Estados concretos

Son las clases que representan el comportamiento específico del casillero en cada etapa:

- `AvailableState`
- `ReservedState`
- `PackageStoredState`
- `ReadyForPickupState`
- `OverdueState`
- `MaintenanceState`

Cada una encapsula reglas de negocio y transiciones explícitas.

### PackageInfo

Modela la información del paquete almacenado en el casillero. Guarda:

- Código del paquete.
- Código de seguridad asociado.

### OperationResult

Representa el resultado de una operación del sistema. Incluye:

- Si la operación fue exitosa o no.
- El mensaje que se mostrará al usuario.

Sirve para unificar las respuestas del backend.

### LockerAuditLogger

Guarda un historial en memoria de las operaciones realizadas. Esto ayuda a fines académicos, depuración y seguimiento de cambios de estado.

### SecurityCodeGenerator

Genera un código de seguridad simple de 4 dígitos para la recogida del paquete.

### JsonResponseBuilder

Construye respuestas JSON manualmente sin usar librerías externas. Se usa para mantener el proyecto simple y cumplir la restricción académica de no depender de frameworks o bibliotecas adicionales.

### index.html

Es el frontend del proyecto. Permite:

- Consultar el estado actual del casillero.
- Reservarlo.
- Guardar un paquete.
- Marcarlo como listo para recoger.
- Recoger el paquete.
- Marcar el caso como vencido.
- Iniciar y finalizar mantenimiento.

También muestra visualmente el estado actual, el estudiante, el código del paquete, el código de seguridad y el historial de acciones.

## 7. Explicación de cada estado

### AvailableState

Representa el casillero disponible.

Permite:

- Reservar el casillero.
- Iniciar mantenimiento.

Rechaza:

- Guardar un paquete sin reserva.
- Recoger un paquete.

Transiciones principales:

- A `ReservedState` cuando se reserva.
- A `MaintenanceState` cuando inicia mantenimiento.

### ReservedState

Representa el casillero reservado para un estudiante.

Permite:

- Guardar un paquete.
- Iniciar mantenimiento si aún no hay paquete almacenado.

Rechaza:

- Reservar nuevamente.
- Recoger el paquete antes de que esté listo.

Transiciones principales:

- A `PackageStoredState` cuando se guarda el paquete.
- A `MaintenanceState` cuando entra a revisión.

### PackageStoredState

Representa que el paquete ya fue depositado en el casillero.

Permite:

- Marcar el paquete como listo para recoger.

Rechaza:

- Reservar nuevamente.
- Iniciar mantenimiento porque ya hay un paquete dentro.

Transición principal:

- A `ReadyForPickupState`.

### ReadyForPickupState

Representa que el paquete ya está listo para ser recogido por el estudiante.

Permite:

- Recoger el paquete con el código correcto.
- Marcar el paquete como vencido.

Rechaza:

- La recogida con un código incorrecto.

Transiciones principales:

- A `AvailableState` si el paquete se entrega correctamente.
- A `OverdueState` si se marca como vencido.

### OverdueState

Representa un paquete cuyo tiempo de recogida ya venció.

Permite:

- Iniciar mantenimiento para revisión manual.

Rechaza:

- La recogida normal desde la interfaz.

Transición principal:

- A `MaintenanceState`.

### MaintenanceState

Representa el casillero en mantenimiento.

Permite:

- Finalizar mantenimiento.

Rechaza:

- Operaciones normales como reservar, guardar, recoger o marcar vencimiento.

Transición principal:

- A `AvailableState` cuando termina el mantenimiento.

## 8. Flujo principal del sistema

El flujo académico más representativo es el siguiente:

1. El casillero inicia en estado `AvailableState`.
2. Un estudiante reserva el casillero.
3. El sistema genera un código de seguridad.
4. El operador guarda el paquete.
5. El paquete se marca como listo para recoger.
6. El estudiante ingresa el código de seguridad.
7. Si el código es correcto, el paquete se entrega y el casillero vuelve a estar disponible.

Flujos alternos:

- Si el código de seguridad es incorrecto, el sistema rechaza la recogida.
- Si el tiempo de recogida vence, el casillero pasa a `OverdueState`.
- Si se necesita revisión técnica o manual, pasa a `MaintenanceState`.

## 9. Ventajas del Patrón State aplicadas

### SRP

Cada clase tiene una responsabilidad clara:

- `SmartLocker` administra el contexto.
- Cada estado implementa su comportamiento específico.
- `WebServer` atiende HTTP.
- `JsonResponseBuilder` construye JSON.
- `SecurityCodeGenerator` genera códigos.

Esto hace el sistema más claro y fácil de explicar.

### OCP

El sistema está abierto a extensión y cerrado a modificación. Si se desea agregar un nuevo estado, por ejemplo `BlockedState`, se puede crear una nueva clase sin reescribir toda la lógica central del casillero.

### Eliminación de condicionales complejos

No existe un `switch` gigante ni una larga secuencia de `if/else` para controlar el estado del casillero. Cada estado conoce su comportamiento y responde por sí mismo.

### Transiciones explícitas

Las transiciones son directas y visibles en cada clase de estado. Esto facilita la lectura, la depuración y la explicación en clase.

## 10. Instrucciones para ejecutar en IntelliJ IDEA

1. Abrir IntelliJ IDEA.
2. Seleccionar **Open** y abrir la carpeta del proyecto `SmartLockerStatePattern`.
3. Verificar que el proyecto use **Java 17** o **Java 21**.
4. Esperar a que IntelliJ indexe los archivos.
5. Abrir la clase `src/edu/university/locker/Main.java`.
6. Ejecutar la clase `Main`.
7. Confirmar en consola que el servidor inició en `http://localhost:8080`.

## 11. Instrucciones para abrir el frontend

Con el proyecto ya ejecutándose:

1. Abrir un navegador web.
2. Ir a la dirección `http://localhost:8080`.
3. Se cargará el archivo `frontend/index.html`.
4. Usar los campos y botones para probar las operaciones del casillero.

## 12. Checklist de pruebas manuales

Usar esta lista para validar el comportamiento del sistema:

- [ ] Abrir `http://localhost:8080` y verificar que la interfaz carga correctamente.
- [ ] Presionar **Ver estado** y confirmar que el estado inicial es **Disponible**.
- [ ] Intentar reservar con nombre vacío y verificar el mensaje de validación.
- [ ] Reservar con un nombre válido y comprobar que el estado cambia a **Reservado**.
- [ ] Verificar que el sistema muestre un código de seguridad generado.
- [ ] Intentar reservar nuevamente y confirmar que la operación se rechaza.
- [ ] Intentar guardar paquete con código vacío y verificar el mensaje de validación.
- [ ] Guardar un paquete con un código válido y comprobar que el estado cambia a **Paquete almacenado**.
- [ ] Intentar iniciar mantenimiento con paquete almacenado y verificar que la operación se rechaza.
- [ ] Marcar el paquete como listo para recoger y comprobar que el estado cambia a **Listo para recoger**.
- [ ] Intentar recoger con código incorrecto y verificar el mensaje de error.
- [ ] Recoger con el código correcto y comprobar que el estado vuelve a **Disponible**.
- [ ] Reservar nuevamente, guardar paquete y marcar listo para recoger.
- [ ] Marcar el caso como vencido y comprobar que el estado cambia a **Vencido**.
- [ ] Intentar recoger un paquete vencido y verificar que el sistema lo rechaza.
- [ ] Iniciar mantenimiento desde estado vencido y comprobar que cambia a **Mantenimiento**.
- [ ] Intentar realizar operaciones normales en mantenimiento y verificar que se rechazan.
- [ ] Finalizar mantenimiento y comprobar que el casillero vuelve a **Disponible**.

## Conclusión

Este proyecto muestra una aplicación clara, simple y académica del Patrón State. El caso del casillero inteligente universitario permite explicar cómo un mismo objeto cambia su comportamiento según el estado en que se encuentra, manteniendo una arquitectura ordenada, fácil de mantener y adecuada para una exposición en clase.
