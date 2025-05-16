package com.example.walletssi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CredencialAdapter extends RecyclerView.Adapter<CredencialAdapter.CredencialViewHolder> {
    private List<Credencial> credenciales;
    private final OnRestaurarClickListener restaurarClickListener;

    public interface OnRestaurarClickListener {
        void onRestaurarClick(Credencial credencial);
    }

    public CredencialAdapter(List<Credencial> credenciales, OnRestaurarClickListener listener) {
        this.credenciales = credenciales;
        this.restaurarClickListener = listener;
    }

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

        if (credencial.isOculta()) {
            holder.buttonRestaurar.setVisibility(View.VISIBLE);
            holder.buttonRestaurar.setOnClickListener(v -> restaurarClickListener.onRestaurarClick(credencial));
        } else {
            holder.buttonRestaurar.setVisibility(View.GONE);
        }

        // Puedes añadir un OnClickListener para navegar a la credencial si se pulsa el item
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
