package com.example.caparking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caparking.Common.Common;
import com.example.caparking.Helper.DBHelper;
import com.example.caparking.Model.PlaceDetail;
import com.example.caparking.Remote.IGoogleAPIService;
import com.example.caparking.util.SessionManager;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPlaceActivity extends AppCompatActivity {

    ImageView photo;
    RatingBar ratingBar;
    Button btnViewOnMap;
    TextView place_name,place_address,opening_hours,pAddress,pName;
    SessionManager manager;
    IGoogleAPIService mService;
    DBHelper DB;
    PlaceDetail mPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_place);
        DB = new DBHelper(this);
        photo = findViewById(R.id.photo);

        ratingBar = findViewById(R.id.ratingBar);

        btnViewOnMap = findViewById(R.id.btnShowMap);


        place_address = findViewById(R.id.places_address);
        place_name = findViewById(R.id.places_name);
        pAddress = findViewById(R.id.pAddress);
        pName = findViewById(R.id.pName);
        opening_hours = findViewById(R.id.places_open_hour);

        mService = Common.getGoogleAPIService();

        place_name.setText("");
        place_address.setText("");
        opening_hours.setText("");
        manager=new SessionManager(getApplicationContext());

        btnViewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mPlace.getResult().getUrl()));
                startActivity(mapIntent);
            }
        });

        if (Common.currentResults.getPhotos() != null && Common.currentResults.getPhotos().length > 0)  {

            Picasso.with(this)
                    .load(getPhotoOfPlaces(Common.currentResults.getPhotos()[0].getPhoto_reference(),1000))
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .into(photo);
        }

        if (Common.currentResults.getRating() != null && !TextUtils.isEmpty(Common.currentResults.getRating()))  {
            ratingBar.setRating(Float.parseFloat(Common.currentResults.getRating()));
        }
        else    {
            ratingBar.setVisibility(View.GONE);
        }

        if (Common.currentResults.getOpening_hours() != null)  {
            opening_hours.setText("Open Now : "+Common.currentResults.getOpening_hours().getOpen_now());
        }
        else    {
            opening_hours.setVisibility(View.GONE);
        }

        mService.getDetailPlaces(getPlaceDetailUrl(Common.currentResults.getPlace_id()))
                .enqueue(new Callback<PlaceDetail>() {
                    @Override
                    public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {

                        mPlace = response.body();

                        place_address.setText(mPlace.getResult().getFormatted_address());
                        place_name.setText(mPlace.getResult().getName());
                        pAddress.setText(mPlace.getResult().getFormatted_address());
                        pName.setText(mPlace.getResult().getName());
                    }

                    @Override
                    public void onFailure(Call<PlaceDetail> call, Throwable t) {

                    }
                });
    }



    private String getPlaceDetailUrl(String place_id) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json");
        url.append("?placeid="+place_id);
        url.append("&key=AIzaSyAgjvl_cLGKpKOJ85WC3BpzVT_dv_tLOwI");

        return url.toString();
    }

    private String getPhotoOfPlaces(String photo_reference,int maxWidth) {

        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
        url.append("?maxwidth="+maxWidth);
        url.append("&photoreference="+photo_reference);
        url.append("&key=AIzaSyAgjvl_cLGKpKOJ85WC3BpzVT_dv_tLOwI");

        return url.toString();
    }

    public void parkingSlot(View view) {
        int id = DB.GetLocId(place_name.getText().toString());
        manager.createParkingSession(id);
        Intent intent = new Intent(getApplicationContext(), SeatSelection.class);
        startActivity(intent);
    }
}
