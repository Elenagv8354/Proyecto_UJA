package com.example.walletssi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Button;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;
import java.io.InputStream;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;

/**
 * FragmentScan es el fragmento encargado de escanear códigos QR,
 * ya sea utilizando la cámara del dispositivo en tiempo real o decodificando
 * una imagen desde la galería.
 * Maneja los permisos de la cámara y la interacción con el usuario para el escaneo.
 */

public class FragmentScan extends Fragment {

    // Códigos de solicitud para permisos y resultados de actividades
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private static final int GALLERY_REQUEST_CODE = 201;

    // Componentes de la interfaz de usuario
    private ImageView imageViewBack;
    private NavController navController;
    private BarcodeView barcodeView;
    private Button importFromGalleryButton;

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                // Muestra el texto del código escaneado en un Toast.
                Toast.makeText(requireContext(), "Resultado: " + result.getText(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void possibleResultPoints(java.util.List resultPoints) {

        }
    };

    public FragmentScan() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Inicializa el NavController.
        navController = Navigation.findNavController(view);

        imageViewBack = view.findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(v -> navController.navigateUp());

        barcodeView = view.findViewById(R.id.barcode_view);
        importFromGalleryButton = view.findViewById(R.id.importFromGalleryButton);

        importFromGalleryButton.setOnClickListener(v -> openGallery());

        // Verificar y solicitar permiso de cámara si no está concedido
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // El permiso ya está concedido, iniciar el escaneo
            startScanning();
        }
    }

    // Inicia el proceso de decodificación continua de códigos utilizando la cámara del dispositivo.
    private void startScanning() {
        barcodeView.decodeContinuous(callback);
    }

    // Abre la galería de imágenes del dispositivo para que el usuario pueda seleccionar una imagen
    // y decodificar un código QR de ella.
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Maneja el resultado de la selección de imagen de la galería.
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    InputStream inputStream = requireContext().getContentResolver().openInputStream(selectedImageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if (bitmap != null) {
                        decodeQRCodeFromBitmap(bitmap);
                    } else {
                        Toast.makeText(requireContext(), "Error al decodificar la imagen", Toast.LENGTH_SHORT).show();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Decodifica un código QR desde un objeto Bitmap.
    // Convierte el Bitmap a un formato legible por la librería de escaneo
    // y muestra el resultado (o un mensaje de error).
    private void decodeQRCodeFromBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        LuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
        MultiFormatReader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(binaryBitmap);
            if (result != null) {
                Toast.makeText(requireContext(), "Código escaneado desde galería: " + result.getText(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireContext(), "No se encontró ningún código en la imagen", Toast.LENGTH_LONG).show();
            }
        } catch (NotFoundException e) {
            Toast.makeText(requireContext(), "No se encontró ningún código en la imagen", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reanuda la cámara para el escaneo cuando el fragmento se vuelve visible, solo si el permiso de la cámara está concedido.
        if (barcodeView != null && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            barcodeView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Pausa la cámara cuando el fragmento deja de ser visible.
        if (barcodeView != null) {
            barcodeView.pause();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso de cámara concedido, iniciar el escaneo
                startScanning();
            } else {
                Toast.makeText(requireContext(), "Permiso de cámara necesario", Toast.LENGTH_SHORT).show();
            }
        }
    }
}