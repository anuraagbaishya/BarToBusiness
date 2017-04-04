package com.appex.barcards.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import com.appex.barcards.R;

public class FilterFragment extends BottomSheetDialogFragment {

    private OnFilterAppliedListener listener;
    private OnFilterClearedListener listener1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_filter, container, false);
        TextView applyFilterTextView = (TextView) rootView.findViewById(R.id.filter_apply_text_view);
        TextView clearFilterTextView = (TextView) rootView.findViewById(R.id.clear_filters);
        final EditText companyEditText = (EditText) rootView.findViewById(R.id.company_filter_edit_text);
        final EditText categoryEditText = (EditText) rootView.findViewById(R.id.category_filter_edit_text);
        final EditText locationEditText = (EditText) rootView.findViewById(R.id.location_filter_edit_text);
        final EditText nameEditText = (EditText) rootView.findViewById(R.id.name_filter_edit_text);

        clearFilterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener1.onFilterCleared();
                FilterFragment.this.dismiss();
            }
        });
        applyFilterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("Company", companyEditText.getText().toString());
                bundle.putString("Name", nameEditText.getText().toString());
                bundle.putString("Position", categoryEditText.getText().toString());
                bundle.putString("Location", locationEditText.getText().toString());
                listener.onFilterApplied(bundle);
                FilterFragment.this.dismiss();
            }
        });

        return rootView;
    }
    public  interface  OnFilterClearedListener {
        void onFilterCleared();
    }
    public interface OnFilterAppliedListener {

        void onFilterApplied(Bundle bundle);
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        Activity activity = getActivity();
        if (context instanceof Activity)
            activity = (Activity) context;
        try {
            listener = (OnFilterAppliedListener) activity;

        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFilterAppliedListener");
        }
        try {
            listener1 = (OnFilterClearedListener ) activity;
        }catch (final  ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnFilterClearedListener");
        }

    }
}
