package com.example.caparking;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.caparking.Adapter.CardAdapter;
import com.example.caparking.Adapter.CardArrayAdapter;
import com.example.caparking.Constants.Constants;
import com.example.caparking.Helper.DBHelper;
import com.example.caparking.Model.Card;
import com.example.caparking.databinding.FragmentPaymentBinding;
import com.example.caparking.databinding.FragmentSeatSelectionBinding;
import com.example.caparking.util.SessionManager;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class PaymentFragment extends Fragment {

    AlertDialog.Builder alertBuilder;
    private FragmentPaymentBinding binding;
    SessionManager manager;
    DBHelper DB;
    private Spinner spinner_show;
    private List<Card> card_list;
    int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentPaymentBinding.inflate(
                inflater, container, false);
        View view = binding.getRoot();

        //binding.cardForm.setVisibility(View.GONE);

        spinner_show = binding.spinner;
        fetchSpinnerValues();
        binding.payOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.payLayout.setVisibility(View.GONE);
                binding.cardForm.setVisibility(View.VISIBLE);
                cardForm();
            }
        });

        binding.totalPrice.setText("$"+manager.getPrice());

        //here data must be an instance of the class MarsDataProvider
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MapsActivity)getActivity()).setNavigationVisibility(true);
        manager = new SessionManager(requireContext());
        DB = new DBHelper(requireContext());
        userId = manager.getToken();

    }

    private void cardForm() {
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
                            DB.updateIsPaid(getArguments().getInt("isPaid"),1);
                            saveCard();
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
        String cardNumber = binding.cardForm.getCardNumber().toString();
        String cardExpiry = binding.cardForm.getExpirationDateEditText().toString();
        String cardCVV = binding.cardForm.getCvv().toString();
        String postalCode = binding.cardForm.getPostalCode().toString();
        String phoneNumber = binding.cardForm.getMobileNumber().toString();


                Boolean checkcard = DB.checkcard(binding.cardForm.getCardNumber().toString());
                if (checkcard == false) {
                    Boolean insert = DB.insertcard(cardNumber,cardExpiry,cardCVV,postalCode,phoneNumber,userId);
                    if (insert == true) {
                        Toast.makeText(requireContext(), "Registration Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Exist Card", Toast.LENGTH_SHORT).show();
                }
            }

        }

    private void fetchSpinnerValues()
    {
        card_list = DB.getCardFromDB(userId);

        CardArrayAdapter spinner_adapter = new CardArrayAdapter(requireContext(), card_list);

       // spinner_adapter.setDropDownViewResource(R.layout.item_card);

        spinner_show.setAdapter(spinner_adapter);

        spinner_show.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Card clickedItem = (Card) parent.getItemAtPosition(position);
                String clickedCountryName = clickedItem.getCard_number();
                Toast.makeText(requireContext(), clickedCountryName + " selected", Toast.LENGTH_SHORT).show();
                binding.btnBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DB.updateIsPaid(getArguments().getInt("isPaid"),1);
                        replaceFragment();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}
