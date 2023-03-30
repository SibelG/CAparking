package com.example.caparking;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.caparking.databinding.FragmentPaymentBinding;
import com.example.caparking.databinding.FragmentSeatSelectionBinding;
import com.example.caparking.util.SessionManager;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class PaymentFragment extends Fragment {

    AlertDialog.Builder alertBuilder;
    private FragmentPaymentBinding binding;
    SessionManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentPaymentBinding.inflate(
                inflater, container, false);
        View view = binding.getRoot();

        binding.cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .setup(requireActivity());
        binding.cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        binding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.cardForm.isValid()) {
                    alertBuilder = new AlertDialog.Builder(requireContext());
                    alertBuilder.setTitle("Confirm before purchase");
                    alertBuilder.setMessage("Card number: " + binding.cardForm.getCardNumber() + "\n" +
                            "Card expiry date: " + binding.cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                            "Card CVV: " + binding.cardForm.getCvv() + "\n" +
                            "Postal code: " + binding.cardForm.getPostalCode() + "\n" +
                            "Phone number: " + binding.cardForm.getMobileNumber());
                    alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Toast.makeText(requireContext(), "Thank you for purchase", Toast.LENGTH_LONG).show();
                        }
                    });
                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();

                    replaceFragment();



                } else {
                    Toast.makeText(requireContext(), "Please complete the form", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.totalPrice.setText("$"+manager.getPrice());

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),SeatSelection.class);
                startActivity(intent);
            }
        });
        //here data must be an instance of the class MarsDataProvider
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new SessionManager(requireContext());

    }

    private void replaceFragment(){
        AlarmFragment newFragment = new AlarmFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void saveCard(){
        if(binding.checkBox.isChecked()){

        }
    }
}