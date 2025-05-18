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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentQr#} factory method to
 * create an instance of this fragment.
 */
public class FragmentQr extends Fragment {
    private ImageView imageViewBack;
    private ImageView imageViewQrCode;
    private NavController navController;

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

        imageViewBack = view.findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(v -> navController.navigateUp());

        // Recibir los datos seleccionados del Fragment anterior
        Bundle args = getArguments();
        if (args != null) {
            ArrayList<String> datosSeleccionados = args.getStringArrayList("datos");
            if (datosSeleccionados != null && !datosSeleccionados.isEmpty()) {
                // Unir los datos seleccionados en un solo String para el código QR
                String dataToEncode = String.join("\n", datosSeleccionados);
                Bitmap qrBitmap = generarQrCode(dataToEncode);
                if (qrBitmap != null) {
                    guardarQrInterno(qrBitmap); // Llama a la función para guardar el QR
                }
            }
        }
    }

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

    private void guardarQrInterno(Bitmap bitmapQr) {
        File directorioInterno = requireContext().getFilesDir();
        File archivoQr = new File(directorioInterno, "codigo_qr.png");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(archivoQr);
            bitmapQr.compress(Bitmap.CompressFormat.PNG, 100, fos);
            // Opcional: Mostrar un mensaje de éxito
            // Toast.makeText(requireContext(), "Código QR guardado", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            // Opcional: Mostrar un mensaje de error
            // Toast.makeText(requireContext(), "Error al guardar el código QR", Toast.LENGTH_SHORT).show();
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
}