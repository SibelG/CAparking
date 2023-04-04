package com.example.caparking.Constants;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.EditText;

import com.example.caparking.Helper.HelperUtilities;


public class Constants {
    //validates user input
    public static boolean isValidUserInput(EditText inputFirstName, EditText inputUserName, EditText inputEmail, EditText inputPassword, EditText inputConfirmPassword) {
        if (HelperUtilities.isEmptyOrNull(inputFirstName.getText().toString())) {
            inputFirstName.setError("Please enter your first name");
            return false;
        } else if (!HelperUtilities.isString(inputFirstName.getText().toString())) {
            inputFirstName.setError("Please enter a valid first name");
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(inputUserName.getText().toString())) {
            inputUserName.setError("Please enter your last name");
            return false;
        } else if (!HelperUtilities.isString(inputUserName.getText().toString())) {
            inputUserName.setError("Please enter a valid last name");
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(inputEmail.getText().toString())) {
            inputEmail.setError("Please enter your email");
            return false;
        } else if (!HelperUtilities.isValidEmail(inputEmail.getText().toString())) {
            inputEmail.setError("Please enter a valid email");
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(inputPassword.getText().toString())) {
            inputPassword.setError("Please enter your password");
            return false;
        } else if (HelperUtilities.isShortPassword(inputPassword.getText().toString())) {
            inputPassword.setError("Your password must have at least 6 characters.");
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(inputConfirmPassword.getText().toString())) {
            inputConfirmPassword.setError("Please confirm password");
            return false;
        }

        if (!(inputConfirmPassword.getText().toString().equals(inputPassword.getText().toString()))) {

            inputConfirmPassword.setError("The password doesn't match.");
            return false;
        }


        return true;

    }


}
