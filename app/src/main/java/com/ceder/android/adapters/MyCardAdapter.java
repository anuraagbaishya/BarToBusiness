package com.ceder.android.adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ceder.android.R;
import com.ceder.android.models.MyCard;

import java.util.List;
import java.util.ResourceBundle;

public class MyCardAdapter extends RecyclerView.Adapter<MyCardAdapter.MyCardViewHolder> {

    static class MyCardViewHolder extends RecyclerView.ViewHolder{

        LinearLayout linearLayout;
        LinearLayout smallLayout;
        ImageView qrImageView;
        TextView counterTextView;
        TextView nameTextView;

        MyCardViewHolder(View view){

            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.my_card_layout);
            smallLayout = (LinearLayout) view.findViewById(R.id.small_layout);
            qrImageView = (ImageView) view.findViewById(R.id.myqr_image_view);
            nameTextView = (TextView) view.findViewById(R.id.my_name_text_view);
            counterTextView = (TextView) view.findViewById(R.id.counter_text_view);
        }
    }

    private List<MyCard> myCards;

    public MyCardAdapter(List<MyCard> myCards) {
        this.myCards = myCards;
    }

    @Override
    public MyCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mycard_item, parent, false);
        return new MyCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyCardViewHolder holder, int position){

        Resources res = holder.itemView.getContext().getResources();
        MyCard myCard = myCards.get(position);
        holder.qrImageView.setImageBitmap(myCard.getQrBitmap());
        holder.nameTextView.setText(myCard.getName());
        holder.counterTextView.setText(res.getString(R.string.counter, position+1, getItemCount()));

    }

    @Override
    public int getItemCount(){

        return myCards.size();
    }
}
