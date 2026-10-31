package sv.edu.aprendeapp.controller

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.edu.aprendeapp.model.Recurso
import sv.edu.aprendeapp.network.RetrofitClient

class RecursoController {
    private val api = RetrofitClient.api

    private fun <T> Call<T>.ejecutar(onOk: (T?) -> Unit, onError: (String) -> Unit) {
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) onOk(response.body()) else onError("HTTP ${response.code()}")
            }
            override fun onFailure(call: Call<T>, t: Throwable) = onError(t.localizedMessage ?: "Error")
        })
    }

    fun listar(onOk: (List<Recurso>) -> Unit, onError: (String) -> Unit) =
        api.listar().ejecutar({ onOk(it.orEmpty()) }, onError)

    fun obtener(id: String, onOk: (Recurso) -> Unit, onError: (String) -> Unit) =
        api.obtener(id).ejecutar({ if (it != null) onOk(it) else onError("vacío") }, onError)

    fun crear(r: Recurso, onOk: () -> Unit, onError: (String) -> Unit) =
        api.crear(r).ejecutar({ onOk() }, onError)

    fun actualizar(id: String, r: Recurso, onOk: () -> Unit, onError: (String) -> Unit) =
        api.actualizar(id, r).ejecutar({ onOk() }, onError)

    fun eliminar(id: String, onOk: () -> Unit, onError: (String) -> Unit) =
        api.eliminar(id).ejecutar({ onOk() }, onError)
}
