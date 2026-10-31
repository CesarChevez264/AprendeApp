package sv.edu.aprendeapp.network

import retrofit2.Call
import retrofit2.http.*
import sv.edu.aprendeapp.model.Recurso

interface RecursoApi {
    @GET("recursos") fun listar(): Call<List<Recurso>>
    @GET("recursos/{id}") fun obtener(@Path("id") id: String): Call<Recurso>
    @POST("recursos") fun crear(@Body recurso: Recurso): Call<Recurso>
    @PUT("recursos/{id}") fun actualizar(@Path("id") id: String, @Body recurso: Recurso): Call<Recurso>
    @DELETE("recursos/{id}") fun eliminar(@Path("id") id: String): Call<Void>
}
