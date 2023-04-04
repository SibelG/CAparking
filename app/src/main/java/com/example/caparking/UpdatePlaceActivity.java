package com.example.caparking;

import static com.example.caparking.util.LogUtil.logD;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caparking.Common.Common;
import com.example.caparking.Helper.DBHelper;
import com.example.caparking.Model.PlaceDetail;
import com.example.caparking.Remote.IGoogleAPIService;
import com.example.caparking.databinding.ActivityUpdateParkingBinding;
import com.example.caparking.databinding.ActivityUpdatePlaceBinding;
import com.example.caparking.util.SessionManager;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class UpdatePlaceActivity extends AppCompatActivity {


    SessionManager manager;
    IGoogleAPIService mService;
    DBHelper DB;
    PlaceDetail mPlace;


    private ActivityUpdatePlaceBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdatePlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DB = new DBHelper(this);

        mService = Common.getGoogleAPIService();

        binding.placesName.setText("");
        binding.placesAddress.setText("");
        binding.placesOpenHour.setText("");
        manager=new SessionManager(getApplicationContext());

        binding.btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mPlace.getResult().getUrl()));
                startActivity(mapIntent);
            }
        });

        if (Common.currentResults.getPhotos() != null && Common.currentResults.getPhotos().length > 0)  {

            Picasso.with(this)
                    .load(Common.getPhotoOfPlaces(Common.currentResults.getPhotos()[0].getPhoto_reference(),1000))
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .into(binding.photo);
        }

        if (Common.currentResults.getRating() != null && !TextUtils.isEmpty(Common.currentResults.getRating()))  {
            binding.ratingBar.setRating(Float.parseFloat(Common.currentResults.getRating()));
        }
        else    {
            binding.ratingBar.setVisibility(View.GONE);
        }

        if (Common.currentResults.getOpening_hours() != null)  {
            binding.placesOpenHour.setText("Open Now : "+Common.currentResults.getOpening_hours().getOpen_now());
        }
        else    {
            binding.placesOpenHour.setVisibility(View.GONE);
        }

        mService.getDetailPlaces(Common.getPlaceDetailUrl(Common.currentResults.getPlace_id()))
                .enqueue(new Callback<PlaceDetail>() {
                    @Override
                    public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {

                        mPlace = response.body();

                        binding.placesAddress.setText(mPlace.getResult().getFormatted_address());
                        binding.placesName.setText(mPlace.getResult().getName());
                        binding.placesAddress.setText(mPlace.getResult().getFormatted_address());


                        Cursor cursor = DB.viewParkingAreas(Common.currentResults.getPlace_id());

                        if (cursor != null && cursor.getCount() == 1) {
                            cursor.moveToFirst();

                            //Toast.makeText(getActivity().getApplicationContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_SHORT).show();
                            binding.seats.setText(String.valueOf(cursor.getInt(1)));
                            logD("name",mPlace.getResult().getName());
                            binding.perHourPrice.setText(String.valueOf(cursor.getDouble(2)));
                        }
                        else{
                            Log.d("error","error");
                        }
                    }

                    @Override
                    public void onFailure(Call<PlaceDetail> call, Throwable t) {

                    }
                });
    }

    public void updateLocation(View view) {

        String id = Common.currentResults.getPlace_id();
        String total_seats = binding.seats.getText().toString();
        String per_hour = binding.perHourPrice.getText().toString();

        if (total_seats.equals("") || per_hour.equals(""))
        {
            Toast.makeText(UpdatePlaceActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Boolean checkid= DB.checkLocations(mPlace.getResult().getName());
            if (checkid==true){
                Boolean update = DB.updateAreas(Integer.parseInt(id),Integer.parseInt(total_seats), Double.valueOf(per_hour));
                if (update==true){
                    Toast.makeText(UpdatePlaceActivity.this, "Bus updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), AdminPanel.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(UpdatePlaceActivity.this, "New entry not updated", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(UpdatePlaceActivity.this, "ID doesnot exists", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
