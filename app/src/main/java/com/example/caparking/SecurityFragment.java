package com.example.caparking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SecurityFragment extends Fragment {



    public SecurityFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SecurityFragment newInstance(String param1, String param2) {
        SecurityFragment fragment = new SecurityFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_security, container, false);
    }
}