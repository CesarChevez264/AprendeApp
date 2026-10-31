# MockAPI: recurso "recursos"

Campos del modelo (en MockAPI Resource -> Edit):
titulo (String), descripcion (String), tipo (String), enlace (String), imagen (String),
ratingTotal (Number), ratingVotos (Number)

Valores de "tipo" permitidos por la app: libro, video, articulo, tutorial
Deja ratingTotal = 0 y ratingVotos = 0 en todos.

Ejemplo de registro:
{
  "titulo": "Clean Code",
  "descripcion": "Libro sobre buenas prácticas para escribir código limpio y mantenible.",
  "tipo": "libro",
  "enlace": "https://www.oreilly.com/library/view/clean-code-a/9780136083238/",
  "imagen": "https://images.unsplash.com/photo-1512820790803-83ca734da794",
  "ratingTotal": 0,
  "ratingVotos": 0
}

Sugerencias de contenido real para llenar 8 registros:
- Kotlin Docs (tutorial), Android Developers Codelabs (tutorial)
- Clean Code (libro), The Pragmatic Programmer (libro)
- CS50 (video), Fireship Kotlin en 100 segundos (video)
- Artículo de Martin Fowler sobre MVC (articulo), Guía oficial de Retrofit (articulo)
