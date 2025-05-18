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

        imageViewQrGuardado = view.findViewById(R.id.imageViewQrGuardado);
        cargarQrGuardado(); // Llama a la función para cargar el QR guardado

        // Establecer OnLongClickListener para borrar el código QR
        imageViewQrGuardado.setOnLongClickListener(v -> {
            mostrarDialogoBorrarQr();
            return true; // Indica que el evento de long click ha sido consumido
        });

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

    private void toggleVisibility(TextView textView, ImageView imageView, String dato) {
        if (textView.getVisibility() == View.VISIBLE) {
            textView.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.mostrar_ocultar); // Asegúrate de tener este drawable
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
            imageView.setImageResource(R.drawable.mostrar); // Asegúrate de tener este drawable
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
                        int checkBoxId = getResources().getIdentifier("checkBoxDato" + (i + 1), "id", requireContext().getPackageName());
                        int linearLayoutTextoId = getResources().getIdentifier("linearLayoutTexto" + obtenerNombreDato(i + 1), "id", requireContext().getPackageName());
                        int textId = getResources().getIdentifier("text" + (i + 1), "id", requireContext().getPackageName());
                        int infoTextId = getResources().getIdentifier("infoText" + (i + 1), "id", requireContext().getPackageName());

                        ImageView imageViewMostrarOcultar = linearLayoutHorizontal.findViewById(getResources().getIdentifier("imageViewMostrarOcultar" + obtenerNombreDato(i + 1), "id", requireContext().getPackageName()));

                        if (imageViewMostrarOcultar != null) {
                            // No necesitas lógica aquí para la visibilidad del ojo en la obtención de datos
                        }

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