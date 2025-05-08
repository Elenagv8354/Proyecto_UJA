package com.example.walletssi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

public class ActivityBienvenida extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        Button botonIniciarSesion = findViewById(R.id.boton_iniciarSesion);
        Button botonRegistrar = findViewById(R.id.boton_registrar);

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