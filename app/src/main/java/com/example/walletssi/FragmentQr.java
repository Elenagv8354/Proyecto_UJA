package com.example.walletssi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Bitmap;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import android.Manifest;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;

import android.os.Environment;
import android.provider.MediaStore;

import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * FragmentQr es el fragmento responsable de mostrar un código QR generado
 * a partir de datos del usuario. Permite al usuario escanear, compartir
 * y descargar el código QR, además de manejar los permisos necesarios
 * para estas operaciones.
 */

public class FragmentQr extends Fragment {

    // Componentes de la interfaz de usuario
    private ImageView imageViewBack;
    private ImageView imageViewQrCode;
    private NavController navController;
    private Button buttonEscanear;
    private Button buttonCompartir;
    private Button buttonDescargar;
    private Bitmap qrBitmap;

    // Código de solicitud para el permiso de la cámara (para escanear)
    private static final int CAMERA_PERMISSION_REQUEST_CODE_SCAN = 201;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qr, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        imageViewQrCode = view.findViewById(R.id.imageViewQrCode);
        buttonEscanear = view.findViewById(R.id.buttonEscanear);
        buttonCompartir = view.findViewById(R.id.buttonCompartir);
        buttonDescargar = view.findViewById(R.id.buttonDescargar);

        imageViewBack = view.findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(v -> navController.navigateUp());

        buttonEscanear.setOnClickListener(v -> iniciarEscaneo());
        buttonCompartir.setOnClickListener(v -> compartirQr());
        buttonDescargar.setOnClickListener(v -> descargarQr());

        // Recibir los datos seleccionados del Fragment anterior
        Bundle args = getArguments();
        if (args != null) {
            ArrayList<String> datosSeleccionados = args.getStringArrayList("datos");
            if (datosSeleccionados != null && !datosSeleccionados.isEmpty()) {
                // Unir los datos seleccionados en un solo String para el código QR
                String dataToEncode = String.join("\n", datosSeleccionados);
                qrBitmap = generarQrCode(dataToEncode);
                if (qrBitmap != null) {
                    guardarQrInterno(qrBitmap); // Llama a la función para guardar el QR
                }
            }
        } else {
            cargarQrGuardadoInterno();
        }
    }

    //Carga un código QR guardado previamente desde el almacenamiento interno de la aplicación y lo muestra en el ImageView.
    private void cargarQrGuardadoInterno() {
        File directorioInterno = requireContext().getFilesDir();
        File archivoQr = new File(directorioInterno, "codigo_qr.png");

        if (archivoQr.exists()) {
            qrBitmap = android.graphics.BitmapFactory.decodeFile(archivoQr.getAbsolutePath());
            imageViewQrCode.setImageBitmap(qrBitmap);
        } else {
            Toast.makeText(requireContext(), "No se encontró código QR guardado internamente", Toast.LENGTH_SHORT).show();
        }
    }

    // Genera un código QR a partir de una cadena de texto
    private Bitmap generarQrCode(String data) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, 900, 900);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageViewQrCode.setImageBitmap(bitmap);
            return bitmap; // Devuelve el Bitmap generado
        } catch (WriterException e) {
            e.printStackTrace();
            return null; // Devuelve null si hay un error
        }
    }

    // Guarda el Bitmap del código QR en el almacenamiento interno de la aplicación.
    // Esto permite que el QR persista y pueda ser recuperado más tarde.
    private void guardarQrInterno(Bitmap bitmapQr) {
        File directorioInterno = requireContext().getFilesDir();
        File archivoQr = new File(directorioInterno, "codigo_qr.png");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(archivoQr);
            bitmapQr.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Inicia el proceso de escaneo de un código QR. Primero verifica el permiso
    // de la cámara; si no lo tiene, lo solicita. Si ya lo tiene, navega al FragmentScan.
    private void iniciarEscaneo() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE_SCAN);
        } else {
            navController.navigate(R.id.action_fragmentQr_to_fragmentScan);
        }
    }

    // Permite al usuario compartir el código QR generado a través de otras aplicaciones instaladas en el dispositivo.
    private void compartirQr() {
        if (qrBitmap != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");

            // Guarda la imagen en caché para poder compartirla
            Uri uri = guardarImagenEnCache(qrBitmap);

            if (uri != null) {
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(shareIntent, "Compartir código QR"));
            } else {
                Toast.makeText(requireContext(), "Error al compartir el código QR", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "No hay código QR para compartir", Toast.LENGTH_SHORT).show();
        }
    }

    // Guarda un Bitmap en el directorio de caché de la aplicación y devuelve su URI.
    // Esto es necesario para compartir archivos con otras aplicaciones de forma segura.
    private Uri guardarImagenEnCache(Bitmap bitmap) {
        try {
            File cachePath = new File(requireContext().getCacheDir(), "images");
            cachePath.mkdirs();
            File file = new File(cachePath, "shared_qr_code.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
            return FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".fileprovider", file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Descarga el código QR generado a la galería de imágenes del dispositivo.
    private void descargarQr() {
        if (qrBitmap != null) {
            String nombreArchivo = "codigo_qr_" + System.currentTimeMillis() + ".png";
            OutputStream fos;
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, nombreArchivo);
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                    values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/TuCarpetaQR");
                    Uri uri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    if (uri != null) {
                        fos = requireContext().getContentResolver().openOutputStream(uri);
                        qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.close();
                        Toast.makeText(requireContext(), "Código QR guardado en la galería", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Error al guardar el código QR", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    File directorio = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "TuCarpetaQR");
                    if (!directorio.exists()) {
                        directorio.mkdirs();
                    }
                    File archivo = new File(directorio, nombreArchivo);
                    fos = new FileOutputStream(archivo);
                    qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                    Toast.makeText(requireContext(), "Código QR guardado en la galería", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Error al guardar el código QR", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "No hay código QR para descargar", Toast.LENGTH_SHORT).show();
        }
    }

    // Maneja la respuesta a la solicitud de permisos.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE_SCAN) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarEscaneo();
            } else {
                Toast.makeText(requireContext(), "Permiso de cámara necesario para escanear", Toast.LENGTH_SHORT).show();
            }
        }
    }
}