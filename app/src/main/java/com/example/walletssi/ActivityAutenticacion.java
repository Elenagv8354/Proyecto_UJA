package com.example.walletssi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

public class ActivityAutenticacion extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacion);

        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);

        String fragmentAMostrar = getIntent().getStringExtra("mostrar_fragment");

        if (fragmentAMostrar != null) {
            if (fragmentAMostrar.equals("inicio_sesion")) {
                navController.navigate(R.id.fragmentInicioSesion2);
            } else if (fragmentAMostrar.equals("registro")) {
                navController.navigate(R.id.fragmentRegistro2);
            }
        }
    }
}