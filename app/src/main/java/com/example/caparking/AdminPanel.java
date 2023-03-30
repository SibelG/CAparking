package com.example.caparking;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caparking.Helper.DBHelper;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AdminPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        Button add = findViewById(R.id.add);
        Button update=findViewById(R.id.update);
        Button delete=findViewById(R.id.delete);
        Button view = findViewById(R.id.view);
        Button end = findViewById(R.id.logout);
        DBHelper DB = new DBHelper(this);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getApplicationContext(), DeleteParking.class);
                startActivity(intent);*/
            }
        });

        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor result = DB.viewParkingAreas();
                if (result.getCount()==0){
                    Toast.makeText(AdminPanel.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (result.moveToNext()){
                    buffer.append("ID :"+result.getString(0)+"\n");
                    buffer.append("Departure :"+result.getString(1)+"\n");
                    buffer.append("Arrival :"+result.getString(2)+"\n");
                    buffer.append("Date :"+result.getString(3)+"\n");
                    buffer.append("Total Seats :"+result.getString(4)+"\n\n");
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminPanel.this);
                builder.setCancelable(true);
                builder.setTitle("ALL PARKING DETAILS");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });*/

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
}

