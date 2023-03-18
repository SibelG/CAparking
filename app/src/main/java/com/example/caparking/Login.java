package com.example.caparking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caparking.Helper.DBHelper;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.pass);
        Button signin = findViewById(R.id.login);
        Button back = findViewById(R.id.button04);
        Button signup = findViewById(R.id.signup);
        DBHelper DB = new DBHelper(this);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String pass = password.getText().toString();

                if (userEmail.equals("") || pass.equals(""))
                    Toast.makeText(Login.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuserpass = DB.checkuseremailpassword(userEmail, pass);
                    if (checkuserpass==true){
                        Toast.makeText(Login.this, "LogIn Successful", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
            }
        });


    }
}