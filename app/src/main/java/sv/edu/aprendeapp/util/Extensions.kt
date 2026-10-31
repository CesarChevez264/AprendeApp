package sv.edu.aprendeapp.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.StringRes
import sv.edu.aprendeapp.model.Roles
import sv.edu.aprendeapp.view.DocenteActivity
import sv.edu.aprendeapp.view.RecursosActivity

fun Context.toast(@StringRes id: Int) = Toast.makeText(this, id, Toast.LENGTH_SHORT).show()
fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun Activity.irSegunRol(rol: String) {
    val destino = if (rol == Roles.DOCENTE) DocenteActivity::class.java else RecursosActivity::class.java
    startActivity(Intent(this, destino))
    finish()
}
