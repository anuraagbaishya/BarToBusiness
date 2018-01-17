package com.ceder.android.fragments;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ceder.android.R;
import com.ceder.android.activities.MainActivity;
import com.ceder.android.models.Card;
import com.ceder.android.models.RealmCard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.realm.Realm;

import static android.content.Context.MODE_PRIVATE;


public class NewCardFragment extends DialogFragment {

    public NewCardFragment(){

    }

    String key;
    Realm realm;
    ProgressDialog progressDialog;
    TextView nameTextView, positionTextView, companyTextView;
    TextView emailTextView, phoneTextView, addLine1TextView;
    TextView addLine2TextView;
    CardView cardView;
    Button saveCardButton, viewCardsButton;
    SharedPreferences preferences;
    Card card;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        key = getArguments().getString("Key");

        Realm.init(getActivity().getApplicationContext());
        realm = Realm.getDefaultInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_card, container, false);
        nameTextView = (TextView) rootView.findViewById(R.id.name_text_view);
        positionTextView = (TextView) rootView.findViewById(R.id.position_text_view);
        companyTextView = (TextView) rootView.findViewById(R.id.company_text_view);
        phoneTextView = (TextView) rootView.findViewById(R.id.phone_text_view);
        emailTextView = (TextView) rootView.findViewById(R.id.email_text_view);
        addLine1TextView = (TextView) rootView.findViewById(R.id.add_line1_text_view);
        addLine2TextView = (TextView) rootView.findViewById(R.id.add_line2_text_view);
        cardView = (CardView) rootView.findViewById(R.id.new_card_view);
        saveCardButton = (Button) rootView.findViewById(R.id.save_card_button);
        viewCardsButton = (Button) rootView.findViewById(R.id.view_card_button);

        preferences = getActivity().getSharedPreferences("com.ceder.android", MODE_PRIVATE);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for(final DataSnapshot postSnapShot : snapshot.getChildren()) {
                        if (postSnapShot.getKey().equals(key)) {
                            card = postSnapShot.getValue(Card.class);
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            nameTextView.setText(card.getName());
                            positionTextView.setText(card.getPosition());
                            companyTextView.setText(card.getCompany());
                            phoneTextView.setText(card.getPhone());
                            emailTextView.setText(card.getEmail());
                            addLine1TextView.setText(card.getAddLine1());
                            addLine2TextView.setText(card.getAddLine2()+", "+card.getAddLine3());
                            cardView.setVisibility(View.VISIBLE);

                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {

                                    RealmCard realmCard = realm.createObject(RealmCard.class);
                                    realmCard.setName(card.getName());
                                    realmCard.setPosition(card.getPosition());
                                    realmCard.setCompany(card.getCompany());
                                    realmCard.setPhone(card.getPhone());
                                    realmCard.setEmail(card.getEmail());
                                    realmCard.setAddLine1(card.getAddLine1());
                                    realmCard.setAddLine2(card.getAddLine2());
                                    realmCard.setAddLine3(card.getAddLine3());
                                    realmCard.setLinkedin(card.getLinkedin());
                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DBError", databaseError.toString());
            }
        });

        saveCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.NAME, card.getName());
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, card.getPhone());
                intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MAIN);
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, card.getEmail());
                intent.putExtra(ContactsContract.Intents.Insert.COMPANY, card.getCompany());
                intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, card.getPosition());
                String address = card.getAddLine1()+", "+card.getAddLine2()+", "+card.getAddLine3();
                intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
                startActivity(intent);
            }
        });

        viewCardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        progressDialog = new ProgressDialog(getActivity(), getTheme());
        progressDialog.setMessage("Reading Data");
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    private void startMainActivity(){

        startActivity(new Intent(getActivity(), MainActivity.class)
                .putExtra("email", preferences.getString("email", "null"))
                .putExtra("name", preferences.getString("name", "null"))
                .putExtra("userid", preferences.getString("userid", "1234")));
    }

}
