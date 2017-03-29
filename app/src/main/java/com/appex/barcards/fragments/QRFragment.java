package com.appex.barcards.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appex.barcards.R;

public class QRFragment extends DialogFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_qr, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.qr_image_view);
        byte[] bytes = getArguments().getByteArray("QR");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        imageView.setImageBitmap(bitmap);
        return rootView;
    }
}
