package sv.edu.aprendeapp.view

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import sv.edu.aprendeapp.R
import sv.edu.aprendeapp.controller.AuthController
import sv.edu.aprendeapp.databinding.ActivityRegistroBinding
import sv.edu.aprendeapp.model.Roles
import sv.edu.aprendeapp.util.PasswordValidator
import sv.edu.aprendeapp.util.irSegunRol
import sv.edu.aprendeapp.util.toast

class RegistroActivity : AppCompatActivity() {
    private lateinit var b: ActivityRegistroBinding
    private val auth = AuthController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.btnRegistrar.setOnClickListener { registrar() }
    }

    private fun registrar() {
        val nombre = b.etNombre.text.toString().trim()
        val email = b.etEmail.text.toString().trim()
        val pass = b.etPass.text.toString()
        val confirmar = b.etConfirmar.text.toString()
        listOf(b.tilNombre, b.tilEmail, b.tilPass, b.tilConfirmar).forEach { it.error = null }

        var ok = true
        if (nombre.isEmpty()) { b.tilNombre.error = getString(R.string.err_campos); ok = false }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { b.tilEmail.error = getString(R.string.err_email); ok = false }
        val errores = PasswordValidator.errores(pass)
        if (errores.isNotEmpty()) {
            b.tilPass.error = errores.joinToString("\n") { getString(it) }
            ok = false
        }
        if (pass != confirmar) { b.tilConfirmar.error = getString(R.string.err_pass_no_coincide); ok = false }
        if (!ok) return

        val rol = if (b.rbDocente.isChecked) Roles.DOCENTE else Roles.ESTUDIANTE
        cargando(true)
        auth.registrar(nombre, email, pass, rol,
            onOk = { toast(R.string.registro_ok); irSegunRol(it) },
            onError = { cargando(false); toast(getString(R.string.err_red, it)) })
    }

    private fun cargando(si: Boolean) {
        b.progress.visibility = if (si) View.VISIBLE else View.GONE
        b.btnRegistrar.isEnabled = !si
    }
}
