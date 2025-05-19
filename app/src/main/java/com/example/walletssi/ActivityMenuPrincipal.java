package com.example.walletssi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class ActivityMenuPrincipal extends AppCompatActivity {

    private static final String PREFS_USUARIO = "prefs_usuario";
    private static final String KEY_USUARIO = "usuario";
    private static final String KEY_CONTRASENA = "contrasena";

    private FragmentMenu1 fragmentMenu1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        verificarSesion();

    }






    private void verificarSesion() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_USUARIO, Context.MODE_PRIVATE);
        String usuarioGuardado = sharedPreferences.getString(KEY_USUARIO, null);
        String contraseñaGuardada = sharedPreferences.getString(KEY_CONTRASENA, null);

        if (usuarioGuardado == null || contraseñaGuardada == null) {
            // No hay credenciales guardadas, ir a la pantalla de autenticación (ActivityAutenticacion)
            Intent intent = new Intent(this, ActivityAutenticacion.class);
            startActivity(intent);
            finish(); // Cierra ActivityMenuPrincipal para que el usuario no pueda volver atrás sin iniciar sesión
        } else {
            // Credenciales encontradas, el usuario ya está "logueado"
            // Aquí puedes realizar cualquier acción adicional que necesites al iniciar con una sesión existente
            // Por ejemplo, mostrar un mensaje de bienvenida o cargar datos del usuario.
        }
    }

}