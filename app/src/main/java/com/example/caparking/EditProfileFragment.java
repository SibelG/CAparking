package com.example.caparking;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.caparking.Helper.DBHelper;
import com.example.caparking.Helper.HelperUtilities;
import com.example.caparking.Model.User;
import com.example.caparking.databinding.FragmentEditProfileBinding;
import com.example.caparking.util.SessionManager;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class EditProfileFragment extends Fragment {


    public EditProfileFragment() {
        // Required empty public constructor
    }

    private Button btnUpdateProfile;
    private EditText clientFirstName;
    private EditText clientFullName;
    private EditText clientEmail;
    private EditText clientPhone;
    private EditText clientCreditCard;
    private DBHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private boolean isValid;
    int userId;
    SessionManager manager;
    private FragmentEditProfileBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MapsActivity)getActivity()).setNavigationVisibility(true);
        manager = new SessionManager(getContext());
        userId = manager.getToken();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(
                inflater, container, false);

        View view = binding.getRoot();
        btnUpdateProfile = binding.btnSaveProfile;

        clientFirstName = binding.txtFirstNameEdit;
        clientFullName = binding.txtFullNameEdit;
        clientEmail = binding.txtEmailEdit;
        clientPhone = binding.txtPhoneEdit;
        clientCreditCard = binding.txtCreditCardEdit;

        loadProfileInfo();

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });


        return view;
    }

    public void loadProfileInfo() {

        try {


            databaseHelper = new DBHelper(requireContext());
            db = databaseHelper.getWritableDatabase();


            cursor = databaseHelper.selectAccount(db, userId);


            if (cursor.moveToFirst()) {


                String fName = cursor.getString(1);
                String email = cursor.getString(3);
                String flName = cursor.getString(4);
                String phone = cursor.getString(5);
                String creditCard = cursor.getString(6);


                clientFirstName.setText(fName);
                clientFullName.setText(flName);
                clientEmail.setText(email);
                clientPhone.setText(phone);
                clientCreditCard.setText(creditCard);


            }
        } catch (SQLiteException ex) {
            Toast.makeText(requireContext(), "Database unavailable", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateProfile() {
        try {

            db = databaseHelper.getWritableDatabase();

            isValid = isValidUserInput();

            if (isValid) {
                databaseHelper.updateAccount(db,
                        clientFirstName.getText().toString(),
                        clientFullName.getText().toString(),
                        clientPhone.getText().toString(),
                        clientEmail.getText().toString(),
                        clientCreditCard.getText().toString(),
                        userId
                );

                /*databaseHelper.updateAccount(db,
                        clientEmail.getText().toString(),
                        userId);*/


                updateProfileDialog().show();
            }
        } catch (SQLiteException ex) {
            Toast.makeText(requireContext(), "Database unavailable", Toast.LENGTH_SHORT).show();
        }

    }


    public Dialog updateProfileDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("The profile updated successfully! ")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        replaceFragment();
                        /*Intent intent = new Intent(requireContext(), ProfileActivity.class);
                        startActivity(intent);*/
                    }
                });

        return builder.create();
    }

    public boolean isValidUserInput() {
        if (HelperUtilities.isEmptyOrNull(clientFirstName.getText().toString())) {
            clientFirstName.setError("Please enter your first name");
            return false;
        } else if (!HelperUtilities.isString(clientFirstName.getText().toString())) {
            clientFirstName.setError("Please enter a valid first name");
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(clientFullName.getText().toString())) {
            clientFullName.setError("Please enter your last name");
            return false;
        } else if (!HelperUtilities.isString(clientFullName.getText().toString())) {
            clientFullName.setError("Please enter a valid last name");
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(clientEmail.getText().toString())) {
            clientEmail.setError("Please enter your email");
            return false;
        } else if (!HelperUtilities.isValidEmail(clientEmail.getText().toString())) {
            clientEmail.setError("Please enter a valid email");
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(clientPhone.getText().toString())) {
            clientPhone.setError("Please enter your phone");
            return false;
        } else if (!HelperUtilities.isValidPhone(clientPhone.getText().toString())) {
            clientPhone.setError("Please enter a valid phone");
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(clientCreditCard.getText().toString())) {
            clientCreditCard.setError("Please enter your credit card number");
            return false;
        } else if (!HelperUtilities.isValidCreditCard(clientCreditCard.getText().toString())) {
            clientCreditCard.setError("Please enter a valid credit card number");
            return false;
        }


        return true;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            cursor.close();
            db.close();
        } catch (Exception ex) {
            Toast.makeText(requireContext(), "Error closing database or cursor", Toast.LENGTH_SHORT).show();
        }

    }
    private void replaceFragment(){
        ProfileFragment newFragment = new ProfileFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



}