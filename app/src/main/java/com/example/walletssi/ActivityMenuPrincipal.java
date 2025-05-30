package com.example.walletssi;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * ActivityMenuPrincipal es la actividad que sirve como punto de entrada principal
 * de la aplicación una vez que el usuario ha sido autenticado.
 *
 * Su principal función es verificar si existe una sesión de usuario activa
 * y, en base a eso, dirigir al usuario al contenido principal o a la pantalla de autenticación.
 */

public class ActivityMenuPrincipal extends AppCompatActivity {

    // Constantes para las preferencias compartidas, usadas para guardar y recuperar
    // las credenciales del usuario de forma persistente.
    private static final String PREFS_USUARIO = "prefs_usuario";
    private static final String KEY_USUARIO = "usuario";
    private static final String KEY_CONTRASENA = "contrasena";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        verificarSesion();
    }


    //Verifica si existen credenciales de usuario guardadas en las preferencias compartidas.
    private void verificarSesion() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_USUARIO, Context.MODE_PRIVATE);

        // Recupera el nombre de usuario y la contraseña guardados.
        String usuarioGuardado = sharedPreferences.getString(KEY_USUARIO, null);
        String contraseñaGuardada = sharedPreferences.getString(KEY_CONTRASENA, null);

        if (usuarioGuardado == null || contraseñaGuardada == null) {
            // No hay credenciales guardadas, ir a la pantalla de autenticación (ActivityAutenticacion)
            Intent intent = new Intent(this, ActivityAutenticacion.class);
            startActivity(intent);
            finish(); // Cierra ActivityMenuPrincipal para que el usuario no pueda volver atrás sin iniciar sesión
        } else {

        }
    }
}