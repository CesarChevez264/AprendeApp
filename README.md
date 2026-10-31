# AprendeApp

Sistema de gestión de recursos de aprendizaje (libros, videos, artículos y tutoriales de Ingeniería de Sistemas) para Android.
Tercer Desafío Práctico, Desarrollo de Software para Móviles, Universidad Don Bosco, ciclo 02-2026.

## Tecnologías
- Kotlin + Android Studio (Views XML, View Binding, Material Components)
- Arquitectura MVC
- Retrofit 2 + Gson + OkHttp para consumir la API REST (MockAPI.io)
- Firebase Authentication (correo y contraseña) y Realtime Database (roles, favoritos, calificaciones por usuario)
- Picasso para cargar imágenes

## Arquitectura MVC
| Capa | Paquete | Contenido |
|---|---|---|
| Model | `model` | `Recurso`, `Roles` |
| View | `view` | Activities, `RecursoAdapter` y layouts XML |
| Controller | `controller` | `AuthController`, `UsuarioController`, `RecursoController` |
| Soporte | `network`, `util` | `RetrofitClient`, `RecursoApi`, `PasswordValidator`, extensiones |

## Roles
- **Estudiante:** ver, buscar, filtrar, ordenar, marcar favoritos y calificar recursos (1 a 5 estrellas).
- **Docente:** pantalla especial para crear (POST), editar (PUT) y eliminar (DELETE) recursos.

## Validación de contraseña
Mínimo 12 caracteres, una mayúscula, una minúscula, un número y un carácter especial (`! @ # $ % ^ & *`).

## API
Base URL: `https://TU_ID.mockapi.io/api/v1/`

| Método | Ruta | Uso |
|---|---|---|
| GET | /recursos | Listar |
| GET | /recursos/{id} | Obtener uno |
| POST | /recursos | Crear |
| PUT | /recursos/{id} | Editar y actualizar calificación |
| DELETE | /recursos/{id} | Eliminar |

Ejemplo de JSON:
```json
{
  "id": "1",
  "titulo": "Clean Code",
  "descripcion": "Libro sobre buenas prácticas...",
  "tipo": "libro",
  "enlace": "https://...",
  "imagen": "https://...",
  "ratingTotal": 0,
  "ratingVotos": 0
}
```
El promedio mostrado a todos los usuarios es `ratingTotal / ratingVotos`.

## Funcionalidades
Registro y login con validación, logout, lista en tarjetas, búsqueda por ID, título o tipo, filtro por tipo, orden por título,
favoritos persistentes por usuario (Firebase), calificación con promedio global (API), CRUD completo para el docente,
confirmación antes de eliminar, loaders, toasts y diálogos. Todos los textos están en `strings.xml`.

## Cómo ejecutar
1. Clonar el repositorio y abrirlo en Android Studio.
2. Verificar que `app/google-services.json` esté presente.
3. Sincronizar Gradle y ejecutar en un emulador o dispositivo con Android 8.0 (API 26) o superior, con internet.

## Usuarios de prueba
| Rol | Correo | Contraseña |
|---|---|---|
| Estudiante | (completar) | (completar) |
| Docente | (completar) | (completar) |

## Capturas de pantalla
(agregar imágenes)

## Autor
(Tu nombre), UDB, 2026
