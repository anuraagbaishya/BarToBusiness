package com.appex.barcards.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.appex.barcards.R;
import com.appex.barcards.adapters.MyCardAdapter;
import com.appex.barcards.models.MyCard;
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

public class MyCardsActivity extends AppCompatActivity {

    ArrayList<MyCard> myCards;
    File[] listFile;
    public static float screen_width = 0;
    ProgressDialog progressDialog;
    MyCardAdapter myCardAdapter;
    Button syncButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cards);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screen_width = displayMetrics.widthPixels;
        syncButton = (Button) findViewById(R.id.sync_button);

        display();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting cards");
        progressDialog.setCancelable(true);

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                GetCardsTask getCardsTask = new GetCardsTask();
                getCardsTask.execute();
            }
        });
    }

    public ArrayList<MyCard> getFromSdcard()
    {
        ArrayList<MyCard> myCardArrayList = new ArrayList<>();

        File file= new File(Environment.getExternalStorageDirectory() + "/Pictures/B2B/");
        if (file.isDirectory())
        {
            listFile = file.listFiles();
            Log.d("SIZE", Integer.toString(listFile.length));

            for (int i = 0; i < listFile.length; i++) {

                MyCard myCard = new MyCard();
                myCard.setQrBitmap(BitmapFactory.decodeFile(listFile[i].getAbsolutePath()));
                myCard.setName(listFile[i].getName().substring(5, listFile[i].getName().indexOf(".PNG")));
                myCardArrayList.add(myCard);
            }
        }
        return myCardArrayList;
    }

    private class GetCardsTask extends AsyncTask<Void, Void, Void>{

        @Override
        public Void doInBackground(Void... params){

            final ArrayList<String> urlList = new ArrayList<>();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference();
            databaseReference.keepSynced(true);
            databaseReference.child("urls").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren())
                        for(DataSnapshot urlSnapshot: snapshot.getChildren()) {
                            urlList.add(urlSnapshot.getValue(String.class));
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

    private void imageDownload(ArrayList<String> urlList){

        for(String str: urlList){
            Log.d("url", str.substring(str.lastIndexOf("-", str.indexOf(".PNG")), str.indexOf(".PNG")));
            Picasso.with(getApplicationContext()).load(str).into(getTarget(str.substring(str.lastIndexOf("-", str.indexOf(".PNG")), str.indexOf(".PNG"))));

        }
        syncButton.setVisibility(View.GONE);
        display();

    }

    private static Target getTarget(final String filename){
        Target target = new Target(){

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

    private void display(){

        Log.d("Display", "Display");
        myCards = new ArrayList<>();
        myCards.clear();
        myCards.addAll(getFromSdcard());
        Log.d("ALSIZE", Integer.toString(myCards.size()));
        if (myCards.size() == 0)
            syncButton.setVisibility(View.VISIBLE);
        else {
            myCardAdapter = new MyCardAdapter(myCards);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.mycard_recycler_view);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(myCardAdapter);
        }
    }

}
