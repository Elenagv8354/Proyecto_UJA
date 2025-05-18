package com.example.walletssi;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.navigation.Navigation;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCredencial#} factory method to
 * create an instance of this fragment.
 */
public class FragmentCredencial extends Fragment {

    private ImageView imageViewBack;
    private NavController navController;
    private Button botonEnviar;

    public FragmentCredencial() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_credencial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        imageViewBack = view.findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(v -> navController.navigateUp());

        botonEnviar = view.findViewById(R.id.boton_enviar);
        if (botonEnviar != null) {
            botonEnviar.setOnClickListener(v -> {
                List<String> datosSeleccionados = obtenerDatosSeleccionados(view);

                if (!datosSeleccionados.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("datos", (ArrayList<String>) datosSeleccionados);
                    navController.navigate(R.id.action_fragmentCredencial_to_fragmentQr, bundle);
                } else {
                    Toast.makeText(requireContext(), "Por favor, selecciona al menos un dato.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private List<String> obtenerDatosSeleccionados(View rootView) {
        List<String> seleccionados = new ArrayList<>();
        LinearLayout linearLayoutInformacion = rootView.findViewById(R.id.linearLayoutInformacion); // ID del LinearLayout que contiene las CardViews

        if (linearLayoutInformacion != null) {
            for (int i = 0; i < linearLayoutInformacion.getChildCount(); i++) {
                View cardViewChild = linearLayoutInformacion.getChildAt(i);
                if (cardViewChild instanceof CardView) {
                    LinearLayout linearLayoutDentroCard = (LinearLayout) ((CardView) cardViewChild).getChildAt(0); // Primer hijo de CardView es el LinearLayout horizontal
                    if (linearLayoutDentroCard != null) {
                        CheckBox checkBox = linearLayoutDentroCard.findViewById(R.id.checkBoxDato1); // ID común de tus CheckBox
                        LinearLayout linearLayoutTexto = (LinearLayout) linearLayoutDentroCard.getChildAt(1); // Segundo hijo es el LinearLayout vertical del texto
                        if (linearLayoutTexto != null) {
                            TextView textViewDato = linearLayoutTexto.findViewById(R.id.text1); // ID común del TextView del dato

                            if (checkBox != null && textViewDato != null && checkBox.isChecked()) {
                                seleccionados.add(textViewDato.getText().toString());
                            }
                        }
                    }
                }
            }
        }
        return seleccionados;
    }
}