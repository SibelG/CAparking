package com.example.caparking;

import static com.example.caparking.util.LogUtil.logD;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.caparking.Common.Common;
import com.example.caparking.Helper.DBHelper;
import com.example.caparking.Model.PlaceDetail;
import com.example.caparking.Remote.IGoogleAPIService;
import com.example.caparking.databinding.FragmentViewPlaceBinding;
import com.example.caparking.util.SessionManager;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class ViewPlaceFragment extends Fragment {


    SessionManager manager;
    IGoogleAPIService mService;
    DBHelper DB;
    PlaceDetail mPlace;
    private FragmentViewPlaceBinding binding;
    String price;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        binding = FragmentViewPlaceBinding.inflate(
                inflater, container, false);
        View view = binding.getRoot();

        binding.placesName.setText("");
        binding.placesAddress.setText("");
        binding.placesOpenHour.setText("");


        binding.btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mPlace.getResult().getUrl()));
                startActivity(mapIntent);
            }
        });

        binding.seatSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parkingSlot();
            }
        });

        if (Common.currentResults.getPhotos() != null && Common.currentResults.getPhotos().length > 0)  {

            Picasso.with(requireContext())
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
                        binding.pAddress.setText(mPlace.getResult().getFormatted_address());
                        binding.pName.setText(mPlace.getResult().getName());

                        Cursor cursor = DB.viewParkingAreas(String.valueOf(DB.GetLocId(binding.placesName.getText().toString())));

                        if (cursor != null && cursor.getCount() == 1) {
                            cursor.moveToFirst();
                            price = String.valueOf(cursor.getDouble(2));

                            binding.perPrice.setText(String.valueOf(cursor.getDouble(2)));
                        }

                        else{
                            Log.d("error","error");
                        }
                        System.out.println("price"+price);
                    }

                    @Override
                    public void onFailure(Call<PlaceDetail> call, Throwable t) {

                    }
                });
        //here data must be an instance of the class MarsDataProvider
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MapsActivity)getActivity()).setNavigationVisibility(true);

        DB = new DBHelper(requireContext());
        manager = new SessionManager(requireContext());
        mService = Common.getGoogleAPIService();


    }

    public void parkingSlot() {
        int id = DB.GetLocId(binding.placesName.getText().toString());
        manager.createParkingSession(id);
        SeatSelection ldf = new SeatSelection ();
        Bundle args = new Bundle();
        args.putString("price", price);
        args.putString("parkName",binding.placesName.getText().toString());
        ldf.setArguments(args);
        getFragmentManager().beginTransaction().add(R.id.nav_host_fragment, ldf).commit();

    }
}
