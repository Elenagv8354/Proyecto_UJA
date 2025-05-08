package com.example.walletssi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentInicioSesion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInicioSesion extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentInicioSesion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentInicioSesion.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentInicioSesion newInstance(String param1, String param2) {
        FragmentInicioSesion fragment = new FragmentInicioSesion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio_sesion, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editTextUsuario = view.findViewById(R.id.edit_usuario_email); // ID EditText de usuario
        EditText editTextContrasena = view.findViewById(R.id.edit_contrasena); // ID EditText de contraseña
        Button botonIniciarSesion = view.findViewById(R.id.boton_iniciar_sesion); // ID botón de inicio de sesión

        if (botonIniciarSesion != null) {
            botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String usuario = editTextUsuario.getText().toString();
                    String contrasena = editTextContrasena.getText().toString();

                    if (usuario.equals("usuario") && contrasena.equals("contrasena")) { // Ejemplo de autenticación exitosa
                        Intent intent = new Intent(requireContext(), ActivityMenuPrincipal.class);
                        startActivity(intent);
                        requireActivity().finish(); // cerrar ActivityAutenticacion
                    } else {
                        Toast.makeText(requireContext(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}