package com.appex.barcards.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appex.barcards.R;
import com.appex.barcards.models.RealmCard;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    static class CardViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, positionTextView, companyTextView;
        TextView emailTextView, phoneTextView, addLine1TextView;
        TextView addLine2TextView;
        CardView cardView;

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
        }
    }

    private List<RealmCard> realmCardList;
    private Context context;

    public CardAdapter(List<RealmCard> realmCardList, Context context) {

        this.realmCardList = realmCardList;
        this.context = context;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_item, parent, false);
        return new CardViewHolder(view);
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
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("LINKEDIN", card.getLinkedin());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {

        return realmCardList.size();
    }
}
