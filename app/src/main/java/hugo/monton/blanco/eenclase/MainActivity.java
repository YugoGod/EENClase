package hugo.monton.blanco.eenclase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import hugo.monton.blanco.eenclase.adapters.PersonajesAdapters;
import hugo.monton.blanco.eenclase.conexiones.ApiConexiones;
import hugo.monton.blanco.eenclase.conexiones.RetrofitObject;
import hugo.monton.blanco.eenclase.modelos.DataItem;
import hugo.monton.blanco.eenclase.modelos.Respuesta;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PersonajesAdapters adapter;
    private RecyclerView.LayoutManager lm;

    private Respuesta respuesta;
    private List<DataItem> personajes;

    private Retrofit retrofit;
    private ApiConexiones apiConexiones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.contenedor);
        personajes = new ArrayList<>();
        adapter = new PersonajesAdapters(personajes, R.layout.personaje_view_holder, this);

        lm = new GridLayoutManager(this, 2);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(lm);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                // En el siguiente condicional miramos si podemos desplazar hacia abajo (1), arriba sería -1.
                if (!recyclerView.canScrollVertically(1)){
                    if (respuesta != null){ // protegemos que alguien no pueda hacer Scroll hasta que hayan datos.
                        String page = respuesta.getNextPage().split("=")[1];
                        Toast.makeText(MainActivity.this, "Page = " + page, Toast.LENGTH_SHORT).show();
                        if (page != null && !page.isEmpty()){
                            cargarSiguientePagina(page);

                        }

                    }
                }
            }


        });

        retrofit = RetrofitObject.getConexion();
        apiConexiones = retrofit.create(ApiConexiones.class);

        cargaInicial();

    }

    private void cargarSiguientePagina(String page) {

        Call<Respuesta> getNextPage = apiConexiones.getPaginaPersonajes(page);

        getNextPage.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                if (response.code() != HttpsURLConnection.HTTP_OK){
                    int temp = personajes.size();
                    respuesta = response.body();
                    personajes.addAll(respuesta.getData());
                    adapter.notifyItemRangeInserted(temp, personajes.size());
                    Toast.makeText(MainActivity.this, "He recargado una página más.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Algo ha fallado.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void cargaInicial() {
        Call<Respuesta> respuesta2 = apiConexiones.getPersonajes();

        respuesta2.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                if (response.code() == HttpsURLConnection.HTTP_OK){
                    respuesta = response.body();
                    personajes.addAll(respuesta.getData());
                    adapter.notifyItemRangeInserted(0, respuesta.getCount());
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {

            }
        });
    }
}