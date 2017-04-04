package com.appex.barcards.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.appex.barcards.R;
import com.appex.barcards.activities.MainActivity;
import com.appex.barcards.activities.MapsActivity;
import com.appex.barcards.models.RealmCard;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder>  {

    private static final int CALL_PERMISSION = 1;
    private String telno;
    private PermissionRequestResult permissionRequestResult;


    static class CardViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, positionTextView, companyTextView;
        TextView emailTextView, phoneTextView, addLine1TextView;
        TextView addLine2TextView;
        CardView cardView;
        ImageView linkedInImageView, mapImageView;

        CardViewHolder(View view) {

            super(view);

            cardView = (CardView) view.findViewById(R.id.card_list_item);
            nameTextView = (TextView) view.findViewById(R.id.name_list_text_view);
            positionTextView = (TextView) view.findViewById(R.id.position_list_text_view);
            companyTextView = (TextView) view.findViewById(R.id.company_list_text_view);
            phoneTextView = (TextView) view.findViewById(R.id.phone_list_text_view);
            emailTextView = (TextView) view.findViewById(R.id.email_list_text_view);
            addLine1TextView = (TextView) view.findViewById(R.id.add_line1_list_text_view);
            addLine2TextView = (TextView) view.findViewById(R.id.add_line2_list_text_view);
            linkedInImageView = (ImageView) view.findViewById(R.id.linkedin_image_view);
            mapImageView = (ImageView) view.findViewById(R.id.map_image_view);
        }
    }

    private List<RealmCard> realmCardList;
    private Context context;

    public CardAdapter(List<RealmCard> realmCardList, Context context) {

        this.realmCardList = realmCardList;
        this.context = context;
    }

    public PermissionRequestResult getPermissionRequestResult() {
        return permissionRequestResult;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_item, parent, false);
        return new CardViewHolder(view);
    }

    private void showRationale(View view) {
        Snackbar.make(view, R.string.permission_message, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPermissionDialog();
                    }
                }).show();
    }

    private void showPermissionDialog() {
        ActivityCompat.requestPermissions((MainActivity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CALL_PERMISSION);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {

        final RealmCard card = realmCardList.get(position);
        holder.nameTextView.setText(card.getName());
        holder.positionTextView.setText(card.getPosition());
        holder.companyTextView.setText(card.getCompany());
        holder.phoneTextView.setText(card.getPhone());
        holder.emailTextView.setText(card.getEmail());
        holder.addLine1TextView.setText(card.getAddLine1());
        holder.addLine2TextView.setText(card.getAddLine2() + ", " + card.getAddLine3());
        if (!isLinkedInValid(card.getLinkedin()))
            holder.linkedInImageView.setVisibility(View.GONE);
        holder.linkedInImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri url = Uri.parse(card.getLinkedin());
                CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
                CustomTabsIntent intent = intentBuilder.build();
                intent.launchUrl((MainActivity) context, url);
            }
        });
        holder.mapImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, MapsActivity.class)
                        .putExtra("loc", card.getAddLine1() + " " + card.getAddLine2() + " " + card.getAddLine3()));
            }
        });

        telno = card.getPhone();

        holder.phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPhone(telno, view);
            }
        });

        holder.emailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder builder = new StringBuilder("mailto:" + Uri.encode(card.getEmail()));
                String uri = builder.toString();
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return realmCardList.size();
    }

    private boolean isLinkedInValid(String url) {
        String regex = "^https:\\/\\/[a-z]{2,3}\\.linkedin\\.com\\/.*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    private void callPhone(final String telno, View view) {

        this.permissionRequestResult = new PermissionRequestResult() {
            @Override
            public void onPermitted() {
                Log.d("BLAH", "BLAH");
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telno));
                try {
                    context.startActivity(intent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telno));
            context.startActivity(intent);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale((MainActivity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                showRationale(view);
            else {
                showPermissionDialog();
            }
        }
    }

    private interface PermissionRequestResult {
        void onPermitted();
    }


}