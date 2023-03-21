package com.example.caparking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caparking.Helper.DBHelper;

public class AdminLogin extends AppCompatActivity {

    public static final String MY_PREFERENCES = "MY_PREFS";
    public static final String EMAIL = "EMAIL";
    public static final String CLIENT_ID = "CLIENT_ID";
    public static final String LOGIN_STATUS = "LOGGED_IN";
    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.pass);
        Button signin = findViewById(R.id.login);
        Button signup = findViewById(R.id.signup);
        Button back = findViewById(R.id.button01);
        DBHelper DB = new DBHelper(this);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, 0);
        Boolean loggedIn = sharedPreferences.getBoolean(LOGIN_STATUS, false);//login status

        //checks the login status and redirects to the main activity
        if (loggedIn) {
            Intent intent = new Intent(getApplicationContext(), AdminPanel.class);
            startActivity(intent);
            finish();
            return;
        }
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String pass = password.getText().toString();

                if (userEmail.equals("") || pass.equals(""))
                    Toast.makeText(AdminLogin.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkadminuserpass = DB.checkadminusernamepassword(userEmail, pass);
                    if (checkadminuserpass==true){

                        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString(EMAIL, userEmail);
                        editor.putBoolean(LOGIN_STATUS, true);

                        editor.commit();

                        Toast.makeText(AdminLogin.this, "LogIn Successful", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(getApplicationContext(), AdminPanel.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(AdminLogin.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(getApplicationContext(), AdminSignup.class);
                startActivity(intent);
            }
        });
    }
}