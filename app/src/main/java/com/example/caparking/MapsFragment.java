package com.example.caparking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.navigation.NavController;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.caparking.Common.Common;
import com.example.caparking.Helper.DBHelper;
import com.example.caparking.Model.MyPlaces;
import com.example.caparking.Model.Results;
import com.example.caparking.Remote.IGoogleAPIService;
import com.example.caparking.databinding.FragmentMapsBinding;
import com.example.caparking.databinding.FragmentPaymentBinding;
import com.example.caparking.util.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class MapsFragment extends Fragment implements OnMapReadyCallback {


    private static final int MY_PERMISSION_CODE = 1000;
    private GoogleMap mMap;

    private double latitude,longitude;
    private Location mLastLocation;
    private Marker mMarker;
    private LocationRequest mLocationRequest;
    IGoogleAPIService mService;
    DBHelper DB;
    MyPlaces currentPlaces;
    private View header;
    SessionManager sessionManager;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    private NavController navController;
    private FragmentMapsBinding binding;

    public MapsFragment() {
        // Required empty public constructor
    }
    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(requireContext());
        DB = new DBHelper(requireContext());




        mService = Common.getGoogleAPIService();

        // searchLocation();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentMapsBinding.inflate(
                inflater, container, false);
        View view = binding.getRoot();
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Common.checkLocationPermission(requireContext());
        }

        buildLocationRequest();
        buildLocationCallback();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,locationCallback, Looper.myLooper());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        nearByPlaces("parking");
                    }
                });
            }
        }, 2000);
        //here data must be an instance of the class MarsDataProvider
        return view;
    }



    @Override
    public void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback()   {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mLastLocation = locationResult.getLastLocation();

                if (mMarker != null)    {
                    mMarker.remove();
                }

                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();

                LatLng latLng = new LatLng(latitude,longitude);

                MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                        .title("Your Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.redcar));
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

                mMarker = mMap.addMarker(markerOptions);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            }
        };
    }

    @SuppressLint("RestrictedApi")
    private void buildLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setSmallestDisplacement(10f);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    private void nearByPlaces(final String placeType) {
        if(mMap!=null) {
            mMap.clear();
            String url = Common.getUrl(latitude, longitude, placeType);

            mService.getNearByPlaces(url)
                    .enqueue(new Callback<MyPlaces>() {
                        @Override
                        public void onResponse(Call<MyPlaces> call, Response<MyPlaces> response) {

                            currentPlaces = response.body();

                            if (response.isSuccessful()) {
                                for (int i = 0; i < response.body().getResults().length; i++) {

                                    MarkerOptions markerOptions = new MarkerOptions();
                                    Results googlePlaces = response.body().getResults()[i];

                                    double lat = Double.parseDouble(googlePlaces.getGeometry().getLocation().getLat());
                                    double lng = Double.parseDouble(googlePlaces.getGeometry().getLocation().getLng());

                                    String placeName = googlePlaces.getName();
                                    String vicinity = googlePlaces.getVicinity();
                                    LatLng latLng = new LatLng(lat, lng);
                                    markerOptions.position(latLng);
                                    markerOptions.title(placeName);
                                    Log.d("T", placeName);

                                    insertAreas(32,12.5,placeName);
                                    if (placeType.equals("parking")) {
                                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.parking));
                                    } else {
                                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                    }

                                    markerOptions.snippet(String.valueOf(i));

                                    mMap.addMarker(markerOptions);

                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyPlaces> call, Throwable t) {

                        }
                    });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case MY_PERMISSION_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        mMap.setMyLocationEnabled(true);
                        buildLocationRequest();
                        buildLocationCallback();

                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

                        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
                    }
                }
            }
            break;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }
        else    {
            mMap.setMyLocationEnabled(true);
        }


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if (marker.getSnippet() != null) {

                    Common.currentResults = currentPlaces.getResults()[Integer.parseInt(marker.getSnippet())];
                    replaceFragment();
                    //startActivity(new Intent(MapsActivity.this, ViewPlaceActivity.class));
                    //startActivity(new Intent(MapsActivity.this, UpdatePlaceActivity.class));
                    /*if(getCallingActivity().getClassName()!=null){
                        if(getCallingActivity().getClassName().equals(AdminPanel.class.getName())){

                        }

                    }*/

                }
                return true;
            }
        });
    }

    private void insertAreas(int total_seats,double perHourPrice, String locationName) {

        Boolean checkLocation = DB.checkLocations(locationName);

        if (checkLocation==false){
            Boolean insert = DB.insertAreas(total_seats,perHourPrice,locationName);
            if (insert == true) {
                Log.d("Tag","success");
            } else {
                Log.d("Tag","error");
            }
        }
    }
    private void replaceFragment(){
        ViewPlaceFragment newFragment = new ViewPlaceFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}