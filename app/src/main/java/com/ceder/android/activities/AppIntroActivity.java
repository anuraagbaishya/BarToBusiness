package com.ceder.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;

import com.ceder.android.R;
import com.ceder.android.utils.Strings;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class AppIntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance(Strings.addTitle, Strings.addDesc, R.drawable.add, ResourcesCompat.getColor(getResources(), R.color.colorAccent, null)));
        addSlide(AppIntroFragment.newInstance(Strings.scanTitle, Strings.scanDesc, R.drawable.scan, ResourcesCompat.getColor(getResources(), R.color.colorAccent, null)));
        addSlide(AppIntroFragment.newInstance(Strings.manageTitle, Strings.manageDesc, R.drawable.connect, ResourcesCompat.getColor(getResources(), R.color.colorAccent, null)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onDonePressed() {

        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onSkipPressed() {

        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
