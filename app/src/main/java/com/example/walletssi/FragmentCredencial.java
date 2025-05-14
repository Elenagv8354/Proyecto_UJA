package com.example.walletssi;

import android.os.Bundle;

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
 * Use the {@link FragmentCredencial#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCredencial extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView imageViewBack;
    private NavController navController;
    private Button botonEnviar;
    private String mParam1;
    private String mParam2;

    public FragmentCredencial() {
        // Required empty public constructor
    }

    public static FragmentCredencial newInstance(String param1, String param2) {
        FragmentCredencial fragment = new FragmentCredencial();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
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
        for (int i = 0; i < ((ViewGroup) rootView).getChildCount(); i++) {
            View child = ((ViewGroup) rootView).getChildAt(i);
            if (child instanceof LinearLayout) {
                CheckBox checkBox = child.findViewById(R.id.checkBoxDato1);
                TextView textViewDato = child.findViewById(android.R.id.text1);

                if (checkBox != null && textViewDato != null && checkBox.isChecked()) {
                    seleccionados.add(textViewDato.getText().toString());
                }
            }
        }
        return seleccionados;
    }
}