package sv.edu.aprendeapp.model

data class Recurso(
    val id: String? = null,
    val titulo: String = "",
    val descripcion: String = "",
    val tipo: String = "",
    val enlace: String = "",
    val imagen: String = "",
    val ratingTotal: Int = 0,
    val ratingVotos: Int = 0
) {
    val promedio: Float get() = if (ratingVotos == 0) 0f else ratingTotal.toFloat() / ratingVotos
}
