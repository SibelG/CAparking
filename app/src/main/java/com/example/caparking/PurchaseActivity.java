package com.example.caparking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.caparking.Adapter.SeatsAdapter;
import com.example.caparking.Model.Seats;
import com.example.caparking.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class PurchaseActivity extends AppCompatActivity {

    private final String ACCESS_TOKEN = "x-access-token";

    //Users Session
    private SessionManager manager;

    private RecyclerView purchase_list_view;
    private SeatsAdapter seatsAdapter;
    private List<Seats> purchase_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        manager = new SessionManager(getApplicationContext());
        getPurchases();
        purchase_list_view = findViewById(R.id.recycler_purchase_view);
        seatsAdapter = new SeatsAdapter(this,purchase_list);
        purchase_list_view.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        purchase_list_view.setAdapter(seatsAdapter);
        final LinearLayoutManager layout_manager = new LinearLayoutManager(getApplicationContext());
        purchase_list_view.setLayoutManager(layout_manager);
    }

    private void getPurchases() {
        purchase_list = new ArrayList<>();
    }
}