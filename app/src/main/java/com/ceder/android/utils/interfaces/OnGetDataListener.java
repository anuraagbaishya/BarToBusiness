package com.ceder.android.utils.interfaces;

import com.google.firebase.database.DataSnapshot;


public interface OnGetDataListener{

    void onStart();
    void onSuccess(DataSnapshot dataSnapshot);
    void onFailure();
}
