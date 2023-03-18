package com.example.caparking;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.caparking.Helper.DBHelper;

import java.util.Calendar;

public class SeatSelection extends AppCompatActivity implements View.OnClickListener {

    Button btncardpay;
    DBHelper DB = new DBHelper(this);
    Integer flagChecked = 0;

    EditText chooseArrivalTime, chooseDepartureTime;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;
    ImageButton c1 = findViewById(R.id.A1);
    ImageButton c2 = findViewById(R.id.A2);
    ImageButton c3 = findViewById(R.id.A3);
    ImageButton c4 = findViewById(R.id.A4);
    ImageButton c5 = findViewById(R.id.A5);
    ImageButton c6 = findViewById(R.id.A6);
    ImageButton c7 = findViewById(R.id.A7);
    ImageButton c8 = findViewById(R.id.A8);
    ImageButton c9 = findViewById(R.id.B1);
    ImageButton c10 = findViewById(R.id.B2);
    ImageButton c11 = findViewById(R.id.B3);
    ImageButton c12 = findViewById(R.id.B4);
    ImageButton c13 = findViewById(R.id.B5);
    ImageButton c14 = findViewById(R.id.B6);
    ImageButton c15 = findViewById(R.id.B7);
    ImageButton c16 = findViewById(R.id.B8);
    ImageButton c17 = findViewById(R.id.C1);
    ImageButton c18 = findViewById(R.id.C2);
    ImageButton c19 = findViewById(R.id.C3);
    ImageButton c20 = findViewById(R.id.C4);
    ImageButton c21 = findViewById(R.id.C5);
    ImageButton c22 = findViewById(R.id.C6);
    ImageButton c23 = findViewById(R.id.C7);
    ImageButton c24 = findViewById(R.id.C8);
    ImageButton c25 = findViewById(R.id.D1);
    ImageButton c26 = findViewById(R.id.D2);
    ImageButton c27 = findViewById(R.id.D3);
    ImageButton c28 = findViewById(R.id.D4);
    ImageButton c29 = findViewById(R.id.D5);
    ImageButton c30 = findViewById(R.id.D6);
    ImageButton c31 = findViewById(R.id.D7);
    ImageButton c32 = findViewById(R.id.D8);
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_seat_selection);
        btncardpay = (Button) findViewById(R.id.btncardpay);

        btncardpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SeatSelection.this, payment_activity.class);
                startActivity(i);
            }
        });



        chooseDepartureTime = findViewById(R.id.departureTime);
        chooseDepartureTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(SeatSelection.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        chooseDepartureTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        chooseArrivalTime = findViewById(R.id.arrivalTime);
        chooseArrivalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(SeatSelection.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        chooseArrivalTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });
    }



    private void checkIsBooked(Integer i) {
        if (flagChecked != 0) {
            setDefaultColor(flagChecked);
            flagChecked = i;
            setColorGreen(i);
        } else {
            flagChecked = i;
            setColorGreen(i);
        }
    }

    private void setDefaultColor(Integer i) {
        switch (i) {
            case 1: {
                c1.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c1.setEnabled(true);
                break;
            }
            case 2:  {
                c2.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c2.setEnabled(true);
                break;
            }
            case 3: {
                c3.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c3.setEnabled(true);
                break;
            }
            case 4:  {
                c4.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c4.setEnabled(true);
                break;
            }
            case 5:  {
                c5.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c5.setEnabled(true);
                break;
            }
            case 6:  {
                c6.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c6.setEnabled(true);
                break;
            }
            case 7:  {
                c7.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c7.setEnabled(true);
                break;
            }
            case 8: {
                c8.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c8.setEnabled(true);
                break;
            }
            case 9: {
                c9.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c9.setEnabled(true);
                break;
            }
            case 10:  {
                c10.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c10.setEnabled(true);
                break;
            }
            case 11: {
                c11.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c11.setEnabled(true);
                break;
            }
            case 12: {
                c12.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c12.setEnabled(true);
                break;
            }
            case 13:  {
                c13.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c13.setEnabled(true);
                break;
            }
            case 14:  {
                c14.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c14.setEnabled(true);
                break;
            }
            case 15: {
                c15.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c15.setEnabled(true);
                break;
            }
            case 16:  {
                c16.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c16.setEnabled(true);
                break;
            }
            case 17: {
                c17.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c17.setEnabled(true);
                break;
            }
            case 18:  {
                c18.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c18.setEnabled(true);
                break;
            }
            case 19: {
                c19.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c19.setEnabled(true);
                break;
            }
            case 20: {
                c20.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c20.setEnabled(true);
            }
            case 21: {
                c21.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c21.setEnabled(true);
            }
            case 22: {
                c22.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c22.setEnabled(true);
                break;
            }
            case 23: {
                c23.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c23.setEnabled(true);
                break;
            }
            case 24: {
                c24.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c24.setEnabled(true);
                break;
            }
            case 25: {
                c25.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c25.setEnabled(true);
                break;
            }
            case 26: {
                c26.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c26.setEnabled(true);
                break;
            }
            case 27: {
                c27.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c27.setEnabled(true);
                break;
            }
            case 28: {
                c28.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c28.setEnabled(true);
                break;
            }
            case 29: {
                c29.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c29.setEnabled(true);
                break;
            }
            case 30 : {
                c30.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c30.setEnabled(true);
                break;
            }
            case 31:  {
                c31.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c31.setEnabled(true);
                break;
            }
            case 32:  {
                c32.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                c32.setEnabled(true);
                break;
            }
            default:
        }
    }

    private void setColorGRAY(Integer i) {
        switch (i) {
            case 1: {
                c1.setBackgroundColor(Color.GRAY);
                c1.setEnabled(false);
                break;
            }
            case 2: {
                c2.setBackgroundColor(Color.GRAY);
                c2.setEnabled(false);
                break;
            }
            case 3: {
                c3.setBackgroundColor(Color.GRAY);
                c3.setEnabled(false);
                break;
            }
            case 4: {
                c4.setBackgroundColor(Color.GRAY);
                c4.setEnabled(false);
                break;
            }
            case 5: {
                c5.setBackgroundColor(Color.GRAY);
                c5.setEnabled(false);
                break;
            }
            case 6: {
                c6.setBackgroundColor(Color.GRAY);
                c6.setEnabled(false);
                break;
            }
            case 7: {
                c7.setBackgroundColor(Color.GRAY);
                c7.setEnabled(false);
                break;
            }
            case 8: {
                c8.setBackgroundColor(Color.GRAY);
                c8.setEnabled(false);
                break;
            }
            case 9: {
                c9.setBackgroundColor(Color.GRAY);
                c9.setEnabled(false);
                break;
            }
            case 10: {
                c10.setBackgroundColor(Color.GRAY);
                c10.setEnabled(false);
                break;
            }
            case 11: {
                c11.setBackgroundColor(Color.GRAY);
                c11.setEnabled(false);
                break;
            }
            case 12: {
                c12.setBackgroundColor(Color.GRAY);
                c12.setEnabled(false);
                break;
            }
            case 13: {
                c13.setBackgroundColor(Color.GRAY);
                c13.setEnabled(false);
                break;
            }
            case 14: {
                c14.setBackgroundColor(Color.GRAY);
                c14.setEnabled(false);
                break;
            }
            case 15: {
                c15.setBackgroundColor(Color.GRAY);
                c15.setEnabled(false);
                break;
            }
            case 16: {
                c16.setBackgroundColor(Color.GRAY);
                c16.setEnabled(false);
                break;
            }
            case 17:  {
                c17.setBackgroundColor(Color.GRAY);
                c17.setEnabled(false);
                break;
            }
            case 18:  {
                c18.setBackgroundColor(Color.GRAY);
                c18.setEnabled(false);
                break;
            }
            case 19:  {
                c19.setBackgroundColor(Color.GRAY);
                c19.setEnabled(false);
                break;
            }
            case 20: {
                c20.setBackgroundColor(Color.GRAY);
                c20.setEnabled(false);
                break;
            }
            case 21:  {
                c21.setBackgroundColor(Color.GRAY);
                c21.setEnabled(false);
                break;
            }
            case 22:  {
                c22.setBackgroundColor(Color.GRAY);
                c22.setEnabled(false);
                break;
            }
            case 23:  {
                c23.setBackgroundColor(Color.GRAY);
                c23.setEnabled(false);
                break;
            }
            case 24:  {
                c24.setBackgroundColor(Color.GRAY);
                c24.setEnabled(false);
                break;
            }
            case 25:  {
                c25.setBackgroundColor(Color.GRAY);
                c25.setEnabled(false);
                break;
            }
            case 26: {
                c26.setBackgroundColor(Color.GRAY);
                c26.setEnabled(false);
                break;
            }
            case 27: {
                c27.setBackgroundColor(Color.GRAY);
                c27.setEnabled(false);
                break;
            }
            case 28: {
                c28.setBackgroundColor(Color.GRAY);
                c28.setEnabled(false);
                break;
            }
            case 29: {
                c29.setBackgroundColor(Color.GRAY);
                c29.setEnabled(false);
                break;
            }
            case 30: {
                c30.setBackgroundColor(Color.GRAY);
                c30.setEnabled(false);
                break;
            }
            case 31: {
                c31.setBackgroundColor(Color.GRAY);
                c31.setEnabled(false);
                break;
            }
            case 32: {
                c32.setBackgroundColor(Color.GRAY);
                c32.setEnabled(false);
                break;
            }
            default:
        }
    }
    private void setColorGreen(Integer i) {
        switch (i) {
            case 1 : c1.setBackgroundColor(Color.GREEN);break;
            case 2 : c2.setBackgroundColor(Color.GREEN);break;
            case 3 : c3.setBackgroundColor(Color.GREEN);break;
            case 4 : c4.setBackgroundColor(Color.GREEN);break;
            case 5 : c5.setBackgroundColor(Color.GREEN);break;
            case 6 : c6.setBackgroundColor(Color.GREEN);break;
            case 7 : c7.setBackgroundColor(Color.GREEN);break;
            case 8 : c8.setBackgroundColor(Color.GREEN);break;
            case 9 : c9.setBackgroundColor(Color.GREEN);break;
            case 10 : c10.setBackgroundColor(Color.GREEN);break;
            case 11 : c11.setBackgroundColor(Color.GREEN);break;
            case 12 : c12.setBackgroundColor(Color.GREEN);break;
            case 13 : c13.setBackgroundColor(Color.GREEN);break;
            case 14 : c14.setBackgroundColor(Color.GREEN);break;
            case 15 : c15.setBackgroundColor(Color.GREEN);break;
            case 16 : c16.setBackgroundColor(Color.GREEN);break;
            case 17 : c17.setBackgroundColor(Color.GREEN);break;
            case 18 : c18.setBackgroundColor(Color.GREEN);break;
            case 19 : c19.setBackgroundColor(Color.GREEN);break;
            case 20 : c20.setBackgroundColor(Color.GREEN);break;
            case 21 : c21.setBackgroundColor(Color.GREEN);break;
            case 22 : c22.setBackgroundColor(Color.GREEN);break;
            case 23 : c23.setBackgroundColor(Color.GREEN);break;
            case 24 : c24.setBackgroundColor(Color.GREEN);break;
            case 25 : c25.setBackgroundColor(Color.GREEN);break;
            case 26 : c26.setBackgroundColor(Color.GREEN);break;
            case 27 : c27.setBackgroundColor(Color.GREEN);break;
            case 28 : c28.setBackgroundColor(Color.GREEN);break;
            case 29 : c29.setBackgroundColor(Color.GREEN);break;
            case 30 : c30.setBackgroundColor(Color.GREEN);break;
            case 31 : c31.setBackgroundColor(Color.GREEN);break;
            case 32 : c32.setBackgroundColor(Color.GREEN);break;
            default:
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.A1:
                checkIsBooked(1);
                break;
            case R.id.A2:
                checkIsBooked(2);
                break;
            case R.id.A3:
                checkIsBooked(3);
                break;
            case R.id.A4:
                checkIsBooked(4);
                break;
            case R.id.A5:
                checkIsBooked(5);
                break;
            case R.id.A6:
                checkIsBooked(6);
                break;
            case R.id.A7:
                checkIsBooked(7);
                break;
            case R.id.A8:
                checkIsBooked(8);
                break;
            case R.id.B1:
                checkIsBooked(9);
                break;
            case R.id.B2:
                checkIsBooked(10);
                break;
            case R.id.B3:
                checkIsBooked(11);
                break;
            case R.id.B4:
                checkIsBooked(12);
                break;
            case R.id.B5:
                checkIsBooked(13);
                break;
            case R.id.B6:
                checkIsBooked(14);
                break;
            case R.id.B7:
                checkIsBooked(15);
                break;
            case R.id.B8:
                checkIsBooked(16);
                break;
            case R.id.C1:
                checkIsBooked(17);
                break;
            case R.id.C2:
                checkIsBooked(18);
                break;
            case R.id.C3:
                checkIsBooked(19);
                break;
            case R.id.C4:
                checkIsBooked(20);
                break;
            case R.id.C5:
                checkIsBooked(21);
                break;
            case R.id.C6:
                checkIsBooked(22);
                break;
            case R.id.C7:
                checkIsBooked(23);
                break;
            case R.id.C8:
                checkIsBooked(24);
                break;
            case R.id.D1:
                checkIsBooked(25);
                break;
            case R.id.D2:
                checkIsBooked(26);
                break;
            case R.id.D3:
                checkIsBooked(27);
                break;
            case R.id.D4:
                checkIsBooked(28);
                break;
            case R.id.D5:
                checkIsBooked(39);
                break;
            case R.id.D6:
                checkIsBooked(30);
                break;
            case R.id.D7:
                checkIsBooked(31);
                break;
            case R.id.D8:
                checkIsBooked(32);
                break;
            default:
        }
    }
}