package com.example.caparking;

import static com.example.caparking.Helper.HelperUtilities.currentDate;
import static com.example.caparking.Helper.HelperUtilities.formatDate;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caparking.Helper.DBHelper;
import com.example.caparking.Helper.HelperUtilities;
import com.example.caparking.databinding.ActivityMainBinding;
import com.example.caparking.databinding.ActivitySeatSelectionBinding;
import com.example.caparking.util.SessionManager;

import org.w3c.dom.Text;

import java.util.Calendar;

public class SeatSelection extends AppCompatActivity implements View.OnClickListener {


    SessionManager manager;
    DBHelper DB = new DBHelper(this);
    private SQLiteDatabase db;
    int flagChecked = 0;
    private boolean flightExists = false;
    private SharedPreferences sharedPreferences;
    private String fare;
    private int parkingID;
    private int userID;
    private int tempYear;
    private int tempMonth;
    private int tempDay;
    private int tempHour,tempMinute;
    private String oneWayDepartureDate, roundDepartureDate, roundReturnDate;
    private boolean isValidRoundDate = true;
    ImageButton c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,
    c11,c12,c13,c14,c15,c16,c17,c18,c19,c20,c21,c22,c23,c24,c25,
    c26,c27,c28,c29,c30,c31,c32;
    private ActivitySeatSelectionBinding binding;
    private DatePickerDialog datePickerDialog2;
    double totalFare;

    TimePickerDialog timePickerDialog,timePickerDialog2;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm,departureTime,returnTime,parkingDate;
    //current date
    private int year;
    private int month;
    private int day;

    //id of date picker controls
    private final int ROUND_DATE_PICKER = R.id.btnRoundDepartureDatePicker;
    private final int ROUND_DEPARTURE_TIME_PICKER = R.id.etChooseDepartureTime;
    private final int ROUND_RETURN_TIME_PICKER = R.id.etChooseReturnTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_seat_selection);
        binding = ActivitySeatSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textDate.setText(currentDate());
        manager=new SessionManager(getApplicationContext());
        sharedPreferences = getApplicationContext().getSharedPreferences("PARKING_USER", Context.MODE_PRIVATE);
        userID = manager.getToken();
        //Toast.makeText(this, String.valueOf(userID), Toast.LENGTH_SHORT).show();
        parkingID = manager.getParkingId();
        fare = getIntent().getStringExtra("price");


        c1 = findViewById(R.id.A1);
        c2 = findViewById(R.id.A2);
        c3 = findViewById(R.id.A3);
        c4 = findViewById(R.id.A4);
        c5 = findViewById(R.id.A5);
        c6 = findViewById(R.id.A6);
        c7 = findViewById(R.id.A7);
        c8 = findViewById(R.id.A8);
        c9 = findViewById(R.id.B1);
        c10 = findViewById(R.id.B2);
        c11 = findViewById(R.id.B3);
        c12 = findViewById(R.id.B4);
        c13 = findViewById(R.id.B5);
        c14 = findViewById(R.id.B6);
        c15 = findViewById(R.id.B7);
        c16 = findViewById(R.id.B8);
        c17 = findViewById(R.id.C1);
        c18 = findViewById(R.id.C2);
        c19 = findViewById(R.id.C3);
        c20 = findViewById(R.id.C4);
        c21 = findViewById(R.id.C5);
        c22 = findViewById(R.id.C6);
        c23 = findViewById(R.id.C7);
        c24 = findViewById(R.id.C8);
        c25 = findViewById(R.id.D1);
        c26 = findViewById(R.id.D2);
        c27 = findViewById(R.id.D3);
        c28 = findViewById(R.id.D4);
        c29 = findViewById(R.id.D5);
        c30 = findViewById(R.id.D6);
        c31 = findViewById(R.id.D7);
        c32 = findViewById(R.id.D8);
        c1.setOnClickListener(this);
        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);
        c5.setOnClickListener(this);
        c6.setOnClickListener(this);
        c7.setOnClickListener(this);
        c8.setOnClickListener(this);
        c9.setOnClickListener(this);
        c10.setOnClickListener(this);

        binding.btncardpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidRoundInput()){
                    bookFlight();
                    Intent i = new Intent(SeatSelection.this, payment_activity.class);
                    startActivity(i);


                }

            }
        });

        year = HelperUtilities.currentYear();
        month = HelperUtilities.currentMonth();
        day = HelperUtilities.currentDay();
        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);
        //round trip departure date picker on click listener
        binding.btnRoundDepartureDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                datePickerDialog(ROUND_DATE_PICKER).show();
            }
        });

        binding.etChooseDepartureTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog(ROUND_DEPARTURE_TIME_PICKER);
            }
        });

        binding.etChooseReturnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog(ROUND_RETURN_TIME_PICKER);
            }
        });


        binding.perFare.setText(fare+" Hour");

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

    private void setColorGRAY(Integer i) {
        switch (i) {
            case 1: {
                c1.setBackgroundColor(Color.GRAY);
                c1.setEnabled(true);
                break;
            }
            case 2:  {
                c2.setBackgroundColor(Color.GRAY);
                c2.setEnabled(true);
                break;
            }
            case 3: {
                c3.setBackgroundColor(Color.GRAY);
                c3.setEnabled(true);
                break;
            }
            case 4:  {
                c4.setBackgroundColor(Color.GRAY);
                c4.setEnabled(true);
                break;
            }
            case 5:  {
                c5.setBackgroundColor(Color.GRAY);
                c5.setEnabled(true);
                break;
            }
            case 6:  {
                c6.setBackgroundColor(Color.GRAY);
                c6.setEnabled(true);
                break;
            }
            case 7:  {
                c7.setBackgroundColor(Color.GRAY);
                c7.setEnabled(true);
                break;
            }
            case 8: {
                c8.setBackgroundColor(Color.GRAY);
                c8.setEnabled(true);
                break;
            }
            case 9: {
                c9.setBackgroundColor(Color.GRAY);
                c9.setEnabled(true);
                break;
            }
            case 10:  {
                c10.setBackgroundColor(Color.GRAY);
                c10.setEnabled(true);
                break;
            }
            case 11: {
                c11.setBackgroundColor(Color.GRAY);
                c11.setEnabled(true);
                break;
            }
            case 12: {
                c12.setBackgroundColor(Color.GRAY);
                c12.setEnabled(true);
                break;
            }
            case 13:  {
                c13.setBackgroundColor(Color.GRAY);
                c13.setEnabled(true);
                break;
            }
            case 14:  {
                c14.setBackgroundColor(Color.GRAY);
                c14.setEnabled(true);
                break;
            }
            case 15: {
                c15.setBackgroundColor(Color.GRAY);
                c15.setEnabled(true);
                break;
            }
            case 16:  {
                c16.setBackgroundColor(Color.GRAY);
                c16.setEnabled(true);
                break;
            }
            case 17: {
                c17.setBackgroundColor(Color.GRAY);
                c17.setEnabled(true);
                break;
            }
            case 18:  {
                c18.setBackgroundColor(Color.GRAY);
                c18.setEnabled(true);
                break;
            }
            case 19: {
                c19.setBackgroundColor(Color.GRAY);
                c19.setEnabled(true);
                break;
            }
            case 20: {
                c20.setBackgroundColor(Color.GRAY);
                c20.setEnabled(true);
            }
            case 21: {
                c21.setBackgroundColor(Color.GRAY);
                c21.setEnabled(true);
            }
            case 22: {
                c22.setBackgroundColor(Color.GRAY);
                c22.setEnabled(true);
                break;
            }
            case 23: {
                c23.setBackgroundColor(Color.GRAY);
                c23.setEnabled(true);
                break;
            }
            case 24: {
                c24.setBackgroundColor(Color.GRAY);
                c24.setEnabled(true);
                break;
            }
            case 25: {
                c25.setBackgroundColor(Color.GRAY);
                c25.setEnabled(true);
                break;
            }
            case 26: {
                c26.setBackgroundColor(Color.GRAY);
                c26.setEnabled(true);
                break;
            }
            case 27: {
                c27.setBackgroundColor(Color.GRAY);
                c27.setEnabled(true);
                break;
            }
            case 28: {
                c28.setBackgroundColor(Color.GRAY);
                c28.setEnabled(true);
                break;
            }
            case 29: {
                c29.setBackgroundColor(Color.GRAY);
                c29.setEnabled(true);
                break;
            }
            case 30 : {
                c30.setBackgroundColor(Color.GRAY);
                c30.setEnabled(true);
                break;
            }
            case 31:  {
                c31.setBackgroundColor(Color.GRAY);
                c31.setEnabled(true);
                break;
            }
            case 32:  {
                c32.setBackgroundColor(Color.GRAY);
                c32.setEnabled(true);
                break;
            }
            default:
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        if(departureTime.equalsIgnoreCase("departure") && returnTime.equalsIgnoreCase("return")){
            var date1= binding.etChooseDepartureTime.getText().toString();
            var date2= binding.etChooseReturnTime.getText().toString();
            totalFare = HelperUtilities.calculateTotalFare(Double.parseDouble(fare),date1,date2);
            binding.totalFare.setText("$"+totalFare);
        }


    }

    private void setDefaultColor(Integer i) {
        switch (i) {
            case 1: {
                c1.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c1.setEnabled(false);
                break;
            }
            case 2: {
                c2.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c2.setEnabled(false);
                break;
            }
            case 3: {
                c3.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c3.setEnabled(false);
                break;
            }
            case 4: {
                c4.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c4.setEnabled(false);
                break;
            }
            case 5: {
                c5.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c5.setEnabled(false);
                break;
            }
            case 6: {
                c6.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c6.setEnabled(false);
                break;
            }
            case 7: {
                c7.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c7.setEnabled(false);
                break;
            }
            case 8: {
                c8.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c8.setEnabled(false);
                break;
            }
            case 9: {
                c9.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c9.setEnabled(false);
                break;
            }
            case 10: {
                c10.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c10.setEnabled(false);
                break;
            }
            case 11: {
                c11.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c11.setEnabled(false);
                break;
            }
            case 12: {
                c12.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c12.setEnabled(false);
                break;
            }
            case 13: {
                c13.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c13.setEnabled(false);
                break;
            }
            case 14: {
                c14.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c14.setEnabled(false);
                break;
            }
            case 15: {
                c15.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c15.setEnabled(false);
                break;
            }
            case 16: {
                c16.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c16.setEnabled(false);
                break;
            }
            case 17:  {
                c17.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c17.setEnabled(false);
                break;
            }
            case 18:  {
                c18.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c18.setEnabled(false);
                break;
            }
            case 19:  {
                c19.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c19.setEnabled(false);
                break;
            }
            case 20: {
                c20.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c20.setEnabled(false);
                break;
            }
            case 21:  {
                c21.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c21.setEnabled(false);
                break;
            }
            case 22:  {
                c22.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c22.setEnabled(false);
                break;
            }
            case 23:  {
                c23.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c23.setEnabled(false);
                break;
            }
            case 24:  {
                c24.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c24.setEnabled(false);
                break;
            }
            case 25:  {
                c25.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c25.setEnabled(false);
                break;
            }
            case 26: {
                c26.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c26.setEnabled(false);
                break;
            }
            case 27: {
                c27.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c27.setEnabled(false);
                break;
            }
            case 28: {
                c28.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c28.setEnabled(false);
                break;
            }
            case 29: {
                c29.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c29.setEnabled(false);
                break;
            }
            case 30: {
                c30.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c30.setEnabled(false);
                break;
            }
            case 31: {
                c31.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                c31.setEnabled(false);
                break;
            }
            case 32: {
                c32.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
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
        flagChecked = 0;
        db = DB.getWritableDatabase();
        Cursor cursor = DB.viewParkingAreas(String.valueOf(parkingID));
        var date = binding.btnRoundDepartureDatePicker.getText().toString();

        if (cursor != null && cursor.getCount() == 1) {
            cursor.moveToFirst();

            int  seatCount = cursor.getInt(1);
            Log.d("seat", String.valueOf(seatCount));

            for (int i=0;i< seatCount;i++){
                boolean check = DB.checkSeats(parkingID,i,"26 Mart 2023 Pazar");
                if(check){
                    setColorGRAY(i);
                    Log.d("seatNumber", String.valueOf(i));
                }
            }

        }
        else{
            Log.d("error","errorrrrrrr");
        }

    }

    public void bookFlight(){
        try{
            departureTime = binding.etChooseDepartureTime.getText().toString();
            returnTime= binding.etChooseReturnTime.getText().toString();
            parkingDate = binding.btnRoundDepartureDatePicker.getText().toString();
            DB.insertSeat(db, departureTime, returnTime,parkingDate,flagChecked,1,totalFare,parkingID,userID);
            bookFlightDialog().show();

        }catch(SQLiteException e){

        }
    }
    private TimePickerDialog showTimeDialog(int timePickerId)
    {


        switch (timePickerId) {
            case ROUND_DEPARTURE_TIME_PICKER:

                        timePickerDialog = new TimePickerDialog(SeatSelection.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                if (hourOfDay >= 12) {
                                    amPm = "PM";
                                } else {
                                    amPm = "AM";
                                }
                                tempHour = hourOfDay;
                                tempMinute = minutes;
                                binding.etChooseDepartureTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                            }
                        }, currentHour, currentMinute, false);

                        timePickerDialog.show();
                        break;

            case ROUND_RETURN_TIME_PICKER:

                        timePickerDialog2 = new TimePickerDialog(SeatSelection.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                if (hourOfDay >= 12) {
                                    amPm = "PM";
                                } else {
                                    amPm = "AM";
                                }
                                String departureDate = tempHour + ":" + tempMinute;
                                String returnDate = hourOfDay+ ":" + minutes;
                                Toast.makeText(getApplicationContext(),departureDate,Toast.LENGTH_SHORT).show();

                                if (HelperUtilities.compareTime(departureDate, returnDate)) {
                                    timePickerAlert().show();
                                    isValidRoundDate = false;
                                } else {
                                    isValidRoundDate = true;
                                    binding.etChooseReturnTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                                }

                            }
                        }, currentHour, currentMinute, false);

                        timePickerDialog2.show();
                        break;

        }

    return null;

    }

    public DatePickerDialog datePickerDialog(int datePickerId) {

        switch (datePickerId) {

            case ROUND_DATE_PICKER:

                if (datePickerDialog2 == null) {
                    datePickerDialog2 = new DatePickerDialog(this, getRoundDepartureDatePickerListener(), year, month, day);
                }
                datePickerDialog2.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                return datePickerDialog2;


        }
        return null;
    }

    public boolean isValidRoundInput() {

        if (binding.btnRoundDepartureDatePicker.getText().toString().equalsIgnoreCase("select date")) {
            datePickerTwoAlert().show();
            return false;
        }
        if (binding.etChooseDepartureTime.getText().toString().equalsIgnoreCase("departure")) {
            timePickerOneAlert().show();
            return false;
        }

        if (binding.etChooseReturnTime.getText().toString().equalsIgnoreCase("return")) {
            datePickerTwoAlert().show();
            return false;
        }
        return true;

    }

    public Dialog bookFlightDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SeatSelection.this);
        builder.setMessage("Your flight booked successfully. ")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    public Dialog flightAlreadyBookedAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SeatSelection.this);
        builder.setMessage("You already booked this flight. ")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }



    public DatePickerDialog.OnDateSetListener getRoundDepartureDatePickerListener() {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int startYear, int startMonth, int startDay) {

                tempYear = startYear;
                tempMonth = startMonth;
                tempDay = startDay;

                //get round trip departure date here
                roundDepartureDate = startYear + "-" + (startMonth + 1) + "-" + startDay;
                binding.btnRoundDepartureDatePicker.setText(HelperUtilities.formatDate(startYear, startMonth, startDay));
            }
        };
    }



    public Dialog timePickerAlert() {
        return new AlertDialog.Builder(this)
                .setMessage("Please select a valid return time. The return time cannot be before the departure time.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).create();
    }

    public Dialog timePickerOneAlert() {
        return new AlertDialog.Builder(this)
                .setMessage("Please select a departure date.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).create();
    }

    public Dialog datePickerTwoAlert() {
        return new AlertDialog.Builder(this)
                .setMessage("Please select a return date.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).create();
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