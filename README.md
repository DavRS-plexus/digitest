# Digitest
Bienvenido al mundo digital 😉

## API: DigiApi

He elegido una api pública sobre Digimon: "https://digi-api.com/", que cumple los siguientes criterios:
* Obtención de un listado de Digimon con su imagen y nombre: digi-api.com/api/v1/digimon
* Búsqueda por nombre: digi-api.com/api/v1/digimon?name=xxx
* Obtención del detalle de un Digimon: digi-api.com/api/v1/digimon/{id}

Decisiones tomadas de cara a simplificar la prueba técnica:
* La api cuenta con paginación pero he decidido obtener un número limitado de 20 resultados.
* La api tiene la posibilidad de buscar más información (aparte del nombre), pero la búsqueda, en la app, se ha limitado al nombre del Digimon.
* La api del detalle devuelve una gran cantidad de información pero la app muestra un número reducido de campos.

## Compilación y ejecución

### Requisitos

- Android Studio compatible con Android Gradle Plugin **9.5.2**.
- **JDK 21**, utilizado también en CI. En Android Studio, selecciónalo como Gradle JDK; para la terminal, configura `JAVA_HOME` con la ruta del JDK.
- Android SDK Platform **37.1** (`compileSdk = 37`, `compileSdkMinor = 1`), instalado desde SDK Manager.
- Un emulador o dispositivo con **Android 9 (API 28)** o superior.
- Conexión a Internet para descargar dependencias y consultar la API de Digimon.

El proyecto incluye el Gradle Wrapper, que descarga Gradle **9.9.1** automáticamente; no es necesario instalar Gradle por separado.

### Desde Android Studio

1. Clona el repositorio y abre su carpeta raíz, donde está `settings.gradle.kts`.
2. Configura el JDK y el SDK indicados y espera a que termine la sincronización de Gradle.
3. En **Build Variants**, selecciona `devDebug` para el módulo `app`.
4. Inicia un emulador o conecta un dispositivo con la depuración USB activada.
5. Selecciona la configuración de ejecución `app` y pulsa **Run**.

Android Studio configura la ruta del SDK en `local.properties`. Este archivo es local y no debe subirse al repositorio.

### Desde PowerShell (Windows)

Ejecuta los comandos desde la raíz del repositorio. Si no has abierto el proyecto en Android Studio, configura la ubicación del SDK mediante `ANDROID_HOME` o un archivo `local.properties` con una entrada como `sdk.dir=C:\\Android\\Sdk`, sustituyendo la ruta por la de tu instalación.

Para generar el APK de desarrollo:

```powershell
.\gradlew.bat :app:assembleDevDebug
```

El APK se genera en `app\build\outputs\apk\dev\debug\app-dev-debug.apk` y se firma automáticamente con la clave de depuración.

Para instalarlo en un emulador iniciado o un dispositivo conectado:

```powershell
.\gradlew.bat :app:installDevDebug
```

La instalación no abre la aplicación. Puedes abrirla desde el dispositivo o ejecutar lo siguiente si `adb` está disponible en el `PATH` (se encuentra en la carpeta `platform-tools` del SDK):

```powershell
adb shell am start -n com.plexus.example.digitest.dev/com.plexus.example.ui.MainActivity
```

## Manual de uso

La app es compatible para dispositivos Android 9 (api 28) y superior. 
Esto cubre el 97% de dispositivos (https://composables.com/android-distribution-chart) a fecha 22/11/2026.

La app consta de 2 secciones:

### Sección Home

Mensaje de bienvenida + botón de "Empezar" que navega a la sección del Buscador.

### Sección Buscador

Esta sección contiene 2 pantallas

#### Pantalla Listado

1. Al entrar a esta pantalla, se realiza una petición para obtener un listado inicial de hasta 20 Digimon (paginación limitada a 20 resultados). Esta petición nunca se cachea.
2. El usuario puede buscar "por nombre", introduciendo un texto en el buscador y haciendo click sobre la lupa del teclado o pantalla. Esta petición sí se cachea.
3. La app mostrará los Digimon con nombres parecidos al texto que haya escrito el usuario.
4. Si el usuario introduce una búsqueda en blanco, se realiza la misma petición que en el primer punto. Sin caché.

#### Pantalla Detalle

1. Se muestra la información más relevante del Digimon: Nombre, Nivel, Tipos, Atributos y Descripción.
2. El usuario no tiene interacción más allá de volver para atrás.
3. Esta petición no se cachea.

### Vídeos del uso

* Se adjunta un vídeo mostrando el uso básico:
https://github.com/user-attachments/assets/5bbb3caf-4472-4285-8b0c-c8463f855c97
* Se adjunta un vídeo mostrando el uso de la caché:
https://github.com/user-attachments/assets/1276abd4-5d00-4e86-a57c-3be1572c052f

## Funcionamiento de caché

* La caché únicamente se ha habilitado cuando se consume este api: digi-api.com/api/v1/digimon?name=xxx; es decir, cuando las búsquedas que hace el usuario contienen un texto.
* Si existe una respuesta cacheada para la búsqueda realizada, se devuelve sin realizar ninguna petición a la API. En caso contrario, se realiza la petición.
* Si la api devuelve una respuesta exitosa y parseable al modelo definido en la app, se realiza el guardado en la caché.
* La caché se realiza en memoria y tiene una caducidad de 5 minutos por entrada.
* La caché tiene un límite de 50 entradas y elimina la menos recientemente utilizada cuando se supera.
* Tiempo monotónico con `System.nanoTime()` de forma que no depende de la hora/fecha del dispositivo.
* Las entradas cuyo tiempo ha expirado se eliminan al intentar recuperarlas.
* El listado inicial y el detalle se consultan siempre de manera remota.

## Funcionamiento del buscador

* Si la consulta coincide con la última aceptada(y exitosa) no se ejecuta la búsqueda.
* Cuando una consulta falla, se limpia la referencia a la última búsqueda para permitir reintentar el mismo texto.
* Al enviar una consulta vacía, se recupera el listado inicial.
* Con cada petición diferente de búsqueda del usuario, se cancela el job de la anterior petición.

## Arquitectura
El proyecto utiliza una arquitectura multimódulo, organizada por funcionalidades y capas, siguiendo principios de Clean Architecture. La presentación utiliza MVVM con Jetpack Compose.

### Organización del proyecto

| Módulo | Responsabilidad |
|---|---|
| `app` | Punto de entrada, inicialización de dependencias, configuración de entornos y navegación principal. |
| `feature:home:ui` | Pantalla de inicio y navegación hacia Digivice. |
| `feature:digivice:ui` | Pantallas de búsqueda y detalle, ViewModels, estados y modelos de UI. |
| `feature:digivice:domain` | Casos de uso, modelos de dominio y contrato del repositorio. |
| `feature:digivice:data` | Implementación del repositorio, acceso a la API, caché y transformación de datos. |
| `core` | Componentes compartidos: red, almacenamiento, errores, navegación, recursos y tema visual. |
| `libs` | Utilidades reutilizables de Kotlin y Android. |
| `build-logic` | Plugins de convención para compartir la configuración de compilación y dependencias. |

#### Presentación (`ui`)

Construida con **Jetpack Compose**, se divide en:

- **NavGraph:** Grafo de navegación de un módulo que contiene la navegación interna a sus diferentes EntryPoints.
- **EntryPoints:** obtienen los ViewModels, observan el estado respetando el ciclo de vida y conectan los eventos con la navegación.
- **Screens:** representan la interfaz a partir del estado recibido y notifican las interacciones mediante callbacks.
- **ViewModels:** procesan las acciones del usuario, ejecutan casos de uso y actualizan el estado.
- **UiState y UiEvent:** separan el estado de la pantalla de eventos puntuales, como navegar al detalle.
- **Modelos y mappers:** adaptan los datos del dominio a las necesidades de presentación.

#### Dominio (`domain`)

Define las operaciones de la funcionalidad sin depender de Compose ni de las implementaciones de red y almacenamiento.

Incluye:

- `GetDigimonListUseCase`: obtener el listado inicial.
- `SearchDigimonUseCase`: buscar Digimon por nombre.
- `GetDigimonDetailUseCase`: consultar el detalle.
- `DigimonRepository`: contrato de acceso a los datos.
- Modelos de dominio compartidos entre los casos de uso y el repositorio.

#### Datos (`data`)

`DigimonRepositoryImpl` implementa el contrato del dominio y coordina las fuentes de datos:

- **Remota:** consulta la API mediante Retrofit.
- **Local:** almacena temporalmente resultados de búsquedas.
- **Mappers:** convierten DTO y modelos locales en modelos de dominio.

### Inyección de dependencias

La aplicación utiliza **Koin** y su plugin de compilador.

Cada funcionalidad registra sus dependencias en módulos propios. `appModule` reúne los módulos de presentación, dominio, datos y servicios compartidos. Koin se inicializa en la clase `App`.

### Navegación

La navegación utiliza **Navigation Compose con rutas tipadas**.

`MainNavHost` integra los grafos de Home y Digivice. El módulo `core:navigation` define las rutas de entrada y los identificadores de grafos compartidos, mientras que cada módulo mantiene sus navegaciones internas.

### Servicios compartidos

- **Red:** Retrofit, OkHttp y Gson, configurados en `core:network`.
- **Errores:** modelos y conversiones comunes en `core:error`.
- **Almacenamiento:** infraestructura de caché en `core:storage:data`.
- **Diseño:** tema, colores, tipografía y dimensiones en `core:theme`; recursos compartidos en `core:resources`.
- **Utilidades:** extensiones y el tipo `Either` en `libs`.

### Tests unitarios

- **JUnit Jupiter (versión 5):** definición y ejecución de pruebas mediante JUnit Platform.
- **MockK:** simulación de dependencias y verificación de llamadas.
- **kotlinx-coroutines-test:** control de corrutinas mediante `runTest`, `StandardTestDispatcher` y `advanceUntilIdle`.

Los tests siguen la estructura **Given / When / Then**.

## Extras

### Convention Plugins

La configuración compartida de Gradle se encuentra en **`build-logic`**.

Los **Convention Plugins** agrupan configuraciones y dependencias comunes para evitar repetirlas en cada módulo y mantener criterios de compilación consistentes. Esta agrupación unida al uso del catálogo de veriones(`libs.versions.toml`) posibilita que la configuración de las dependencias de los módulos sea extremadamente simple, reduciendo la duplicación y permitiendo modificar una configuración común desde un único lugar.

| Plugin | Responsabilidad |
|---|---|
| `config.android` | Configuración común para aplicaciones y librerías Android: SDK 37.1, SDK mínimo 28, generación de `BuildConfig` y runner de pruebas instrumentadas. |
| `config.compose` | Activa Compose y aplica su plugin de compilador a los módulos Android. |
| `config.kotlin` | Configura Kotlin/JVM y Java con destino JVM 17, dependencias de pruebas y ejecución con JUnit Platform. |
| `kotlin.library` | Aplica `config.kotlin` y añade corrutinas. |
| `kotlin.domain` | Extiende `kotlin.library` con Koin y su plugin de compilador. |
| `kotlin.data` | Extiende `kotlin.library` con Koin, anotaciones JVM, Retrofit, Gson y el convertidor Gson. |
| `android.dependencies` | Configura una librería Android con Koin, serialización, Navigation Compose y herramientas de pruebas unitarias. |
| `compose.dependencies` | Configura una librería Android con Compose, su BOM, dependencias visuales, integración con Koin y tooling para la variante `debug`. |

### Análisis estático con Detekt
El proyecto utiliza **Detekt** para detectar problemas de estilo, complejidad y mantenibilidad en el código.
La configuración compartida está en `config/detekt/detekt.yml`.

#### Ejecución local

Desde la raíz del proyecto:

```powershell
# Windows (PowerShell)
.\gradlew.bat detekt
```

```bash
# Linux / macOS
bash ./gradlew detekt
```

Los informes se generan en `build/reports/detekt` de cada módulo.

### Integración continua (CI)

La CI utiliza **GitHub Actions** y se ejecuta al abrir o actualizar una pull request. También puede lanzarse manualmente desde **Actions > Run workflow**.

| Workflow | Archivo | Comprobaciones |
|---|---|---|
| **Build** | `.github/workflows/build.yml` | Compila las variantes `debug` mediante `:app:assembleDebug`. |
| **Unit tests** | `.github/workflows/unit-tests.yml` | Ejecuta `detekt` y después las pruebas unitarias mediante `test`. |

Si falla la compilación, el análisis estático o alguna prueba, la comprobación correspondiente aparece como fallida en la pull request.

### Build Variants + Product Flavors

| Flavor | Identificador de aplicación | URL de la API |
|---|---|---|
| `dev` | `com.plexus.example.digitest.dev` | `https://digi-api.com/` |
| `pro` | `com.plexus.example.digitest` | `https://digi-api.com/` |

El sufijo `.dev` permite instalar las versiones de desarrollo y producción simultáneamente en el mismo dispositivo.

Actualmente, ambos entornos utilizan la misma API. La URL se genera como `BuildConfig.digiapiUrl` y se inyecta a través de Koin para configurar el módulo de red.

| Build Type | Características |
|---|---|
| `debug` | Depuración habilitada y firma automática de desarrollo. Sin minificación ni reducción de recursos. |
| `debugMinified` | Hereda la configuración de `release`, pero utiliza la firma de desarrollo para facilitar su instalación local. |
| `release` | Minificación del código y reducción de recursos habilitadas. Sin configuración de firma de distribución en el proyecto. |

El objetivo de `debugMinified` es comprobar el comportamiento de la aplicación con las optimizaciones de producción, sin necesitar una clave de distribución.

`matchingFallbacks` permite que `debugMinified` utilice la variante `debug` de los módulos dependientes que no disponen de ese tipo de compilación.

### Modo claro/oscuro

La aplicación adapta su tema a la configuración del dispositivo mediante `isSystemInDarkTheme()`. No dispone de un selector propio: utiliza el modo claro u oscuro elegido en Android.

La configuración se centraliza en `AppTheme`, dentro del módulo `core:theme`, y se aplica desde `MainActivity` a toda la interfaz Compose.
