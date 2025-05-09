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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentQr#} factory method to
 * create an instance of this fragment.
 */
public class FragmentQr extends Fragment {

    private ImageView imageViewQrCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qr, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageViewQrCode = view.findViewById(R.id.imageViewQrCode); // Asegúrate de tener un ImageView con este ID en fragment_qr.xml

        // Recibir los datos seleccionados del Fragment anterior
        Bundle args = getArguments();
        if (args != null) {
            ArrayList<String> datosSeleccionados = args.getStringArrayList("datos");
            if (datosSeleccionados != null && !datosSeleccionados.isEmpty()) {
                // Unir los datos seleccionados en un solo String para el código QR
                String dataToEncode = String.join("\n", datosSeleccionados);
                generarQrCode(dataToEncode);
            }
        }
    }

    private void generarQrCode(String data) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}