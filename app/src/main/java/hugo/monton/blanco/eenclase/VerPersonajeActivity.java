package hugo.monton.blanco.eenclase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import javax.net.ssl.HttpsURLConnection;

import hugo.monton.blanco.eenclase.conexiones.ApiConexiones;
import hugo.monton.blanco.eenclase.conexiones.RetrofitObject;
import hugo.monton.blanco.eenclase.modelos.DataItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerPersonajeActivity extends AppCompatActivity {

    private ImageView imgPersonaje;
    private TextView lblNombre;
    private TextView lblPelis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_personaje);

        inicializaVistas();
        Toast.makeText(this, "ID: " + getIntent().getExtras().getString("ID"), Toast.LENGTH_SHORT).show();
        if (getIntent().getExtras() != null && getIntent().getExtras().getString("ID") != null){
            ApiConexiones api = RetrofitObject.getConexion().create(ApiConexiones.class);
            Call<DataItem> doGetPersoanaje = api.getPersonaje(getIntent().getExtras().getString("ID"));

            doGetPersoanaje.enqueue(new Callback<DataItem>() {
                @Override
                public void onResponse(Call<DataItem> call, Response<DataItem> response) {
                    if (response.code() == HttpsURLConnection.HTTP_OK){
                        lblNombre.setText(response.body().getName());
                        lblPelis.setText(response.body().getFilms().toString());
                        Picasso.get()
                                .load(response.body().getImageUrl())
                                .error(R.drawable.ic_launcher_background)
                                .error(R.drawable.ic_launcher_background)
                                .into(imgPersonaje);
                    }else{
                        Toast.makeText(VerPersonajeActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DataItem> call, Throwable t) {

                }
            });
        }

    }

    private void inicializaVistas() {
        imgPersonaje = findViewById(R.id.imgPersonajeVer);
        lblNombre = findViewById(R.id.lblNombrePersonajeVer);
        lblPelis = findViewById(R.id.lblFilmsPersonajeVer);
    }
}