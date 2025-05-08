package com.example.walletssi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ActivityAutenticacion extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacion);

        final String fragmentAMostrar = getIntent().getStringExtra("mostrar_fragment");
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content); // Obtener la vista raíz

        rootView.post(new Runnable() {
            @Override
            public void run() {
                FragmentContainerView fragmentContainerView = findViewById(R.id.fragmentContainerView);
                if (fragmentContainerView != null) {
                    NavController navController = Navigation.findNavController(ActivityAutenticacion.this, R.id.fragmentContainerView);
                    if (fragmentAMostrar != null) {
                        if (fragmentAMostrar.equals("inicio_sesion")) {
                            navController.navigate(R.id.fragmentInicioSesion2);
                        } else if (fragmentAMostrar.equals("registro")) {
                            navController.navigate(R.id.fragmentRegistro2);
                        }
                    }
                } else {
                    Log.e("ActivityAutenticacion", "FragmentContainerView not found!");
                }
            }
        });
    }
}