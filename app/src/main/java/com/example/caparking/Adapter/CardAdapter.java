package com.example.caparking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.caparking.Common.Common;
import com.example.caparking.Helper.HelperUtilities;
import com.example.caparking.Model.Card;
import com.example.caparking.Model.Seats;
import com.example.caparking.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder>{

    private List<Card> card_list;
    private Context context;
    public CardAdapter(Context context, List<Card> card_list) {
        this.context = context;
        this.card_list = card_list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView card_name, card_number;
        ImageView card_image;
        MyViewHolder(View view) {
            super(view);
            card_name = (TextView) view.findViewById(R.id.card_name);
            card_number = (TextView) view.findViewById(R.id.card_number);
            card_image = (ImageView) view.findViewById(R.id.card_image);

        }
    }
    @Override
    public CardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        // Return a new holder instance
        return new CardAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardAdapter.MyViewHolder holder, int position) {
        final Card detail = card_list.get(position);
        holder.card_name.setText(HelperUtilities.maskCardNumber(detail.getCard_number()));
        holder.card_number.setText(detail.getCard_number().substring(0,2)+"**********"+detail.getCard_number().substring(12,16));

        if(HelperUtilities.getCredidCard(detail.getCard_number()).equalsIgnoreCase("Visa")){

            Picasso.with(context)
                    .load(R.drawable.visa)
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .into(holder.card_image);

            holder.card_name.setText("Visa");
        }else if(HelperUtilities.getCredidCard(detail.getCard_number()).equalsIgnoreCase("MasterCard")){

            Picasso.with(context)
                    .load(R.drawable.mastercard)
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .into(holder.card_image);

            holder.card_name.setText("Master Card");
        }else if(HelperUtilities.getCredidCard(detail.getCard_number()).equalsIgnoreCase("American Express")){

            Picasso.with(context)
                    .load(R.drawable.american_express)
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .into(holder.card_image);

            holder.card_name.setText("American Express");

        }else {

            Picasso.with(context)
                    .load(R.drawable.dinersclub)
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .into(holder.card_image);

            holder.card_name.setText("DinersClub");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            //ParkingAreas location = detail.getParkingAreas();
            @Override
            public void onClick(View v) {
                //Open cardDetailFragment
               /* card_detail_fragment = cardDetailFragment.newInstance(detail.getEmail(), detail.getLocation().getName(), detail.getLocation().getAddress(), detail.getAmount(),
                        detail.getLocation_id(),detail.getSpaceId(), detail.getStart_time(), detail.getEnd_time(),detail.getProgress());
                setFragment(card_detail_fragment, card_DETAIL_FRAGMENT);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return card_list.size();
    }
}
