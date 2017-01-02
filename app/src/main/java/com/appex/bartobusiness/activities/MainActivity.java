package com.appex.bartobusiness.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.appex.bartobusiness.R;
import com.appex.bartobusiness.adapters.CardAdapter;
import com.appex.bartobusiness.models.RealmCard;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    CardAdapter cardAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionMenu fabMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        FloatingActionButton fabAddCard = (FloatingActionButton) findViewById(R.id.fab_add_card);
        FloatingActionButton fabScanQR = (FloatingActionButton) findViewById(R.id.fab_scan_qr);
        FloatingActionButton fabMyCards = (FloatingActionButton) findViewById(R.id.fab_mycards);
        TextView noCardTextView = (TextView) findViewById(R.id.no_card_text_view);
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmCard> realmResults = realm.where(RealmCard.class).findAll();
        ArrayList<RealmCard> realmCards = new ArrayList<>(realmResults.subList(0, realmResults.size()));
        if(realmCards.size() == 0){

            noCardTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        cardAdapter = new CardAdapter(realmCards, MainActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cardAdapter);

        fabMenu.setClosedOnTouchOutside(true);
        fabMenu.hideMenuButton(false);
        fabMenu.showMenuButton(true);

        fabAddCard.setOnClickListener(this);
        fabScanQR.setOnClickListener(this);
        fabMyCards.setOnClickListener(this);

        Log.d("ID", getIntent().getStringExtra("userid"));
        Log.d("Name", getIntent().getStringExtra("name"));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.fab_add_card:
                startActivity(new Intent(getApplicationContext(), AddActivity.class));
                break;

            case R.id.fab_scan_qr:
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.initiateScan();
                break;

            case R.id.fab_mycards:
                startActivity(new Intent(getApplicationContext(), MyCardsActivity.class));
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
                String key = result.getContents();
                startActivity(new Intent(getApplicationContext(), NewCardActivity.class).putExtra("Key",key));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
