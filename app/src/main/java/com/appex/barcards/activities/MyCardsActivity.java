package com.appex.barcards.activities;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;

import com.appex.barcards.R;
import com.appex.barcards.adapters.MyCardAdapter;
import com.appex.barcards.models.MyCard;

import java.io.File;
import java.util.ArrayList;

public class MyCardsActivity extends AppCompatActivity {

    ArrayList<MyCard> myCards;
    File[] listFile;
    public static float screen_width = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cards);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screen_width = displayMetrics.widthPixels;
        myCards = new ArrayList<>();
        myCards = getFromSdcard();
        Log.d("ALSIZE", Integer.toString(myCards.size()));
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.mycard_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        MyCardAdapter myCardAdapter = new MyCardAdapter(myCards);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myCardAdapter);
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
}
