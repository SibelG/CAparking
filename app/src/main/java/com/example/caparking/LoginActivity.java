package com.example.caparking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caparking.Helper.DBHelper;
import com.example.caparking.databinding.ActivityLoginBinding;
import com.example.caparking.util.SessionManager;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {


    SessionManager sessionManager;
    DBHelper DB;
    ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(getApplicationContext());
        DB = new DBHelper(getApplicationContext());

        EditText email = binding.email;
        EditText password = binding.pass;
        Button signin = binding.login;
        Button back = binding.button04;
        Button signup = binding.signup;


        //checks the login status and redirects to the main activity


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String pass = password.getText().toString();

                if (userEmail.equals("") || pass.equals(""))
                    Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuserpass = DB.checkuseremailpassword(userEmail, pass);
                    if (checkuserpass){

                        int id = DB.GetId(userEmail);
                        sessionManager.createLoginSession(id);
                        Toast.makeText(getApplicationContext(), String.valueOf(id), Toast.LENGTH_SHORT).show();

                        Toast.makeText(getApplicationContext(), "LogIn Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intent);
                        //replaceFragment();


                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(i);
            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        if (sessionManager.isLoggedIn()) {

            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
            finish();
            return;

        }
    }




}