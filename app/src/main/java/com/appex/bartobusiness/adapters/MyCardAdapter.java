package com.appex.bartobusiness.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appex.bartobusiness.R;
import com.appex.bartobusiness.models.MyCard;

import java.util.List;

public class MyCardAdapter extends RecyclerView.Adapter<MyCardAdapter.MyCardViewHolder> {

    static class MyCardViewHolder extends RecyclerView.ViewHolder{

        LinearLayout linearLayout;
        LinearLayout smallLayout;
        ImageView qrImageView;
        ImageView qrImageViewLarge;
        TextView nameTextView;

        MyCardViewHolder(View view){

            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.my_card_layout);
            smallLayout = (LinearLayout) view.findViewById(R.id.small_layout);
            qrImageView = (ImageView) view.findViewById(R.id.myqr_image_view);
            qrImageViewLarge = (ImageView) view.findViewById(R.id.myqr_image_view_large);
            nameTextView = (TextView) view.findViewById(R.id.my_name_text_view);
            smallLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    qrImageViewLarge.setVisibility(View.VISIBLE);
                    smallLayout.setVisibility(View.GONE);
                }
            });
            qrImageViewLarge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    smallLayout.setVisibility(View.VISIBLE);
                    qrImageViewLarge.setVisibility(View.GONE);
                }
            });
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

        MyCard myCard = myCards.get(position);
        holder.qrImageView.setImageBitmap(myCard.getQrBitmap());
        holder.qrImageViewLarge.setImageBitmap(myCard.getQrBitmap());
        holder.nameTextView.setText(myCard.getName());

    }

    @Override
    public int getItemCount(){

        return myCards.size();
    }


}
