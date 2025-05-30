package com.example.walletssi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * CredencialAdapter es un adaptador para RecyclerView que se encarga de mostrar
 * una lista de objetos Credencial.
 * Proporciona los datos a cada elemento visual de la lista y maneja la interacción
 * del usuario, como el botón de "Mostrar" para las credenciales ocultas.
 */

public class CredencialAdapter extends RecyclerView.Adapter<CredencialAdapter.CredencialViewHolder> {

    // Lista de objetos Credencial que se mostrarán en el RecyclerView.
    private List<Credencial> credenciales;
    private final OnRestaurarClickListener restaurarClickListener;

    public interface OnRestaurarClickListener {
        void onRestaurarClick(Credencial credencial);
    }

    public CredencialAdapter(List<Credencial> credenciales, OnRestaurarClickListener listener) {
        this.credenciales = credenciales;
        this.restaurarClickListener = listener;
    }

    //Actualiza la lista de credenciales y notifica al RecyclerView para que se actualice con los nuevos datos.
    public void setCredenciales(List<Credencial> credenciales) {
        this.credenciales = credenciales;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CredencialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_credencial, parent, false); // Crea un layout para cada item
        return new CredencialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CredencialViewHolder holder, int position) {
        Credencial credencial = credenciales.get(position);
        holder.textViewNombre.setText(credencial.getNombre());

        // Controla la visibilidad del botón "Restaurar" basado en el estado 'oculta' de la credencial.
        if (credencial.isOculta()) {
            holder.buttonRestaurar.setVisibility(View.VISIBLE);
            holder.buttonRestaurar.setOnClickListener(v -> restaurarClickListener.onRestaurarClick(credencial));
        } else {
            holder.buttonRestaurar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return credenciales.size();
    }

    public static class CredencialViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNombre;
        public Button buttonRestaurar;

        public CredencialViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombreCredencial);
            buttonRestaurar = itemView.findViewById(R.id.buttonRestaurarCredencial);
        }
    }
}
