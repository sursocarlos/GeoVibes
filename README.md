# 🌍 GeoVibes App

GeoVibes es una aplicación Android moderna desarrollada con **Kotlin** y **Jetpack Compose** que combina la geolocalización con un tablón de avisos gestionado por roles.

El proyecto permite a los usuarios explorar un mapa interactivo y consultar avisos locales. Incluye autenticación con Firebase, diferenciando entre usuarios normales y administradores, permitiendo a estos últimos realizar operaciones **CRUD** completas.

---

## 📱 Funcionalidades Principales

### 1. Autenticación y Seguridad 🔐
- **Splash Screen** con logo y transición fluida.
- **Login y Registro** con validaciones en tiempo real:
  - Formato de email.
  - Longitud mínima de contraseña.
  - Confirmación de contraseña.
  - Nombre de usuario personalizado.
- Manejo de errores (red, credenciales incorrectas, intentos fallidos).
- **Persistencia de sesión** entre reinicios.

### 2. Mapa Interactivo (Home) 🗺️
- Integración con **Google Maps SDK** (Maps Compose).
- **Barra superior flotante** con avatar y nombre real del usuario.
- Ubicación inicial centrada en **Mairena del Aljarafe (Sevilla)**.
- Navegación directa al tablón de avisos y cierre de sesión.

### 3. Gestión de Avisos (Roles y Permisos) 📋
Sistema basado en Firebase Realtime Database:

#### 👤 Usuario Normal
- Puede ver la lista de avisos en tiempo real.
- Sin permisos de edición o creación.

#### 🛡️ Administrador
- **Crear** avisos (título, descripción, fecha automática).
- **Editar** avisos existentes.
- **Eliminar** avisos con confirmación (Toast).
- Acceso a botones flotantes y herramientas administrativas.

---

## 🚀 Instrucciones de Instalación y Configuración

Sigue estos pasos para ejecutar el proyecto en tu entorno local.

### 1. Clonar el repositorio

git clone [https://github.com/sursocarlos/GeoVibes.git](https://github.com/sursocarlos/GeoVibes.git)


## 2. Configurar Firebase
Descarga el archivo `google-services.json` desde tu consola de Firebase.
Colócalo en la siguiente ruta dentro del proyecto:

    app/google-services.json

## 3. Configurar Google Maps (local.properties)
Para que los mapas funcionen y para mantener segura la API Key, el proyecto lee la clave desde el archivo `local.properties`.
Abre o crea el archivo `local.properties` en la raíz del proyecto.
Añade tu clave de API de la siguiente manera:

    ## This file must *NOT* be checked into Version Control Systems,
    # as it contains information specific to your local configuration.
    sdk.dir=C:\Users\TU_USUARIO\AppData\Local\Android\Sdk
    # --- API KEY DE GOOGLE MAPS ---
    MAPS_API_KEY=AIzaSyTuClaveDeGoogleMapsAqui...

## 4. Compilar y Ejecutar
1. Abre el proyecto en Android Studio (versión Ladybug o superior recomendada).
2. Sincroniza el proyecto con Gradle (Sync Project with Gradle Files).
3. Ve a:
  - Build → Clean Project
  - Build → Rebuild Project
4. Ejecuta la app en un emulador o dispositivo físico.

## 🔐 Credenciales de Prueba
Puedes usar estas credenciales para probar los diferentes roles:

| Rol | Email | Contraseña | Permisos |
| :--- | :--- | :--- | :--- |
| Administrador | admin@admin.com | admin123 | Crear, Leer, Actualizar, Borrar |
| Usuario | user@test.com | 123456 | Solo Leer |

Nota: Los nuevos usuarios registrados desde la app tienen rol de usuario por defecto. Para ascender a un usuario a administrador, edita manualmente su campo role a admin en Firebase Realtime Database → users.

## 📸 Estructura del Proyecto

    /
    ├── viewmodel/       # Lógica de negocio (AuthViewModel, ElementsViewModel)
    ├── ui/
    │   ├── screens/     # Pantallas (Login, Register, Home, ElementList, ElementForm)
    │   └── theme/       # Tema y colores (TravelBlue)
    ├── model/           # Clases de datos (User, Elemento)
    └── components/      # Componentes UI reutilizables (GeoVibesTextField)