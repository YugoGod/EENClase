package hugo.monton.blanco.eenclase.conexiones;

import android.provider.ContactsContract;

import hugo.monton.blanco.eenclase.modelos.DataItem;
import hugo.monton.blanco.eenclase.modelos.Respuesta;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiConexiones {

    // Obtener los datos iniciales.
    @GET("/characters") // Conexión al 'Endpoint'.
    Call<Respuesta> getPersonajes();

    // Obtener un personaje.
    @GET("/character/{id}")
    Call<DataItem> getPersonaje(@Path("id") String id);

    // Obtener una página en concreto. characters?page=1
    @GET("/characters?")
    Call<Respuesta> getPaginaPersonajes(@Query("page") String page);

}
