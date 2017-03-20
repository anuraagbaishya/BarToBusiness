package com.appex.barcards.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.appex.barcards.R;
import com.appex.barcards.utils.CircleTransform;
import com.squareup.picasso.Picasso;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        RotateAnimation anim = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.ABSOLUTE);
        anim.setDuration(1000);

        ImageView imageView = (ImageView) findViewById(R.id.logo_image_view);
        Picasso.with(getApplicationContext()).load(R.drawable.splash).transform(new CircleTransform()).into(imageView);
        imageView.startAnimation(anim);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Adequate-ExtraLight.ttf");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //  Initialize SharedPreferences
                        SharedPreferences getPrefs = PreferenceManager
                                .getDefaultSharedPreferences(getBaseContext());

                        //  Create a new boolean and preference and set it to true
                        boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                        //  If the activity has never started before...
                        if (isFirstStart) {

                            //  Launch app intro
                            Intent i = new Intent(getApplicationContext(), AppIntroActivity.class);
                            startActivity(i);

                            //  Make a new preferences editor
                            SharedPreferences.Editor e = getPrefs.edit();

                            //  Edit preference to make it false because we don't want this to run again
                            e.putBoolean("firstStart", false);

                            //  Apply changes
                            e.apply();
                        }
                        else
                            startLoginActivity();
                    }
                });

                thread.start();
            }
        }, 2000);
    }
    private void startLoginActivity(){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
}
