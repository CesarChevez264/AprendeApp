package sv.edu.aprendeapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import sv.edu.aprendeapp.R
import sv.edu.aprendeapp.controller.AuthController
import sv.edu.aprendeapp.databinding.ActivityLoginBinding
import sv.edu.aprendeapp.util.irSegunRol
import sv.edu.aprendeapp.util.toast

class LoginActivity : AppCompatActivity() {
    private lateinit var b: ActivityLoginBinding
    private val auth = AuthController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)

        // Sesión ya iniciada: ir directo a la pantalla según rol
        if (auth.haySesion()) {
            cargando(true)
            auth.rolActual({ irSegunRol(it) }, { cargando(false); auth.logout() })
        }

        b.btnLogin.setOnClickListener {
            val email = b.etEmail.text.toString().trim()
            val pass = b.etPass.text.toString()
            if (email.isEmpty() || pass.isEmpty()) { toast(R.string.err_campos); return@setOnClickListener }
            cargando(true)
            auth.login(email, pass, { irSegunRol(it) }, { cargando(false); toast(R.string.err_login) })
        }
        b.tvRegistro.setOnClickListener { startActivity(Intent(this, RegistroActivity::class.java)) }
    }

    private fun cargando(si: Boolean) {
        b.progress.visibility = if (si) View.VISIBLE else View.GONE
        b.btnLogin.isEnabled = !si
    }
}
