package com.appex.bartobusiness.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.appex.bartobusiness.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionMenu fabMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);

        FloatingActionButton fabAddCard = (FloatingActionButton) findViewById(R.id.fab_add_card);
        FloatingActionButton fabScanQR = (FloatingActionButton) findViewById(R.id.fab_scan_qr);
        
        fabMenu.setClosedOnTouchOutside(true);
        fabMenu.hideMenuButton(false);
        fabMenu.showMenuButton(true);

        fabAddCard.setOnClickListener(this);
        fabScanQR.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.fab_add_card:
                startActivity(new Intent(getApplicationContext(), AddActivity.class));
                break;

            case R.id.fab_scan_qr:
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.initiateScan();
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                Toast.makeText(getApplicationContext(), result.getContents(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), NewCardActivity.class));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
