package com.example.caparking;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.caparking.Adapter.SeatsAdapter;
import com.example.caparking.Helper.DBHelper;
import com.example.caparking.Model.Seats;
import com.example.caparking.databinding.FragmentProfileBinding;
import com.example.caparking.databinding.FragmentPurchaseBinding;
import com.example.caparking.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PurchaseFragment extends Fragment {



    //Users Session
    private SessionManager manager;

    private RecyclerView purchase_list_view;
    private SeatsAdapter seatsAdapter;
    private List<Seats> purchase_list;
    DBHelper db;

    private FragmentPurchaseBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentPurchaseBinding.inflate(
                inflater, container, false);
        View view = binding.getRoot();
        manager = new SessionManager(requireContext());
        getPurchases();
        purchase_list_view = binding.recyclerPurchaseView;
        seatsAdapter = new SeatsAdapter(requireContext(),purchase_list);
        purchase_list_view.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));
        purchase_list_view.setAdapter(seatsAdapter);
        final LinearLayoutManager layout_manager = new LinearLayoutManager(requireContext());
        purchase_list_view.setLayoutManager(layout_manager);
        //here data must be an instance of the class MarsDataProvider
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void getPurchases() {
        db = new DBHelper(requireContext());
        int id = manager.getToken();
        Log.d("iddd", String.valueOf(id));
        purchase_list = new ArrayList<Seats>();
        purchase_list = db.getDataFromDB(id);
    }
}