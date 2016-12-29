package com.appex.bartobusiness.fragments;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.appex.bartobusiness.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class QRFragment extends DialogFragment {

    public final static int QRcodeWidth = 500;
    Bitmap bitmap;
    ByteArrayOutputStream outputStream;
    File file;
    String key, name;
    FileOutputStream fileOutputStream;
    private static final int WRITE_PERMISSION = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_qr, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.qr_image_view);
        outputStream = new ByteArrayOutputStream();

        key = getArguments().getString("Key");
        name = getArguments().getString("Name");
        name = name.replaceAll("\\s+","");
        try {

            bitmap = textToImageEncode(key);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }


        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            Log.d("BLAH", "BLAH");

            if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                Log.d("BLAH", "BLAH");
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
            }
        } else
            writeToStorage(bitmap, key);

        return rootView;
    }

    Bitmap textToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        ContextCompat.getColor(getActivity(), R.color.black) :
                        ContextCompat.getColor(getActivity(), R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    void writeToStorage(Bitmap bitmap, String key) {

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        File folder = new File(Environment.getExternalStorageDirectory() + "/Pictures/B2B/");
        Boolean success = folder.exists();
        if (!success)
            success = folder.mkdir();

        if (success) {
            file = new File(Environment.getExternalStorageDirectory() + "/Pictures/B2B/" + key.substring(0,5) + name + ".PNG");

            try {
                file.createNewFile();
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(outputStream.toByteArray());
                fileOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        switch (requestCode) {

            case WRITE_PERMISSION:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    writeToStorage(bitmap, key);
                } else
                    Toast.makeText(getActivity(), "Cannot Save", Toast.LENGTH_SHORT).show();
        }

    }
}
