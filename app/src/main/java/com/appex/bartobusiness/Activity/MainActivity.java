package com.appex.bartobusiness.Activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.appex.bartobusiness.Fragment.QRFragment;
import com.appex.bartobusiness.Model.Card;
import com.appex.bartobusiness.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button addButton;
    EditText nameEditText, positionEditText, companyEditText;
    EditText emailEditText, phoneEditText, addLine1EditText;
    EditText addLine2EditText, addLine3EditText;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FirebaseApp.initializeApp(getApplicationContex
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();

        addButton = (Button) findViewById(R.id.button);
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        positionEditText = (EditText) findViewById(R.id.position_edit_text);
        companyEditText = (EditText) findViewById(R.id.company_edit_text);
        phoneEditText = (EditText) findViewById(R.id.phone_edit_text);
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        addLine1EditText = (EditText) findViewById(R.id.add_line1_edit_text);
        addLine2EditText = (EditText) findViewById(R.id.add_line2_edit_text);
        addLine3EditText = (EditText) findViewById(R.id.add_line3_edit_text);
        scrollView = (ScrollView) findViewById(R.id.main_activity_scroll_view);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Card card = new Card(nameEditText.getText().toString(),
                        positionEditText.getText().toString(),
                        companyEditText.getText().toString(),
                        phoneEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        addLine1EditText.getText().toString(),
                        addLine2EditText.getText().toString(),
                        addLine3EditText.getText().toString());

                String key = databaseReference.child("cards").push().getKey();
                databaseReference.child("cards").child(key).setValue(card);

                Snackbar.make(scrollView, "Added", Snackbar.LENGTH_LONG).show();

                Bundle bundle = new Bundle();
                bundle.putString("Key", key);
                QRFragment qrFragment = new QRFragment();
                qrFragment.setArguments(bundle);
                qrFragment.show(getSupportFragmentManager(), "qr fragment");

            }
        });
    }
}