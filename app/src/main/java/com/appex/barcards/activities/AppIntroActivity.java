package com.appex.barcards.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;

import com.appex.barcards.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class AppIntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("XYZ", "ABC", R.drawable.bcbanner, ResourcesCompat.getColor(getResources(),R.color.colorPrimary, null)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onDonePressed(){

        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}
