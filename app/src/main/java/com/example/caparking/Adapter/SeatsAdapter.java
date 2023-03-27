package com.example.caparking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.caparking.Model.ParkingAreas;
import com.example.caparking.Model.Seats;
import com.example.caparking.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class SeatsAdapter extends RecyclerView.Adapter<SeatsAdapter.MyViewHolder>{

    private List<Seats> purchase_list;
    private Context context;
    public SeatsAdapter(Context context, List<Seats> purchase_list) {
        this.context=context;
        this.purchase_list = purchase_list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView purchase_location_name, purchase_location_date, purchase_location_amount, purchase_location_paid;
        MyViewHolder(View view) {
            super(view);
            purchase_location_name = (TextView) view.findViewById(R.id.purchase_location_name);
            purchase_location_date = (TextView) view.findViewById(R.id.purchase_location_date);
            purchase_location_amount = (TextView) view.findViewById(R.id.purchase_location_amount);
            purchase_location_paid = (TextView) view.findViewById(R.id.purchase_location_paid);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View location_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        // Return a new holder instance
        return new MyViewHolder(location_view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Seats detail = purchase_list.get(position);
        holder.purchase_location_name.setText(detail.getParkingName());
        holder.purchase_location_date.setText(detail.getDate());
        //holder.purchase_location_amount.setText(NumberFormat.getCurrencyInstance().format(detail.getParkingAreas().getPerHourPrice()));
        if(detail.getSeatStatus()==1){
            holder.purchase_location_paid.setText("In Progress");
        }
        else{
            holder.purchase_location_paid.setText("Completed");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            //ParkingAreas location = detail.getParkingAreas();
            @Override
            public void onClick(View v) {
                //Open PurchaseDetailFragment
               /* purchase_detail_fragment = PurchaseDetailFragment.newInstance(detail.getEmail(), detail.getLocation().getName(), detail.getLocation().getAddress(), detail.getAmount(),
                        detail.getLocation_id(),detail.getSpaceId(), detail.getStart_time(), detail.getEnd_time(),detail.getProgress());
                setFragment(purchase_detail_fragment, PURCHASE_DETAIL_FRAGMENT);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return purchase_list.size();
    }
}
