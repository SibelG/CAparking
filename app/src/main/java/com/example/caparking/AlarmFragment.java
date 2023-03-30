package com.example.caparking;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caparking.databinding.FragmentAlarmBinding;
import com.example.caparking.databinding.FragmentEditProfileBinding;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class AlarmFragment extends Fragment {

    TextView parkName, parkTime, parkQty;
    Button tookPark, snooze;
    FragmentAlarmBinding binding;

    public AlarmFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AlarmFragment newInstance(String param1, String param2) {
        AlarmFragment fragment = new AlarmFragment();
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
        binding = FragmentAlarmBinding.inflate(
                inflater, container, false);

        View view = binding.getRoot();


        Intent intent = requireActivity().getIntent();
        binding.alarmParkName.setText(intent.getStringExtra("parkName"));
        binding.alarmParkTime.setText("Time: "+intent.getStringExtra("parkTime"));
        binding.alarmParkQuantity.setText("Qty: "+intent.getStringExtra("parkQty"));

        binding.alarmTook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
            }
        });

        binding.alarmSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(),"Reminder set after 10 minutes.",Toast.LENGTH_LONG).show();
                requireActivity().finish();
            }
        });

        return view;

    }
}