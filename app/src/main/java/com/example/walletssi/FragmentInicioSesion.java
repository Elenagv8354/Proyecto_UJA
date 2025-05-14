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

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentInicioSesion#} factory method to
 * create an instance of this fragment.
 */
public class FragmentInicioSesion extends Fragment {

    private EditText editTextUsuario;
    private EditText editTextContrasena;
    private Button botonIniciarSesion;

    private static final String PREFS_USUARIO = "prefs_usuario";
    private static final String KEY_USUARIO = "usuario";
    private static final String KEY_CONTRASENA = "contrasena";

    public FragmentInicioSesion() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio_sesion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextUsuario = view.findViewById(R.id.edit_usuario_email); // ID EditText de usuario
        editTextContrasena = view.findViewById(R.id.edit_contrasena); // ID EditText de contraseña
        botonIniciarSesion = view.findViewById(R.id.boton_iniciar_sesion); // ID botón de inicio de sesión

        if (botonIniciarSesion != null) {
            botonIniciarSesion.setOnClickListener(v -> iniciarSesion());
        }
    }

    private void iniciarSesion() {
        String usuarioIngresado = editTextUsuario.getText().toString().trim();
        String contraseñaIngresada = editTextContrasena.getText().toString().trim();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_USUARIO, Context.MODE_PRIVATE);
        String usuarioGuardado = sharedPreferences.getString(KEY_USUARIO, null);
        String contraseñaGuardada = sharedPreferences.getString(KEY_CONTRASENA, null);

        Log.d("InicioSesion", "Usuario Guardado: " + usuarioGuardado);
        Log.d("InicioSesion", "Contraseña Guardada: " + contraseñaGuardada);
        Log.d("InicioSesion", "Usuario Ingresado: " + usuarioIngresado);
        Log.d("InicioSesion", "Contraseña Ingresada: " + contraseñaIngresada);

        if (usuarioGuardado != null && contraseñaGuardada != null &&
                usuarioIngresado.equals(usuarioGuardado) && contraseñaIngresada.equals(contraseñaGuardada)) {
            Intent intent = new Intent(requireContext(), ActivityMenuPrincipal.class);
            startActivity(intent);
            requireActivity().finish(); // cerrar ActivityAutenticacion
        } else {
            Toast.makeText(requireContext(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        }
    }
}