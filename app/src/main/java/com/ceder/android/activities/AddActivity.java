package com.ceder.android.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ceder.android.R;
import com.ceder.android.fragments.QRFragment;
import com.ceder.android.models.Card;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    Button addButton;
    EditText nameEditText, positionEditText, companyEditText;
    EditText emailEditText, phoneEditText, linkedinEditText;
    EditText addLine2EditText, addLine3EditText, addLine1EditText;
    ScrollView scrollView;
    public final static int QRcodeWidth = 500, WRITE_PERMISSION = 1;
    Bitmap bitmap;
    ByteArrayOutputStream outputStream;
    File file;
    String key, name;
    FileOutputStream fileOutputStream;
    SharedPreferences preferences;
    ProgressDialog progressDialog;
    Uri downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();

        addButton = (Button) findViewById(R.id.button);
        nameEditText = (TextInputEditText) findViewById(R.id.name_edit_text);
        positionEditText = (TextInputEditText) findViewById(R.id.position_edit_text);
        companyEditText = (TextInputEditText) findViewById(R.id.company_edit_text);
        phoneEditText = (TextInputEditText) findViewById(R.id.phone_edit_text);
        emailEditText = (TextInputEditText) findViewById(R.id.email_edit_text);
        addLine1EditText = (TextInputEditText) findViewById(R.id.add_line1_edit_text);
        addLine2EditText = (TextInputEditText) findViewById(R.id.add_line2_edit_text);
        addLine3EditText = (TextInputEditText) findViewById(R.id.add_line3_edit_text);
        linkedinEditText = (TextInputEditText) findViewById(R.id.linkedin_edit_text);
        scrollView = (ScrollView) findViewById(R.id.main_activity_scroll_view);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding to DataBase");
        progressDialog.setCancelable(false);

        outputStream = new ByteArrayOutputStream();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = nameEditText.getText().toString();
                if (isInternetConnected()) {

                    progressDialog.show();
                    String linkedinURL = linkedinEditText.getText().toString();
                    if (linkedinURL.isEmpty())
                        linkedinURL = "NA";
                    Card card = new Card(nameEditText.getText().toString(),
                            positionEditText.getText().toString(),
                            companyEditText.getText().toString(),
                            phoneEditText.getText().toString(),
                            emailEditText.getText().toString(),
                            addLine1EditText.getText().toString(),
                            addLine2EditText.getText().toString(),
                            addLine3EditText.getText().toString(),
                            linkedinURL);

                    key = databaseReference.child("cards").push().getKey();
                    databaseReference.child("cards").child(key).setValue(card);

                    Snackbar.make(scrollView, "Added", Snackbar.LENGTH_SHORT).show();


                } else
                    Snackbar.make(scrollView, "Check Internet Connection", Snackbar.LENGTH_SHORT).show();
                progressDialog.dismiss();
                progressDialog.setMessage("Uploading QR to Server");
                progressDialog.setCancelable(false);
                progressDialog.show();

                try {
                    bitmap = textToImageEncode(key);
                    Log.d("BIT", bitmap.toString());
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(AddActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
                    } else {

                        Log.d("BIT", bitmap.toString());
                        Log.d("KEY", key);
                        writeToStorage(bitmap, key);
                    }
                }
                else
                    writeToStorage(bitmap, key);
            }
        });
    }

    public boolean isInternetConnected() {
        boolean isConnected;
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = (activeNetwork != null)
                && (activeNetwork.isConnectedOrConnecting());
        return isConnected;
    }

    Bitmap textToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        ContextCompat.getColor(AddActivity.this, R.color.black) :
                        ContextCompat.getColor(AddActivity.this, R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    void writeToStorage(Bitmap bitmap, String key) {

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        uploadOnServer(outputStream);
        File folder = new File(Environment.getExternalStorageDirectory() + "/Pictures/B2B/");
        Boolean success = folder.exists();
        if (!success)
            success = folder.mkdir();

        if (success) {
            file = new File(Environment.getExternalStorageDirectory() + "/Pictures/B2B/" + key.substring(0, 5) + name + ".PNG");
            try {
                file.createNewFile();
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(outputStream.toByteArray());
                fileOutputStream.close();
                Snackbar.make(scrollView, "Uploaded", Snackbar.LENGTH_INDEFINITE)
                        .setAction("VIEW", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                QRFragment qrFragment = new QRFragment();
                                Bundle bundle = new Bundle();
                                bundle.putByteArray("QR", outputStream.toByteArray());
                                qrFragment.setArguments(bundle);
                                qrFragment.show(getSupportFragmentManager(), "qr fragment");
                            }
                        })
                        .setActionTextColor(ContextCompat.getColor(AddActivity.this,R.color.colorPrimaryDark)).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void uploadOnServer(final ByteArrayOutputStream outputStream) {

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        preferences = getSharedPreferences("com.appex.bartobusiness", MODE_PRIVATE);
        StorageReference childReference = storageReference.child("userqr" + preferences.getString("userid", "null"));
        StorageReference imageReference = childReference.child(key.substring(0, 5) + name + ".PNG");
        byte[] data = outputStream.toByteArray();

        UploadTask uploadTask = imageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(AddActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadUrl = taskSnapshot.getDownloadUrl();
                UrlTask urlTask = new UrlTask();
                urlTask.execute(downloadUrl.toString());
                progressDialog.dismiss();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        switch (requestCode) {

            case WRITE_PERMISSION:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    writeToStorage(bitmap, key);
                } else
                    Toast.makeText(AddActivity.this, "Cannot Save", Toast.LENGTH_SHORT).show();
        }

    }

    private class UrlTask extends AsyncTask<String, Void, Void> {

        protected Void doInBackground(String... params) {

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference();
            Map<String, Object> childUpdates = new HashMap<>();
            String key = databaseReference.child("urls").child(preferences.getString("userid", "null")).push().getKey();
            childUpdates.put("/" + key + "/", params[0]);
            databaseReference.child("urls").child(preferences.getString("userid", "null")).updateChildren(childUpdates);
            return null;
        }

    }
}