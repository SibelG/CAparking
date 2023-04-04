package com.example.caparking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.caparking.Helper.HelperUtilities;
import com.example.caparking.Model.Card;
import com.example.caparking.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CardArrayAdapter extends ArrayAdapter<Card> {
    private Context context;
    private List<Card> cardArrayList;

    public CardArrayAdapter(Context context, List<Card> cardList) {
        super(context, 0, cardList);
        this.context = context;
        this.cardArrayList = cardList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_card, parent, false
            );
        }

        TextView card_name = (TextView) convertView.findViewById(R.id.card_name);
        TextView card_number = (TextView) convertView.findViewById(R.id.card_number);
        ImageView card_image = (ImageView) convertView.findViewById(R.id.card_image);

        Card currentItem = getItem(position);

        if (currentItem != null) {
           card_name.setText(HelperUtilities.maskCardNumber(currentItem.getCard_number()));
           card_number.setText(currentItem.getCard_number().substring(0,2)+"**********"+currentItem.getCard_number().substring(12,16));

            if(HelperUtilities.getCredidCard(currentItem.getCard_number()).equalsIgnoreCase("Visa")){

                Picasso.with(context)
                        .load(R.drawable.visa)
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .error(R.drawable.ic_error_black_24dp)
                        .into(card_image);

                card_name.setText("Visa");
            }else if(HelperUtilities.getCredidCard(currentItem.getCard_number()).equalsIgnoreCase("MasterCard")){

                Picasso.with(context)
                        .load(R.drawable.mastercard)
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .error(R.drawable.ic_error_black_24dp)
                        .into(card_image);

                card_name.setText("Master Card");
            }else if(HelperUtilities.getCredidCard(currentItem.getCard_number()).equalsIgnoreCase("American Express")){

                Picasso.with(context)
                        .load(R.drawable.american_express)
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .error(R.drawable.ic_error_black_24dp)
                        .into(card_image);

                card_name.setText("American Express");

            }else {

                Picasso.with(context)
                        .load(R.drawable.dinersclub)
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .error(R.drawable.ic_error_black_24dp)
                        .into(card_image);

                card_name.setText("DinersClub");
            }
        }

        return convertView;
    }
}
