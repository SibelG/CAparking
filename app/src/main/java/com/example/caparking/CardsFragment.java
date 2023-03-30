package com.example.caparking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.caparking.Adapter.CardAdapter;
import com.example.caparking.Adapter.SeatsAdapter;
import com.example.caparking.Helper.DBHelper;
import com.example.caparking.Model.Card;
import com.example.caparking.Model.Seats;
import com.example.caparking.databinding.FragmentCardsBinding;
import com.example.caparking.databinding.FragmentPurchaseBinding;
import com.example.caparking.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CardsFragment extends Fragment {

    //Users Session
    @Inject
    SessionManager manager;

    private RecyclerView card_list_view;
    private CardAdapter cardAdapter;
    private List<Card> card_list;
    DBHelper db;
    FragmentCardsBinding binding;


    public CardsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CardsFragment newInstance(String param1, String param2) {
        CardsFragment fragment = new CardsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new SessionManager(requireContext());

    }

    private void getCards() {
        db = new DBHelper(requireContext());
        int id = manager.getToken();
        card_list = new ArrayList<Card>();
        card_list = db.getCardFromDB(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCardsBinding.inflate(
                inflater, container, false);
        View view = binding.getRoot();

        getCards();
        card_list_view = binding.recyclerCardView;
        cardAdapter = new CardAdapter(requireContext(),card_list);
        card_list_view.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));
        card_list_view.setAdapter(cardAdapter);
        final LinearLayoutManager layout_manager = new LinearLayoutManager(requireContext());
        card_list_view.setLayoutManager(layout_manager);
        //here data must be an instance of the class MarsDataProvider
        return view;
    }
}