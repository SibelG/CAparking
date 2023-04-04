package com.example.caparking;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.example.caparking.Constants.Constants;
import com.example.caparking.Helper.DBHelper;
import com.example.caparking.databinding.ActivityLoginBinding;
import com.example.caparking.databinding.ActivitySignupBinding;


import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignUpActivity extends AppCompatActivity {

    DBHelper DB;
    private boolean isValid;
    ActivitySignupBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DB = new DBHelper(getApplicationContext());
        Button btnsignup = binding.btn1;
        EditText et1 = binding.et1;
        EditText et2 = binding.et2;
        EditText et3 = binding.et3;
        EditText et4 = binding.et4;
        EditText et5 = binding.et5;


        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = et1.getText().toString();
                String email = et2.getText().toString();
                String username = et3.getText().toString();
                String password = et4.getText().toString();
                String rePassword = et5.getText().toString();

                isValid = Constants.isValidUserInput(et1, et3, et2, et4, et5);
                if (isValid) {
                    Boolean checkuser = DB.checkemail(email);
                    if (checkuser == false) {
                        Boolean insert = DB.insertData(fullname, email, username, password);
                        if (insert == true) {
                            registrationSuccessDialog().show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        accountExistsAlert().show();
                    }
                }
            }
        });

    }

    public Dialog registrationSuccessDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your profile created successfully! ")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
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