package com.ceder.android.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ceder.android.R;
import com.ceder.android.adapters.CardAdapter;
import com.ceder.android.fragments.FilterFragment;
import com.ceder.android.fragments.HelpFragment;
import com.ceder.android.fragments.NewCardFragment;
import com.ceder.android.models.RealmCard;
import com.ceder.android.utils.CircleTransform;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.ceder.android.activities.AddActivity.WRITE_PERMISSION;

public class MainActivity extends AppCompatActivity implements FilterFragment.OnFilterAppliedListener, FilterFragment.OnFilterClearedListener {

    CardAdapter cardAdapter;
    RecyclerView recyclerView;
    Toolbar toolbar;
    Boolean backPressed = false;
    String company = "", name = "", location = "", category = "";
    LinearLayout noCardLayout;
    ArrayList<RealmCard> realmCards;
    ArrayList<RealmCard> originalCards;
    BottomNavigationView bottomNavigationView;
    Bundle args;
    private boolean showDialog = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(8.0f);
        ImageView toolbarImageView = (ImageView) findViewById(R.id.toolbar_image_view);
        String imageURL = "https://graph.facebook.com/" + getIntent().getStringExtra("userid") + "/picture?width=1200";
        Picasso.with(getApplicationContext()).load(imageURL).transform(new CircleTransform()).into(toolbarImageView);
        toolbarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });
        noCardLayout = (LinearLayout) findViewById(R.id.no_card_layout);
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);

        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.action_view_cards);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.action_view_cards:
                                return true;

                            case R.id.action_scan_qr:
                                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                                intentIntegrator.setBarcodeImageEnabled(true);
                                intentIntegrator.setOrientationLocked(true);
                                intentIntegrator.setBeepEnabled(false);
                                intentIntegrator.initiateScan();
                                return true;

                            case R.id.action_new_card:
                                startActivity(new Intent(getApplicationContext(), AddActivity.class));
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                return true;
                        }
                        return false;
                    }
                }
        );

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmCard> realmResults = realm.where(RealmCard.class).findAll();
        realmCards = new ArrayList<>(realmResults.subList(0, realmResults.size()));
        originalCards = new ArrayList<>(realmResults.subList(0, realmResults.size()));
        if (realmCards.size() == 0) {

            noCardLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        cardAdapter = new CardAdapter(realmCards, MainActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cardAdapter);
    }

    @Override
    public void onBackPressed() {
        if (backPressed) {
            super.onBackPressed();
            return;
        }

        this.backPressed = true;
        Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressed = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.filter_menu_item:
                FilterFragment filterFragment = new FilterFragment();
                filterFragment.show(getSupportFragmentManager(), "Filter");
                break;
            case R.id.help:
                HelpFragment helpFragment = new HelpFragment();
                helpFragment.show(getSupportFragmentManager(), "help");
                break;
            case R.id.about:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
        return false;
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
                args = new Bundle();
                args.putString("Key", key);
                showDialog = true;

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResumeFragments() {

        super.onResumeFragments();
        if (showDialog) {
            showDialog = false;

            DialogFragment cardFragment = new NewCardFragment();
            cardFragment.setArguments(args);
            cardFragment.show(getSupportFragmentManager(), "New Card Fragment");
        }
    }

    @Override
    public void onFilterCleared() {
        for (int i = 0; i < realmCards.size(); i++)
            realmCards.remove(i);
        for (int i = 0; i < originalCards.size(); i++)
            realmCards.add(originalCards.get(i));
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFilterApplied(Bundle bundle) {
        boolean companyflag = false, nameflag = false, categoryflag = false, locationflag = false;
        int countinner = 0, countouter = 0;
        Log.d("com", bundle.getString("Company"));
        Log.d("nam", bundle.getString("Name"));
        Log.d("pos", bundle.getString("Position"));
        Log.d("loc", bundle.getString("Location"));
        company = bundle.getString("Company");
        name = bundle.getString("Name");
        category = bundle.getString("Position");
        location = bundle.getString("Location");

        if (company != null && company.length() != 0) {
            companyflag = true;

        }
        if (name != null && name.length() != 0) {
            nameflag = true;
        }
        if (category != null && category.length() != 0) {
            categoryflag = true;
        }
        if (location != null && location.length() != 0) {
            locationflag = true;
        }


        for (int i = 0; i < realmCards.size(); i++) {
            RealmCard card = realmCards.get(i);
            if (companyflag) {
                if (card.getCompany().equals(company))
                    countinner++;
                countouter++;
            }
            if (nameflag) {
                if (card.getName().equals(name))
                    countinner++;
                countouter++;
            }
            if (categoryflag) {
                if (card.getPosition().equals(category))
                    countinner++;
                countouter++;
            }
            if (locationflag) {
                if (card.getAddLine1().equals(location) || card.getAddLine2().equals(location) || card.getAddLine3().equals(location))
                    countinner++;
                countouter++;
            }
            if (countinner != countouter) {
                realmCards.remove(i);

            }
            countinner = countouter = 0;
        }


        if (realmCards.size() == 0) {

            noCardLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        cardAdapter.notifyDataSetChanged();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        switch (requestCode) {

            case WRITE_PERMISSION:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else
                    Toast.makeText(MainActivity.this, "Cannot read from storage", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.action_view_cards);
    }
}



