package com.example.caparking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caparking.Helper.DBHelper;

public class AdminSignup extends AppCompatActivity {

    private boolean isValid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);

        Button btnsignup = findViewById(R.id.btn1);
        Button btnsignin = findViewById(R.id.btn2);
        EditText et1 = findViewById(R.id.et1);
        EditText et2 = findViewById(R.id.et2);
        EditText et3 = findViewById(R.id.et3);
        EditText et4 = findViewById(R.id.et4);
        EditText et5 = findViewById(R.id.et5);
        DBHelper DB = new DBHelper(this);

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = et1.getText().toString();
                String email = et2.getText().toString();
                String username = et3.getText().toString();
                String password = et4.getText().toString();
                String code = et5.getText().toString();

                if (fullname.equals("") ||email.equals("")||username.equals("")||password.equals("") || code.equals(""))
                    Toast.makeText(AdminSignup.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                else {
                    Boolean checkadminuser = DB.checkadminusername(username);
                    if (checkadminuser==false){
                        if (code.equals("56964")){
                            Boolean insert = DB.insertadmin(fullname,email,username,password);
                            if (insert==true){
                                Toast.makeText(AdminSignup.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), AdminLogin.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(AdminSignup.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(AdminSignup.this, "The Admin Code is Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(AdminSignup.this, "User Already exists please signin", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AdminLogin.class);
                        startActivity(intent);
                    }
                }

            }
        });
        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminLogin.class);
                startActivity(intent);
            }
        });
    }

}