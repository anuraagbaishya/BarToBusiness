package com.ceder.android.models;

import android.graphics.Bitmap;

public class MyCard {

    public Bitmap getQrBitmap() {
        return qrBitmap;
    }

    public void setQrBitmap(Bitmap qrBitmap) {
        this.qrBitmap = qrBitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Bitmap qrBitmap;
    private String name;
}
