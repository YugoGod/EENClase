package hugo.monton.blanco.eenclase.adapters;

import android.content.Context;
import android.content.Intent;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import hugo.monton.blanco.eenclase.R;
import hugo.monton.blanco.eenclase.VerPersonajeActivity;
import hugo.monton.blanco.eenclase.modelos.DataItem;

public class PersonajesAdapters extends RecyclerView.Adapter<PersonajesAdapters.PersonajeVH> {

    private List<DataItem> objects;
    private int resources;
    private Context context;

    public PersonajesAdapters(List<DataItem> objects, int resources, Context context) {
        this.objects = objects;
        this.resources = resources;
        this.context = context;
    }

    @NonNull
    @Override
    public PersonajesAdapters.PersonajeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PersonajeVH(LayoutInflater.from(context).inflate(resources, null));
    }

    @Override
    public void onBindViewHolder(@NonNull PersonajesAdapters.PersonajeVH holder, int position) {

        DataItem personaje = objects.get(position);

        holder.lblNombre.setText(personaje.getName());

        Picasso.get()
                .load(personaje.getImageUrl())
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgPersonaje);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VerPersonajeActivity.class);
                Bundle budnle = new Bundle();
                budnle.putString("ID", String.valueOf(personaje.getId()));
                intent.putExtras(budnle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class PersonajeVH extends RecyclerView.ViewHolder {
        ImageView imgPersonaje;
        TextView lblNombre;

        public PersonajeVH(@NonNull View itemView) {
            super(itemView);

            imgPersonaje = itemView.findViewById(R.id.imgPersonajeVH);
            lblNombre = itemView.findViewById(R.id.lblNombrePersonajeVH);

        }
    }
}
