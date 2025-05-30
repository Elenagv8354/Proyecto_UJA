package com.example.walletssi;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

/**
 * ActivityBienvenida es la primera actividad que se muestra al iniciar la aplicación.
 * Sirve como punto de entrada y presenta al usuario las opciones de iniciar sesión o registrarse.
 */

public class ActivityBienvenida extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        // Busca los botones de "Iniciar Sesión" y "Registrar" en el layout
        Button botonIniciarSesion = findViewById(R.id.boton_iniciarSesion);
        Button botonRegistrar = findViewById(R.id.boton_registrar);

        // Configura el comportamiento al hacer clic en el botón "Iniciar Sesión" o "Registrar".
        // y le indica que fragmento debe mostrar.
        if (botonIniciarSesion != null) {
            botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentInicioSesion = new Intent(ActivityBienvenida.this, ActivityAutenticacion.class);
                    intentInicioSesion.putExtra("mostrar_fragment", "inicio_sesion");
                    startActivity(intentInicioSesion);
                }
            });
        }

        if (botonRegistrar != null) {
            botonRegistrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentRegistro = new Intent(ActivityBienvenida.this, ActivityAutenticacion.class);
                    intentRegistro.putExtra("mostrar_fragment", "registro");
                    startActivity(intentRegistro);
                }
            });
        }
    }
}