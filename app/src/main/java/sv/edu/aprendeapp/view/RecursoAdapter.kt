package sv.edu.aprendeapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import sv.edu.aprendeapp.R
import sv.edu.aprendeapp.databinding.ItemRecursoBinding
import sv.edu.aprendeapp.model.Recurso

class RecursoAdapter(
    private val esDocente: Boolean,
    private val onFavorito: (Recurso) -> Unit,
    private val onCalificar: (Recurso) -> Unit,
    private val onEditar: (Recurso) -> Unit,
    private val onEliminar: (Recurso) -> Unit,
    private val onAbrir: (Recurso) -> Unit
) : RecyclerView.Adapter<RecursoAdapter.VH>() {

    private var items = listOf<Recurso>()
    private var favoritos = setOf<String>()

    inner class VH(val b: ItemRecursoBinding) : RecyclerView.ViewHolder(b.root)

    @Suppress("NotifyDataSetChanged")
    fun submit(lista: List<Recurso>, favs: Set<String>) {
        items = lista; favoritos = favs; notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemRecursoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(h: VH, position: Int) {
        val r = items[position]
        val b = h.b
        val ctx = b.root.context
        b.tvTitulo.text = r.titulo
        b.tvTipo.text = r.tipo
        b.tvDescripcion.text = r.descripcion
        b.tvEnlace.text = r.enlace
        b.tvEnlace.setOnClickListener { onAbrir(r) }

        if (r.imagen.isNotBlank()) Picasso.get().load(r.imagen).into(b.ivImagen)
        else b.ivImagen.setImageDrawable(null)

        b.rbPromedio.rating = r.promedio
        b.tvRating.text = if (r.ratingVotos == 0) ctx.getString(R.string.sin_votos)
        else ctx.getString(R.string.rating_promedio, r.promedio, r.ratingVotos)

        // Estudiante: favorito y calificar. Docente: editar y eliminar.
        b.btnFavorito.visibility = if (esDocente) View.GONE else View.VISIBLE
        b.btnCalificar.visibility = if (esDocente) View.GONE else View.VISIBLE
        b.btnEditar.visibility = if (esDocente) View.VISIBLE else View.GONE
        b.btnEliminar.visibility = if (esDocente) View.VISIBLE else View.GONE

        val esFav = r.id in favoritos
        b.btnFavorito.setImageResource(
            if (esFav) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off)
        b.btnFavorito.setOnClickListener { onFavorito(r) }
        b.btnCalificar.setOnClickListener { onCalificar(r) }
        b.btnEditar.setOnClickListener { onEditar(r) }
        b.btnEliminar.setOnClickListener { onEliminar(r) }
    }
}
