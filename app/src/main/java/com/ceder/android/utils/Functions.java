package com.ceder.android.utils;

import android.util.Log;

import com.ceder.android.utils.interfaces.OnGetDataListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Functions {

    public void newCard(final String key, final OnGetDataListener listener){

        listener.onStart();
        Log.d("key", key);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (final DataSnapshot postSnapShot : snapshot.getChildren()) {
                        if (postSnapShot.getKey().equals(key)) {
                            listener.onSuccess(postSnapShot);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure();
                Log.e("DBError", databaseError.toString());
            }
        });
    }
}
