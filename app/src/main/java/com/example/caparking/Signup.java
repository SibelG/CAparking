package com.example.caparking;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caparking.Constants.Constants;
import com.example.caparking.Helper.DBHelper;

public class Signup extends AppCompatActivity {

    DBHelper DB;
    private boolean isValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button btnsignup = findViewById(R.id.btn1);
        Button btnsignin = findViewById(R.id.btn2);
        EditText et1 = findViewById(R.id.et1);
        EditText et2 = findViewById(R.id.et2);
        EditText et3 = findViewById(R.id.et3);
        EditText et4 = findViewById(R.id.et4);
        EditText et5 = findViewById(R.id.et4);
        DB = new DBHelper(this);

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = et1.getText().toString();
                String email = et2.getText().toString();
                String username = et3.getText().toString();
                String password = et4.getText().toString();
                String rePassword = et5.getText().toString();

                isValid = Constants.isValidUserInput(et1,et3,et2,et4,et5);
                if(isValid){
                    Boolean checkuser = DB.checkusername(username);
                    if (checkuser==false){
                        Boolean insert = DB.insertData(fullname,email,username,password);
                        if (insert==true){
                            registrationSuccessDialog().show();
                        }
                        else {
                            Toast.makeText(Signup.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        accountExistsAlert().show();
                    }
                }
            }
        });
        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }


    public Dialog registrationSuccessDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your profile created successfully! ")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    }
                });

        return builder.create();
    }

    public Dialog accountExistsAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("An account with this email already exists. Please try again. ")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    }
                });

        return builder.create();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            DB.close();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error closing database or cursor", Toast.LENGTH_SHORT).show();
        }
    }

}