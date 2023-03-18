package com.example.caparking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caparking.Helper.DBHelper;

public class adminlogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.pass);
        Button signin = findViewById(R.id.login);
        Button signup = findViewById(R.id.signup);
        Button back = findViewById(R.id.button01);
        DBHelper DB = new DBHelper(this);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String pass = password.getText().toString();

                if (userEmail.equals("") || pass.equals(""))
                    Toast.makeText(adminlogin.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkadminuserpass = DB.checkadminusernamepassword(userEmail, pass);
                    if (checkadminuserpass==true){
                        Toast.makeText(adminlogin.this, "LogIn Successful", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(getApplicationContext(), adminpanel.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(adminlogin.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), adminsignup.class);
                startActivity(intent);
            }
        });
    }
}