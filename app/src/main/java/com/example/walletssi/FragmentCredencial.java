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
    private Button botonEnviar; // Referencia al botón "Enviar"
    private String mParam1;
    private String mParam2;

    public FragmentCredencial() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCredencial.
     */
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_credencial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        imageViewBack = view.findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(v -> {
            // Utiliza navigateUp() para volver a la pantalla anterior en la pila de navegación
            navController.navigateUp();
            // Alternativamente, puedes navegar directamente al FragmentMenu1 usando su ID:
            // navController.navigate(R.id.fragmentMenu1);
        });

        botonEnviar = view.findViewById(R.id.boton_enviar); // Reemplaza con el ID de tu botón "Enviar"
        if (botonEnviar != null) {
            botonEnviar.setOnClickListener(v -> {
                // Verificar si todos los datos están seleccionados
                boolean datosSeleccionados = true; // Reemplaza con tu lógica de verificación

                if (datosSeleccionados) {
                    Navigation.findNavController(v).navigate(R.id.action_fragmentCredencial_to_fragmentQr);
                } else {
                    // Aquí puedes mostrar un mensaje al usuario indicando que faltan datos
                    // Por ejemplo: Toast.makeText(getContext(), "Por favor, selecciona todos los datos.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}