package com.ceder.android.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ceder.android.R;
import com.ceder.android.adapters.MyCardAdapter;
import com.ceder.android.models.MyCard;
import com.ceder.android.utils.CircleTransform;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.ceder.android.activities.AddActivity.WRITE_PERMISSION;

public class ProfileActivity extends AppCompatActivity {

    ArrayList<MyCard> myCards = new ArrayList<>();
    File[] listFile = new File[10];
    ProgressDialog progressDialog;
    MyCardAdapter myCardAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView noCardTextView;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        noCardTextView = (TextView) findViewById(R.id.no_card_text_view);
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
            } else
                display();
        } else
            display();

        preferences = getSharedPreferences("com.ceder.android", MODE_PRIVATE);
        CollapsingToolbarLayout collapsingToolbarLayout =(CollapsingToolbarLayout)
                findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.collapsingToolbarLayoutTitleColorExpanded);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsingToolbarLayoutTitleColorCollapsed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(preferences.getString("name", null));
        toolbar.setSubtitle(preferences.getString("email", null));
        setSupportActionBar(toolbar);

        ImageView profileImageView = (ImageView) findViewById(R.id.profile_image_view);
        String imageURL = "https://graph.facebook.com/" + preferences.getString("userid", null) + "/picture?width=1200";
        Picasso.with(getApplicationContext()).load(imageURL).into(profileImageView);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                GetCardsTask getCardsTask = new GetCardsTask();
//                getCardsTask.execute();
//            }
//        });

    }

    public ArrayList<MyCard> getFromSdcard() {
        ArrayList<MyCard> myCardArrayList = new ArrayList<>();

        File file = new File(Environment.getExternalStorageDirectory() + "/Pictures/B2B/");
        if (file.isDirectory()) {
            listFile = file.listFiles();
            Log.d("SIZE", Integer.toString(listFile.length));

            for (int i = 0; i < listFile.length; i++) {

                MyCard myCard = new MyCard();
                myCard.setQrBitmap(BitmapFactory.decodeFile(listFile[i].getAbsolutePath()));
                myCard.setName(listFile[i].getName().substring(5, listFile[i].getName().indexOf(".PNG")).replace('+', ' '));
                myCardArrayList.add(myCard);
            }
        }
        Collections.sort(myCardArrayList, new Comparator<MyCard>() {
            @Override
            public int compare(MyCard myCard1, MyCard myCard2) {
                return myCard1.getName().compareToIgnoreCase(myCard2.getName());
            }
        });
        return myCardArrayList;
    }

    private class GetCardsTask extends AsyncTask<Void, Void, Void> {

        @Override
        public Void doInBackground(Void... params) {

            preferences = getSharedPreferences("com.ceder.android", MODE_PRIVATE);
            String userId = preferences.getString("userid", null);
            final ArrayList<String> urlList = new ArrayList<>();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference();
            databaseReference.keepSynced(true);
            databaseReference.child("urls").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d("URL", snapshot.getValue().toString());
                        urlList.add(snapshot.getValue(String.class));
                    }
                    imageDownload(urlList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }
    }

    private void imageDownload(ArrayList<String> urlList) {

        for (String str : urlList)
            Picasso.with(getApplicationContext()).load(str).into(getTarget(str.substring(str.lastIndexOf("-", str.indexOf(".PNG")), str.indexOf(".PNG"))));

        display();

    }

    private static Target getTarget(final String filename) {
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        FileOutputStream fileOutputStream;
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        File folder = new File(Environment.getExternalStorageDirectory() + "/Pictures/B2B/");
                        Boolean success = folder.exists();
                        if (!success)
                            success = folder.mkdir();

                        if (success) {
                            File file = new File(Environment.getExternalStorageDirectory() + "/Pictures/B2B/" + filename + ".PNG");
                            if (!file.exists()) {
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
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }

    private void display() {

        //swipeRefreshLayout.setRefreshing(false);
        myCards = new ArrayList<>();
        myCards.clear();
        myCards.addAll(getFromSdcard());
        Log.d("ListSize", Integer.toString(myCards.size()));
        myCardAdapter = new MyCardAdapter(myCards);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.mycard_recycler_view);
        if (myCards.size() == 0) {
            noCardTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noCardTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myCardAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        switch (requestCode) {

            case WRITE_PERMISSION:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    display();
                } else
                    Toast.makeText(ProfileActivity.this, "Cannot Save", Toast.LENGTH_SHORT).show();
        }

    }

}
