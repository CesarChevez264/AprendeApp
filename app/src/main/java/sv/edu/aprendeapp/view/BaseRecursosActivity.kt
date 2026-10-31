package sv.edu.aprendeapp.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import sv.edu.aprendeapp.R
import sv.edu.aprendeapp.controller.AuthController
import sv.edu.aprendeapp.controller.RecursoController
import sv.edu.aprendeapp.controller.UsuarioController
import sv.edu.aprendeapp.databinding.ActivityRecursosBinding
import sv.edu.aprendeapp.model.Recurso
import sv.edu.aprendeapp.util.toast

abstract class BaseRecursosActivity : AppCompatActivity() {
    abstract val esDocente: Boolean

    private lateinit var b: ActivityRecursosBinding
    private lateinit var adapter: RecursoAdapter
    private val recursoCtrl = RecursoController()
    private val usuarioCtrl = UsuarioController()
    private val authCtrl = AuthController()

    private var todos = listOf<Recurso>()
    private var favoritos = setOf<String>()
    private var soloFavoritos = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityRecursosBinding.inflate(layoutInflater)
        setContentView(b.root)
        setSupportActionBar(b.toolbar)

        adapter = RecursoAdapter(
            esDocente,
            onFavorito = { toggleFavorito(it) },
            onCalificar = { dialogoCalificar(it) },
            onEditar = { abrirForm(it) },
            onEliminar = { confirmarEliminar(it) },
            onAbrir = { abrirEnlace(it) }
        )
        b.rvRecursos.layoutManager = LinearLayoutManager(this)
        b.rvRecursos.adapter = adapter

        b.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?) = false
            override fun onQueryTextChange(q: String?): Boolean { aplicarFiltros(); return true }
        })
        b.spTipo.onChange { aplicarFiltros() }
        b.spOrden.onChange { aplicarFiltros() }

        b.fabAgregar.visibility = if (esDocente) View.VISIBLE else View.GONE
        b.fabAgregar.setOnClickListener { abrirForm(null) }
    }

    override fun onResume() {
        super.onResume()
        cargar()
    }

    // Carga de datos
    private fun cargar() {
        b.progress.visibility = View.VISIBLE
        val traerRecursos = {
            recursoCtrl.listar(
                onOk = { todos = it; b.progress.visibility = View.GONE; aplicarFiltros() },
                onError = { b.progress.visibility = View.GONE; toast(getString(R.string.err_red, it)) }
            )
        }
        if (esDocente) traerRecursos()
        else usuarioCtrl.favoritos { favoritos = it; traerRecursos() }
    }

    // Búsqueda, filtro y orden
    private fun aplicarFiltros() {
        var lista = todos
        if (soloFavoritos) lista = lista.filter { it.id in favoritos }
        if (b.spTipo.selectedItemPosition != 0) {
            val tipo = b.spTipo.selectedItem.toString()
            lista = lista.filter { it.tipo.equals(tipo, ignoreCase = true) }
        }
        val q = b.searchView.query.toString().trim()
        if (q.isNotEmpty()) {
            lista = lista.filter {
                it.id == q || it.titulo.contains(q, true) || it.tipo.contains(q, true)
            }
        }
        lista = if (b.spOrden.selectedItemPosition == 0) lista.sortedBy { it.titulo.lowercase() }
        else lista.sortedByDescending { it.titulo.lowercase() }

        adapter.submit(lista, favoritos)
        b.tvVacio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
    }

    // Favoritos
    private fun toggleFavorito(r: Recurso) {
        val id = r.id ?: return
        val nuevo = id !in favoritos
        usuarioCtrl.setFavorito(id, nuevo)
        favoritos = if (nuevo) favoritos + id else favoritos - id
        toast(if (nuevo) R.string.fav_agregado else R.string.fav_quitado)
        aplicarFiltros()
    }

    // Calificación
    private fun dialogoCalificar(r: Recurso) {
        val rb = RatingBar(this).apply { numStars = 5; stepSize = 1f; rating = 3f }
        val cont = FrameLayout(this).apply {
            setPadding(48, 32, 48, 0)
            addView(rb, FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL))
        }
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.calificar_titulo)
            .setView(cont)
            .setPositiveButton(R.string.aceptar) { _, _ -> calificar(r, rb.rating.toInt().coerceAtLeast(1)) }
            .setNegativeButton(R.string.cancelar, null)
            .show()
    }

    private fun calificar(r: Recurso, estrellas: Int) {
        val id = r.id ?: return
        b.progress.visibility = View.VISIBLE
        // Se trae el recurso fresco
        recursoCtrl.obtener(id, { actual ->
            usuarioCtrl.miRating(id) { previo ->
                val nuevo = if (previo == null)
                    actual.copy(ratingTotal = actual.ratingTotal + estrellas, ratingVotos = actual.ratingVotos + 1)
                else // si ya había votado, se reemplaza su voto (no se duplica)
                    actual.copy(ratingTotal = actual.ratingTotal - previo + estrellas)
                recursoCtrl.actualizar(id, nuevo, {
                    usuarioCtrl.guardarRating(id, estrellas)
                    toast(R.string.calificar_ok)
                    cargar()
                }, { b.progress.visibility = View.GONE; toast(getString(R.string.err_red, it)) })
            }
        }, { b.progress.visibility = View.GONE; toast(getString(R.string.err_red, it)) })
    }

    //  Docente: editar / eliminar
    private fun abrirForm(r: Recurso?) {
        val i = Intent(this, FormRecursoActivity::class.java)
        if (r != null) i.putExtra("recurso", Gson().toJson(r))
        startActivity(i)
    }

    private fun confirmarEliminar(r: Recurso) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.confirmar_eliminar_titulo)
            .setMessage(R.string.confirmar_eliminar_msg)
            .setPositiveButton(R.string.eliminar) { _, _ ->
                val id = r.id ?: return@setPositiveButton
                b.progress.visibility = View.VISIBLE
                recursoCtrl.eliminar(id,
                    { toast(R.string.eliminado_ok); cargar() },
                    { b.progress.visibility = View.GONE; toast(getString(R.string.err_red, it)) })
            }
            .setNegativeButton(R.string.cancelar, null)
            .show()
    }

    private fun abrirEnlace(r: Recurso) {
        try { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(r.enlace))) }
        catch (e: Exception) { toast(R.string.err_abrir_enlace) }
    }

    // Menú
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.action_favoritos).isVisible = !esDocente
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_favoritos -> {
            soloFavoritos = !soloFavoritos
            item.setTitle(if (soloFavoritos) R.string.menu_todos else R.string.menu_favoritos)
            aplicarFiltros(); true
        }
        R.id.action_logout -> {
            authCtrl.logout()
            startActivity(Intent(this, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun Spinner.onChange(f: () -> Unit) {
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) = f()
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }
    }
}
