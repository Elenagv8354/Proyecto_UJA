package com.example.walletssi;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.File;
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
    private ImageView imageViewQrGuardado;

    // Estado de visibilidad de la información detallada
    private boolean isNombreVisible = false;
    private boolean isDniVisible = false;
    private boolean isNacimientoVisible = false;
    private boolean isTelefonoVisible = false;

    // Views para cada dato
    private TextView infoText1;
    private ImageView imageViewMostrarOcultarNombre;
    private TextView infoText2;
    private ImageView imageViewMostrarOcultarDni;
    private TextView infoText3;
    private ImageView imageViewMostrarOcultarNacimiento;
    private TextView infoText4;
    private ImageView imageViewMostrarOcultarTelefono;

    // // Variables para almacenar los datos reales de la credencial
    private String realNombre = "Elena García Valenzuela";
    private String realDni = "82771927F";
    private String realNacimiento = "02/01/2003";
    private String realTelefono = "816827697";

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

        imageViewQrGuardado = view.findViewById(R.id.imageViewQrGuardado);
        cargarQrGuardado(); // Llama a la función para cargar el QR guardado

        // Establecer OnLongClickListener para borrar el código QR
        imageViewQrGuardado.setOnLongClickListener(v -> {
            mostrarDialogoBorrarQr();
            return true; // Indica que el evento de long click ha sido consumido
        });

        // // // ESTO ES LO QUE FALTABA: INICIALIZAR LA VISIBILIDAD DE LOS TEXTVIEWS // // //
        // Llama a la función que aplica el estado de visibilidad con el estado inicial (false = GONE).
        // Pasamos los datos REALES para que estén listos si se hacen visibles.
        applyVisibilityState(infoText1, imageViewMostrarOcultarNombre, isNombreVisible, realNombre);
        applyVisibilityState(infoText2, imageViewMostrarOcultarDni, isDniVisible, realDni);
        applyVisibilityState(infoText3, imageViewMostrarOcultarNacimiento, isNacimientoVisible, realNacimiento);
        applyVisibilityState(infoText4, imageViewMostrarOcultarTelefono, isTelefonoVisible, realTelefono);

        // --- CONFIGURAR LISTENERS PARA MOSTRAR/OCULTAR ---
        // Los listeners ahora invierten el estado booleano y luego llaman a 'applyVisibilityState'
        // para actualizar la UI.
        imageViewMostrarOcultarNombre.setOnClickListener(v -> {
            isNombreVisible = !isNombreVisible; // Invertir el estado
            applyVisibilityState(infoText1, imageViewMostrarOcultarNombre, isNombreVisible, realNombre);
        });
        imageViewMostrarOcultarDni.setOnClickListener(v -> {
            isDniVisible = !isDniVisible;
            applyVisibilityState(infoText2, imageViewMostrarOcultarDni, isDniVisible, realDni);
        });
        imageViewMostrarOcultarNacimiento.setOnClickListener(v -> {
            isNacimientoVisible = !isNacimientoVisible;
            applyVisibilityState(infoText3, imageViewMostrarOcultarNacimiento, isNacimientoVisible, realNacimiento);
        });
        imageViewMostrarOcultarTelefono.setOnClickListener(v -> {
            isTelefonoVisible = !isTelefonoVisible;
            applyVisibilityState(infoText4, imageViewMostrarOcultarTelefono, isTelefonoVisible, realTelefono);
        });


        botonEnviar = view.findViewById(R.id.boton_enviar);
        if (botonEnviar != null) {
            botonEnviar.setOnClickListener(v -> {
                // IMPORTANT: 'obtenerDatosSeleccionados' SIEMPRE usa las variables 'realXxx'
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
    } // Cierre correcto de onViewCreated

    private void cargarQrGuardado() {
        // Obtiene el directorio de archivos internos
        File directorioInterno = requireContext().getFilesDir();
        // Crea una referencia al archivo del código QR guardado
        File archivoQr = new File(directorioInterno, "codigo_qr.png");

        // Verifica si el archivo existe
        if (archivoQr.exists()) {
            // Decodifica el archivo de imagen en un objeto Bitmap
            Bitmap bitmapQr = BitmapFactory.decodeFile(archivoQr.getAbsolutePath());
            // Establece el Bitmap en el ImageView para mostrarlo
            imageViewQrGuardado.setImageBitmap(bitmapQr);
            // Haz visible el ImageView ahora que tiene una imagen
            imageViewQrGuardado.setVisibility(View.VISIBLE);
        } else {
            // Si el archivo no existe (nunca se ha guardado un QR), oculta el ImageView
            imageViewQrGuardado.setVisibility(View.GONE);
        }
    }

    private void mostrarDialogoBorrarQr() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Borrar código QR")
                .setMessage("¿Estás seguro de que quieres borrar el código QR guardado?")
                .setPositiveButton("Borrar", (dialog, which) -> {
                    borrarQrGuardado();
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void borrarQrGuardado() {
        File directorioInterno = requireContext().getFilesDir();
        File archivoQr = new File(directorioInterno, "codigo_qr.png");

        if (archivoQr.exists()) {
            if (archivoQr.delete()) {
                Toast.makeText(requireContext(), "Código QR borrado", Toast.LENGTH_SHORT).show();
                imageViewQrGuardado.setVisibility(View.GONE); // Oculta el ImageView después de borrar
            } else {
                Toast.makeText(requireContext(), "Error al borrar el código QR", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "No hay código QR guardado para borrar", Toast.LENGTH_SHORT).show();
        }
    }

    // --- FUNCIÓN CLAVE: PARA APLICAR EL ESTADO DE VISIBILIDAD ---
    // Esta función maneja la lógica de mostrar/ocultar y cambiar el icono del ojo.
    private void applyVisibilityState(TextView textView, ImageView imageView, boolean visible, String data) {
        if (visible) {
            textView.setText(data); // Asigna el dato real
            textView.setVisibility(View.VISIBLE); // ¡Hace el TextView visible!
            // SUGERENCIA: Usa iconos más descriptivos como ic_visibility_on
            imageView.setImageResource(R.drawable.mostrar); // Ojo ABIERTO
        } else {
            textView.setText(""); // Limpia el texto (opcional, pero buena práctica si está GONE)
            textView.setVisibility(View.GONE); // ¡Hace el TextView GONE (oculto completamente)!
            // SUGERENCIA: Usa iconos más descriptivos como ic_visibility_off
            imageView.setImageResource(R.drawable.mostrar_ocultar); // Ojo CERRADO
        }
    }


    // --- MÉTODO PARA OBTENER DATOS SELECCIONADOS PARA EL QR ---
    // Es CRÍTICO que este método SIEMPRE use las variables 'realXxx' para los datos.
    private List<String> obtenerDatosSeleccionados(View rootView) {
        List<String> seleccionados = new ArrayList<>();
        LinearLayout linearLayoutInformacion = rootView.findViewById(R.id.linearLayoutInformacion);

        if (linearLayoutInformacion != null) {
            for (int i = 0; i < linearLayoutInformacion.getChildCount(); i++) {
                View cardViewChild = linearLayoutInformacion.getChildAt(i);
                if (cardViewChild instanceof CardView) {
                    LinearLayout linearLayoutHorizontal = (LinearLayout) ((CardView) cardViewChild).getChildAt(0);
                    if (linearLayoutHorizontal != null && linearLayoutHorizontal.getOrientation() == LinearLayout.HORIZONTAL) {
                        int checkBoxId = getResources().getIdentifier("checkBoxDato" + (i + 1), "id", requireContext().getPackageName());
                        CheckBox checkBox = linearLayoutHorizontal.findViewById(checkBoxId);

                        // Obtén el TextView del título (ej. "Nombre:", "DNI:") para el prefijo del QR.
                        int textId = getResources().getIdentifier("text" + (i + 1), "id", requireContext().getPackageName());
                        TextView textViewTitulo = null;
                        // Intenta encontrar el LinearLayout que contiene los textos primero (si lo tienes en tu XML)
                        int linearLayoutTextoId = getResources().getIdentifier("linearLayoutTexto" + obtenerNombreDato(i + 1), "id", requireContext().getPackageName());
                        LinearLayout linearLayoutTextos = linearLayoutHorizontal.findViewById(linearLayoutTextoId);
                        if (linearLayoutTextos != null && linearLayoutTextos.getChildCount() > 0) {
                            textViewTitulo = (TextView) linearLayoutTextos.getChildAt(0);
                        } else {
                            // Si no, busca el TextView del título directamente
                            textViewTitulo = linearLayoutHorizontal.findViewById(textId);
                        }


                        if (checkBox != null && checkBox.isChecked()) {
                            String prefijo = (textViewTitulo != null) ? textViewTitulo.getText().toString() + " " : "";

                            // AÑADE EL DATO REAL DESDE LAS VARIABLES DE CLASE
                            switch (i + 1) { // i + 1 porque tus IDs de datos son 1-indexed (infoText1, etc.)
                                case 1: // Corresponde a infoText1 (Nombre)
                                    if (!realNombre.isEmpty()) seleccionados.add(prefijo + realNombre);
                                    break;
                                case 2: // Corresponde a infoText2 (DNI)
                                    if (!realDni.isEmpty()) seleccionados.add(prefijo + realDni);
                                    break;
                                case 3: // Corresponde a infoText3 (Nacimiento)
                                    if (!realNacimiento.isEmpty()) seleccionados.add(prefijo + realNacimiento);
                                    break;
                                case 4: // Corresponde a infoText4 (Telefono)
                                    if (!realTelefono.isEmpty()) seleccionados.add(prefijo + realTelefono);
                                    break;
                            }
                        }
                    }
                }
            }
        }
        return seleccionados;
    }

    /**
     * Método auxiliar para obtener el nombre de un dato basado en su índice.
     * Usado para construir IDs de recursos dinámicamente.
     * @param index El índice del dato (1 para Nombre, 2 para DNI, etc.).
     * @return El nombre del dato como cadena.
     */
    private String obtenerNombreDato(int index) {
        switch (index) {
            case 1: return "Nombre";
            case 2: return "Dni";
            case 3: return "Nacimiento";
            case 4: return "Telefono";
            default: return "";
        }
    }
}