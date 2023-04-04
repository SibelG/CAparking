package com.example.caparking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caparking.Helper.DBHelper;
import com.example.caparking.databinding.FragmentAlarmBinding;
import com.example.caparking.databinding.FragmentEditProfileBinding;
import com.example.caparking.util.SessionManager;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class AlarmFragment extends Fragment {

    TextView parkName, parkTime, parkQty;
    Button tookPark, snooze;
    FragmentAlarmBinding binding;
    PendingIntent pendingIntent;

    SessionManager manager;

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
        ((MapsActivity)getActivity()).setNavigationVisibility(true);
        manager = new SessionManager(requireContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAlarmBinding.inflate(
                inflater, container, false);

        View view = binding.getRoot();


        Intent intent = requireActivity().getIntent();
        binding.alarmParkName.setText(manager.getName());
        binding.alarmParkTime.setText("Time: "+manager.getTime());
        binding.alarmParkQuantity.setText("Qty: "+manager.getPrice());



        binding.alarmTook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
            }
        });

        binding.alarmSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processinsert("dd","dd","dd");
            }
        });

        return view;

    }
    private void processinsert(String title, String date, String time) {
        DBHelper dbHelper = new DBHelper(requireContext());
        dbHelper.addreminder(title,date,time);//inserts the title,date,time into sql lite database
        setAlarm();                                                                //calls the set alarm method to set alarm
    }

    private void setAlarm() {

        Intent intent = new Intent(requireContext(),AlarmBroadcastReceiver.class);

        String dt="Sun Apr 02 20:55:00 GMT 2023";
        Date remind = new Date(dt.trim());
        //PendingIntent intent1 = PendingIntent.getBroadcast(requireContext(),1,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(requireContext(),1,
                    intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_CANCEL_CURRENT);
        } else {
            pendingIntent = PendingIntent.getBroadcast(requireContext(), 1,
                    intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }
        AlarmManager alarmManager = (AlarmManager)requireContext().getSystemService(requireContext().ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        calendar.setTime(remind);
        calendar.set(Calendar.SECOND,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        cancelAlarm();
    }

    public void cancelAlarm()
    {
        Intent intent = new Intent(requireContext(), AlarmBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(requireContext().ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}