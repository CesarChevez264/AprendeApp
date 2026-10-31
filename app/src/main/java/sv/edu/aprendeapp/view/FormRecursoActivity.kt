package sv.edu.aprendeapp.view

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import sv.edu.aprendeapp.R
import sv.edu.aprendeapp.controller.RecursoController
import sv.edu.aprendeapp.databinding.ActivityFormRecursoBinding
import sv.edu.aprendeapp.model.Recurso
import sv.edu.aprendeapp.util.toast

class FormRecursoActivity : AppCompatActivity() {
    private lateinit var b: ActivityFormRecursoBinding
    private val ctrl = RecursoController()
    private var editando: Recurso? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityFormRecursoBinding.inflate(layoutInflater)
        setContentView(b.root)

        val json = intent.getStringExtra("recurso")
        if (json != null) {
            val r = Gson().fromJson(json, Recurso::class.java)
            editando = r
            b.tvTituloForm.setText(R.string.editar_recurso)
            b.etTitulo.setText(r.titulo)
            b.etDescripcion.setText(r.descripcion)
            b.etEnlace.setText(r.enlace)
            b.etImagen.setText(r.imagen)
            val tipos = resources.getStringArray(R.array.tipos_form)
            b.spTipo.setSelection(tipos.indexOf(r.tipo).coerceAtLeast(0))
        } else {
            b.tvTituloForm.setText(R.string.agregar_recurso)
        }
        b.btnGuardar.setOnClickListener { guardar() }
    }

    private fun guardar() {
        val titulo = b.etTitulo.text.toString().trim()
        val descripcion = b.etDescripcion.text.toString().trim()
        val enlace = b.etEnlace.text.toString().trim()
        val imagen = b.etImagen.text.toString().trim()
        listOf(b.tilTitulo, b.tilDescripcion, b.tilEnlace, b.tilImagen).forEach { it.error = null }

        var ok = true
        if (titulo.length < 3) { b.tilTitulo.error = getString(R.string.err_titulo); ok = false }
        if (descripcion.length < 10) { b.tilDescripcion.error = getString(R.string.err_descripcion); ok = false }
        if (!Patterns.WEB_URL.matcher(enlace).matches()) { b.tilEnlace.error = getString(R.string.err_enlace); ok = false }
        if (!Patterns.WEB_URL.matcher(imagen).matches()) { b.tilImagen.error = getString(R.string.err_imagen); ok = false }
        if (!ok) return

        val base = editando
        // Al editar se conservan los votos acumulados
        val recurso = Recurso(
            base?.id, titulo, descripcion, b.spTipo.selectedItem.toString(),
            enlace, imagen, base?.ratingTotal ?: 0, base?.ratingVotos ?: 0
        )
        b.btnGuardar.isEnabled = false
        b.progress.visibility = View.VISIBLE

        val onOk: () -> Unit = { toast(R.string.guardado_ok); finish() }
        val onError: (String) -> Unit = {
            b.btnGuardar.isEnabled = true
            b.progress.visibility = View.GONE
            toast(getString(R.string.err_red, it))
        }
        val id = base?.id
        if (id == null) ctrl.crear(recurso, onOk, onError) else ctrl.actualizar(id, recurso, onOk, onError)
    }
}
