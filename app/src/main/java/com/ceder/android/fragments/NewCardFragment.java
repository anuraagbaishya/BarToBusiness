package com.ceder.android.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ceder.android.R;
import com.ceder.android.models.Card;
import com.ceder.android.models.RealmCard;

import io.realm.Realm;


public class NewCardFragment extends DialogFragment {

    Card card;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        card = getArguments().getParcelable("card");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_card, container, false);

        TextView nameTextView = (TextView) rootView.findViewById(R.id.name_text_view);
        TextView positionTextView = (TextView) rootView.findViewById(R.id.position_text_view);
        TextView companyTextView = (TextView) rootView.findViewById(R.id.company_text_view);
        TextView phoneTextView = (TextView) rootView.findViewById(R.id.phone_text_view);
        TextView emailTextView = (TextView) rootView.findViewById(R.id.email_text_view);
        TextView addLine1TextView = (TextView) rootView.findViewById(R.id.add_line1_text_view);
        TextView addLine2TextView = (TextView) rootView.findViewById(R.id.add_line2_text_view);
        CardView cardView = (CardView) rootView.findViewById(R.id.new_card_item);
        Button saveCardButton = (Button) rootView.findViewById(R.id.save_card_button);
        Button viewCardsButton = (Button) rootView.findViewById(R.id.view_card_button);

        nameTextView.setText(card.getName());
        positionTextView.setText(card.getPosition());
        companyTextView.setText(card.getCompany());
        phoneTextView.setText(card.getPhone());
        emailTextView.setText(card.getEmail());
        addLine1TextView.setText(card.getAddLine1());
        addLine2TextView.setText(card.getAddLine2()+", "+card.getAddLine3());

        Realm.init(getActivity().getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
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

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

    }
}
