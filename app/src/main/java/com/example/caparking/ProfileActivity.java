package com.example.caparking;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;

import static android.provider.MediaStore.Images.Media.getBitmap;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caparking.Helper.DBHelper;
import com.example.caparking.Helper.HelperUtilities;
import com.example.caparking.Model.User;
import com.example.caparking.databinding.ActivityProfileBinding;
import com.example.caparking.databinding.ActivitySeatSelectionBinding;
import com.example.caparking.util.SessionManager;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private Intent intent;
    private int userId;
    private String TAG;
    private DBHelper DB;
    private SQLiteDatabase db;
    private Cursor cursor;
    SessionManager manager;
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        manager = new SessionManager(this);
        userId = manager.getToken();

        DB = new DBHelper(this);
        getProfileInformation(userId);
        loadImage(userId);

        binding.btnEditProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(uploadImageIntent, REQUEST_CODE);
            }
        });

        binding.btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    //gets employee profile information
    public void getProfileInformation(int employeeID) {
        try {



            db = DB.getReadableDatabase();

            cursor = DB.selectAccount(userId);


            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                String fName = cursor.getString(1);
                String email = cursor.getString(3);
                String flName = cursor.getString(4);
                /*String phone = cursor.getString(5);
                String creditCard = cursor.getString(6);*/

                User client = new User(fName, email,flName, "", "");

                binding.txtClientFirstName.setText("First Name: " + client.getName());
                binding.txtFullName.setText("FullName: " + client.getFullname());
                /*binding.txtClientPhone.setText("Phone: " + client.getPhone());
                binding.txtClientCreditCard.setText("Credit Card: " + HelperUtilities.maskCardNumber(client.getCreditCard()));*/
                binding.txtClientEmail.setText("Email: " + client.getEmail());

            }

        } catch (SQLiteException ex) {
            Toast.makeText(getApplicationContext(), "Database unavailable", Toast.LENGTH_SHORT).show();
        }
    }



    //uploads image from sd card
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {

                case REQUEST_CODE:
                    if (resultCode == Activity.RESULT_OK && data != null) {

                        //data gives you the image uri. Try to convert that to bitmap
                        Uri selectedImage = data.getData();

                        //uploadImage.setImageURI(selectedImage);
                        Bitmap bitmap = getBitmap(this.getContentResolver(), selectedImage);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);

                        // Create a byte array from ByteArrayOutputStream
                        byte[] byteArray = stream.toByteArray();

                        try {

                            db = DB.getWritableDatabase();

                            DB.updateClientImage(db,
                                    byteArray,
                                    String.valueOf(userId));

                            db = DB.getReadableDatabase();

                            cursor = DB.selectImage(db, userId);

                            if (cursor.moveToFirst()) {
                                // Create a bitmap from the byte array
                                byte[] image = cursor.getBlob(0);

                                //binding.profileImage.setImageBitmap(HelperUtilities.decodeSampledBitmapFromByteArray(image, 300, 300));
                                binding.profileImage.setImageURI(selectedImage);
                            }


                        } catch (SQLiteException ex) {
                            Toast.makeText(getApplicationContext(), "Database unavailable", Toast.LENGTH_SHORT).show();
                        }


                        break;
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Log.e(TAG, "Selecting picture cancelled");
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in onActivityResult : " + e.getMessage());
        }
    }

    //loads image on create
    public void loadImage(int userId) {
        try {

            db = DB.getReadableDatabase();

            cursor = DB.selectImage(db, userId);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();

                // Create a bitmap from the byte array
                if (cursor.getBlob(0) != null) {
                    byte[] image = cursor.getBlob(0);

                    binding.profileImage.setImageBitmap(HelperUtilities.decodeSampledBitmapFromByteArray(image, 300, 300));

                }

            }


        } catch (SQLiteException ex) {
            Toast.makeText(getApplicationContext(), "Database unavailable", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            cursor.close();
            db.close();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error closing database or cursor", Toast.LENGTH_SHORT).show();
        }
    }
}
