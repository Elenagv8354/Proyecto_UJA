package com.example.walletssi;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
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

import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCredencial#} factory method to
 * create an instance of this fragment.
 */
public class FragmentCredencial extends Fragment {

    private ImageView imageViewBack;
    private NavController navController;
    private Button botonEnviar;

    // Estado de visibilidad de la información detallada
    private boolean isNombreVisible = true;
    private boolean isDniVisible = true;
    private boolean isNacimientoVisible = true;
    private boolean isTelefonoVisible = true;

    // Views para cada dato
    private TextView infoText1;
    private ImageView imageViewMostrarOcultarNombre;
    private TextView infoText2;
    private ImageView imageViewMostrarOcultarDni;
    private TextView infoText3;
    private ImageView imageViewMostrarOcultarNacimiento;
    private TextView infoText4;
    private ImageView imageViewMostrarOcultarTelefono;

    public FragmentCredencial() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_credencial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        imageViewBack = view.findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(v -> navController.navigateUp());

        // Inicializar vistas
        infoText1 = view.findViewById(R.id.infoText1);
        imageViewMostrarOcultarNombre = view.findViewById(R.id.imageViewMostrarOcultarNombre);
        infoText2 = view.findViewById(R.id.infoText2);
        imageViewMostrarOcultarDni = view.findViewById(R.id.imageViewMostrarOcultarDni);
        infoText3 = view.findViewById(R.id.infoText3);
        imageViewMostrarOcultarNacimiento = view.findViewById(R.id.imageViewMostrarOcultarNacimiento);
        infoText4 = view.findViewById(R.id.infoText4);
        imageViewMostrarOcultarTelefono = view.findViewById(R.id.imageViewMostrarOcultarTelefono);

        // Configurar listeners para mostrar/ocultar
        imageViewMostrarOcultarNombre.setOnClickListener(v -> toggleVisibility(infoText1, imageViewMostrarOcultarNombre, "Nombre"));
        imageViewMostrarOcultarDni.setOnClickListener(v -> toggleVisibility(infoText2, imageViewMostrarOcultarDni, "DNI"));
        imageViewMostrarOcultarNacimiento.setOnClickListener(v -> toggleVisibility(infoText3, imageViewMostrarOcultarNacimiento, "Nacimiento"));
        imageViewMostrarOcultarTelefono.setOnClickListener(v -> toggleVisibility(infoText4, imageViewMostrarOcultarTelefono, "Telefono"));

        botonEnviar = view.findViewById(R.id.boton_enviar);
        if (botonEnviar != null) {
            botonEnviar.setOnClickListener(v -> {
                List<String> datosSeleccionados = obtenerDatosSeleccionados(view);

                if (!datosSeleccionados.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("datos", (ArrayList<String>) datosSeleccionados);
                    navController.navigate(R.id.action_fragmentCredencial_to_fragmentQr, bundle);
                } else {
                    Toast.makeText(requireContext(), "Por favor, selecciona al menos un dato.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void toggleVisibility(TextView textView, ImageView imageView, String dato) {
        if (textView.getVisibility() == View.VISIBLE) {
            textView.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.mostrar_ocultar);
            switch (dato) {
                case "Nombre":
                    isNombreVisible = false;
                    break;
                case "DNI":
                    isDniVisible = false;
                    break;
                case "Nacimiento":
                    isNacimientoVisible = false;
                    break;
                case "Telefono":
                    isTelefonoVisible = false;
                    break;
            }
        } else {
            textView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.mostrar);
            switch (dato) {
                case "Nombre":
                    isNombreVisible = true;
                    break;
                case "DNI":
                    isDniVisible = true;
                    break;
                case "Nacimiento":
                    isNacimientoVisible = true;
                    break;
                case "Telefono":
                    isTelefonoVisible = true;
                    break;
            }
        }
    }

    private List<String> obtenerDatosSeleccionados(View rootView) {
        List<String> seleccionados = new ArrayList<>();
        LinearLayout linearLayoutInformacion = rootView.findViewById(R.id.linearLayoutInformacion);

        if (linearLayoutInformacion != null) {
            for (int i = 0; i < linearLayoutInformacion.getChildCount(); i++) {
                View cardViewChild = linearLayoutInformacion.getChildAt(i);
                if (cardViewChild instanceof CardView && ((CardView) cardViewChild).getChildCount() > 0) {
                    LinearLayout linearLayoutHorizontal = (LinearLayout) ((CardView) cardViewChild).getChildAt(0);
                    if (linearLayoutHorizontal != null && linearLayoutHorizontal.getOrientation() == LinearLayout.HORIZONTAL) {
                        // Obtener IDs dinámicamente
                        int checkBoxId = getResources().getIdentifier("checkBoxDato" + (i + 1), "id", requireContext().getPackageName());
                        int linearLayoutTextoId = getResources().getIdentifier("linearLayoutTexto" + obtenerNombreDato(i + 1), "id", requireContext().getPackageName());
                        int textId = getResources().getIdentifier("text" + (i + 1), "id", requireContext().getPackageName());
                        int infoTextId = getResources().getIdentifier("infoText" + (i + 1), "id", requireContext().getPackageName());

                        CheckBox checkBox = linearLayoutHorizontal.findViewById(checkBoxId);
                        LinearLayout linearLayoutTextos = linearLayoutHorizontal.findViewById(linearLayoutTextoId);
                        TextView textViewTitulo = null;
                        TextView textViewInfo = null;

                        if (linearLayoutTextos != null && linearLayoutTextos.getChildCount() == 2) {
                            textViewTitulo = (TextView) linearLayoutTextos.getChildAt(0);
                            textViewInfo = (TextView) linearLayoutTextos.getChildAt(1);
                        } else {
                            textViewTitulo = linearLayoutHorizontal.findViewById(textId);
                            textViewInfo = linearLayoutHorizontal.findViewById(infoTextId);
                        }

                        if (checkBox != null && textViewTitulo != null && checkBox.isChecked()) {
                            seleccionados.add(textViewTitulo.getText().toString());
                            if (textViewInfo != null && textViewInfo.getVisibility() == View.VISIBLE) {
                                seleccionados.add(textViewInfo.getText().toString());
                            }
                        }
                    }
                }
            }
        }
        return seleccionados;
    }

    // Método auxiliar para obtener el nombre del dato basado en el índice
    private String obtenerNombreDato(int index) {
        switch (index) {
            case 1:
                return "Nombre";
            case 2:
                return "Dni";
            case 3:
                return "Nacimiento";
            case 4:
                return "Telefono";
            default:
                return ""; // O maneja el caso de más datos
        }
    }
}