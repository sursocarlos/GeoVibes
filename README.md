# GeoVibes App

GeoVibes es un proyecto que tiene como propósito compartir lugares de interés en un mapa interactivo, facilitando a los usuarios encontrar miradores y sitios especiales que no aparecen en guías tradicionales. Está dirigida a viajeros, excursionistas y curiosos que disfrutan explorando.

Cuenta con **login**, **registro** y **pantalla principal (Home)** que muestra el email del usuario autenticado y permite cerrar sesión.

---

## Contenido del proyecto

- `app/` → código de la app (pantallas, navegación, ViewModel, Firebase, etc.)
- `build.gradle` y `settings.gradle` → configuración del proyecto
- `README.md` → este archivo
- **Nota:** El archivo `google-services.json` no está incluido en el repositorio por seguridad.  
  Para que funcione la app, se debe añadir localmente en `app/`.

---

## Requisitos

- Android Studio Bumblebee o superior
- Emulador o dispositivo Android con mínimo SDK 24
- Conexión a internet para Firebase
- Firebase configurado con Authentication (Email/Password)

---

## Instrucciones para ejecutar

1. Clona el proyecto desde GitHub:

bash
git clone https://github.com/sursocarlos/GeoVibes

Abre el proyecto en Android Studio.

Coloca tu archivo `google-services.json` dentro de `app/` (este archivo no se sube al repositorio).

Haz **Build → Clean Project** y luego **Build → Rebuild Project**.

Ejecuta la app en un emulador o dispositivo.

---

## Pantallas y funcionamiento

### 1. Login
- Muestra un formulario con Email y Contraseña.
- Valida que los campos no estén vacíos.
- Mensajes de error en español si:
    - El email está mal formateado.
    - La contraseña es incorrecta.
- Al iniciar sesión correctamente, redirige a **Home**.

### 2. Registro
- Formulario con Email y Contraseña.
- Valida:
    - Campos no vacíos.
    - Contraseña mínima de 6 caracteres.
- Permite crear usuarios en Firebase Authentication.
- Redirige automáticamente a **Home** al registrarse correctamente.
- En caso de error (correo ya usado, formato inválido, etc.) muestra un mensaje en español.

### 3. Home
- Muestra el email del usuario autenticado en la parte superior de la pantalla.
- Botón **Cerrar sesión** que vuelve a la pantalla de login.
- La sesión se mantiene incluso al cerrar y abrir la app.

---

## Demostración de funcionamiento
- Login vacío → muestra mensajes de error.
- Registro con formulario visible → permite crear usuario.
- Home → muestra el email del usuario y botón de cerrar sesión.
- Al cerrar sesión, vuelve a la pantalla de login.

---

## Notas
- Se ha usado **Firebase Authentication** para gestionar usuarios.
- La Base de datos de Firebase no es necesaria para esta práctica; solo se autentican usuarios.
- La app está desarrollada con **Jetpack Compose** y navegación por **NavController**.
- No se incluye el archivo `google-services.json` en el repositorio por seguridad.
