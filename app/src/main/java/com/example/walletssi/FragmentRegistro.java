package com.example.walletssi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRegistro#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRegistro extends Fragment {

    private EditText editUsuario;
    private EditText editEmail;
    private EditText editContrasena;
    private EditText editContrasena2;
    private Button botonRegistrar;
    private CheckBox checkboxTerminos;
    private NavController navController;

    private static final String PREFS_USUARIO = "prefs_usuario";
    private static final String KEY_USUARIO = "usuario";
    private static final String KEY_CONTRASENA = "contrasena";
    private static final String KEY_EMAIL = "email";

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentRegistro() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentRegistro.
     */
    public static FragmentRegistro newInstance(String param1, String param2) {
        FragmentRegistro fragment = new FragmentRegistro();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        editUsuario = view.findViewById(R.id.edit_usuario);
        editEmail = view.findViewById(R.id.edit_email);
        editContrasena = view.findViewById(R.id.edit_contrasena);
        editContrasena2 = view.findViewById(R.id.edit_contrasena2);
        botonRegistrar = view.findViewById(R.id.boton_registrar);
        checkboxTerminos = view.findViewById(R.id.checkbox_terminos);

        botonRegistrar.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        String usuario = editUsuario.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String contraseña = editContrasena.getText().toString().trim();
        String confirmarContrasena = editContrasena2.getText().toString().trim();

        if (usuario.isEmpty()) {
            editUsuario.setError("El nombre de usuario es requerido");
            return;
        }

        if (email.isEmpty()) {
            editEmail.setError("El email es requerido");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Introduce un email válido");
            return;
        }

        if (contraseña.isEmpty()) {
            editContrasena.setError("La contraseña es requerida");
            return;
        }

        if (contraseña.length() < 6) {
            editContrasena.setError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        if (!contraseña.equals(confirmarContrasena)) {
            editContrasena2.setError("Las contraseñas no coinciden");
            return;
        }

        if (!checkboxTerminos.isChecked()) {
            Toast.makeText(requireContext(), "Debes aceptar los términos y la política", Toast.LENGTH_SHORT).show();
            return;
        }

        // Guardar los datos del usuario usando SharedPreferences dentro del contexto de la Activity
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_USUARIO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USUARIO, usuario);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_CONTRASENA, contraseña);
        editor.apply();

        Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();

        // Navegar al Fragment de inicio de sesión
        navController.navigate(R.id.action_fragmentRegistro2_to_fragmentInicioSesion2);
    }
}