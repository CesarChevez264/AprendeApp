package sv.edu.aprendeapp.controller

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UsuarioController {
    private fun ref(nodo: String): DatabaseReference {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        return FirebaseDatabase.getInstance().reference.child("usuarios").child(uid).child(nodo)
    }

    fun favoritos(onOk: (Set<String>) -> Unit) {
        ref("favoritos").get()
            .addOnSuccessListener { s -> onOk(s.children.mapNotNull { it.key }.toSet()) }
            .addOnFailureListener { onOk(emptySet()) }
    }

    fun setFavorito(id: String, fav: Boolean) {
        if (fav) ref("favoritos").child(id).setValue(true)
        else ref("favoritos").child(id).removeValue()
    }

    fun miRating(id: String, onOk: (Int?) -> Unit) {
        ref("ratings").child(id).get()
            .addOnSuccessListener { onOk(it.getValue(Int::class.javaObjectType)) }
            .addOnFailureListener { onOk(null) }
    }

    fun guardarRating(id: String, estrellas: Int) {
        ref("ratings").child(id).setValue(estrellas)
    }
}
