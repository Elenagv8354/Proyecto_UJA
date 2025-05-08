package com.example.walletssi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentScan} factory method to
 * create an instance of this fragment.
 */
public class FragmentScan extends Fragment {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101; // Usar un código diferente si ya usaste 100
    private BarcodeView barcodeView;

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                // Aquí tienes el resultado del escaneo (texto del código)
                Toast.makeText(requireContext(), "Resultado: " + result.getText(), Toast.LENGTH_LONG).show();
                // Puedes detener el escaneo si solo necesitas un resultado
                // barcodeView.stopDecoding();
            }
        }

        @Override
        public void possibleResultPoints(java.util.List resultPoints) {
            // Puedes implementar lógica para mostrar puntos de detección si lo deseas
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

        barcodeView = view.findViewById(R.id.barcode_view);

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

    private void startScanning() {
        barcodeView.decodeContinuous(callback);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Iniciar la cámara en onResume
        if (barcodeView != null) {
            barcodeView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Pausar la cámara en onPause
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
                // Puedes mostrar un mensaje o navegar a otra pantalla si el permiso es denegado
            }
        }
    }
}