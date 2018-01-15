package com.ceder.android.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ceder.android.R;

public class HelpFragment extends DialogFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);
        TextView helpTextView = (TextView) rootView.findViewById(R.id.help_text_view);
        helpTextView.setText("Click + to view options.\nClick Add Card to add your card" +
                "\nClick Scan QR to scan other's QR and view information" +
                "\nClick My Cards to view cards created previously" +
                "\nTap phone number and email on card to directly call phone or send email");
        return rootView;
    }

}
