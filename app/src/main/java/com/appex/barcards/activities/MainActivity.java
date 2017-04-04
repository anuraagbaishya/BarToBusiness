package com.appex.barcards.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.appex.barcards.R;
import com.appex.barcards.adapters.CardAdapter;
import com.appex.barcards.fragments.FilterFragment;
import com.appex.barcards.models.RealmCard;
import com.appex.barcards.utils.CircleTransform;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FilterFragment.OnFilterAppliedListener, FilterFragment.OnFilterClearedListener {

    CardAdapter cardAdapter;
    RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    Boolean backPressed = false;
    String company = "", name = "", location = "", category = "";
    TextView noCardTextView ;
    ArrayList<RealmCard> realmCards;
    ArrayList<RealmCard> originalCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initNavigationDrawer();

        FloatingActionMenu fabMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        FloatingActionButton fabAddCard = (FloatingActionButton) findViewById(R.id.fab_add_card);
        FloatingActionButton fabScanQR = (FloatingActionButton) findViewById(R.id.fab_scan_qr);
        FloatingActionButton fabMyCards = (FloatingActionButton) findViewById(R.id.fab_mycards);
        noCardTextView = (TextView) findViewById(R.id.no_card_text_view);
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmCard> realmResults = realm.where(RealmCard.class).findAll();
        realmCards = new ArrayList<>(realmResults.subList(0, realmResults.size()));
        originalCards = new ArrayList<>(realmResults.subList(0, realmResults.size()));
        if (realmCards.size() == 0) {

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

        switch (v.getId()) {

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
                Log.d("yo","yo");

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
                startActivity(new Intent(getApplicationContext(), NewCardActivity.class).putExtra("Key", key));
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.settings:
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.help:
                        Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                        drawerLayout.closeDrawers();
                        break;

                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        TextView navNameTextView = (TextView) header.findViewById(R.id.name_nav_text_view);
        TextView navEmailTextView = (TextView) header.findViewById(R.id.email_nav_text_view);
        ImageView navProfileImageView = (ImageView) header.findViewById(R.id.nav_profile_image_view);
        navNameTextView.setText(getIntent().getStringExtra("name"));
        navEmailTextView.setText(getIntent().getStringExtra("email"));
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        String imageURL = "https://graph.facebook.com/" + getIntent().getStringExtra("userid") + "/picture?width=1200";
        Picasso.with(getApplicationContext()).load(imageURL).transform(new CircleTransform()).into(navProfileImageView);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void  onFilterCleared(){
        for(int i =0;i<realmCards.size();i++)
            realmCards.remove(i);
        for (int i=0;i<originalCards.size();i++)
            realmCards.add(originalCards.get(i));
        cardAdapter.notifyDataSetChanged();
    }
    @Override
    public void onFilterApplied(Bundle bundle) {
        boolean companyflag=false,nameflag=false,categoryflag=false,locationflag=false;
        int countinner=0,countouter=0;
        Log.d("com",bundle.getString("Company"));
        Log.d("nam",bundle.getString("Name"));
        Log.d("pos",bundle.getString("Position"));
        Log.d("loc",bundle.getString("Location"));
        company = bundle.getString("Company");
        name = bundle.getString("Name");
        category = bundle.getString("Position");
        location = bundle.getString("Location");

        if (company != null && company.length() != 0) {
           companyflag=true;

        }
        if (name != null && name.length() != 0) {
            nameflag=true;
        }
        if (category != null && category.length() != 0) {
            categoryflag=true;
        }
        if (location != null && location.length() != 0) {
            locationflag=true;
        }


        for (int i= 0;i<realmCards.size();i++) {
            RealmCard card=realmCards.get(i);
            if (companyflag) {
                if (card.getCompany().equals( company))
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
            if (countinner != countouter ) {
                realmCards.remove(i);

            }
            countinner = countouter = 0;
        }



        if (realmCards.size() == 0) {

            noCardTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        cardAdapter.notifyDataSetChanged();

    }


}



