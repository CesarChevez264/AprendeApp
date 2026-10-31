package sv.edu.aprendeapp.controller

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import sv.edu.aprendeapp.model.Roles

class AuthController {
    private val auth = FirebaseAuth.getInstance()

    private val db = FirebaseDatabase.getInstance().reference

    fun haySesion() = auth.currentUser != null

    fun registrar(nombre: String, email: String, pass: String, rol: String,
                  onOk: (String) -> Unit, onError: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { res ->
                val uid = res.user!!.uid
                db.child("usuarios").child(uid)
                    .setValue(mapOf("nombre" to nombre, "email" to email, "rol" to rol))
                    .addOnSuccessListener { onOk(rol) }
                    .addOnFailureListener { onError(it.localizedMessage ?: "") }
            }
            .addOnFailureListener { onError(it.localizedMessage ?: "") }
    }

    fun login(email: String, pass: String, onOk: (String) -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener { rolActual(onOk, onError) }
            .addOnFailureListener { onError(it.localizedMessage ?: "") }
    }

    fun rolActual(onOk: (String) -> Unit, onError: (String) -> Unit) {
        val uid = auth.currentUser?.uid ?: return onError("sin sesión")
        db.child("usuarios").child(uid).child("rol").get()
            .addOnSuccessListener { onOk(it.getValue(String::class.java) ?: Roles.ESTUDIANTE) }
            .addOnFailureListener { onError(it.localizedMessage ?: "") }
    }

    fun logout() = auth.signOut()
}
