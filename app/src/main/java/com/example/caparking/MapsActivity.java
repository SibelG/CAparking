package com.example.caparking;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.caparking.Common.Common;
import com.example.caparking.Helper.DBHelper;
import com.example.caparking.Helper.HelperUtilities;
import com.example.caparking.Model.MyPlaces;
import com.example.caparking.Model.Results;
import com.example.caparking.Remote.IGoogleAPIService;
import com.google.android.gms.common.api.ApiException;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSION_CODE = 1000;
    private GoogleMap mMap;

    private double latitude,longitude;
    private Location mLastLocation;
    private Marker mMarker;
    private LocationRequest mLocationRequest;
    IGoogleAPIService mService;

    MyPlaces currentPlaces;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    DBHelper DB = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mService = Common.getGoogleAPIService();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            checkLocationPermission();
        }

        //nearByPlaces("parking");

       /* findButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                nearByPlaces("parking");
            }
        });*/
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())   {

                    case R.id.action_parking:
                        nearByPlaces("parking");
                        break;

                }

                return true;
            }
        });

        buildLocationRequest();
        buildLocationCallback();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,locationCallback, Looper.myLooper());

        // searchLocation();
    }



    @Override
    protected void onStop() {
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
            String url = getUrl(latitude, longitude, placeType);

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

    private String getUrl(double latitude, double longitude, String placeType) {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?keyword=parking");
        googlePlacesUrl.append("&location="+latitude+","+longitude);
        googlePlacesUrl.append("&radius="+10000);
        googlePlacesUrl.append("&type="+placeType);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=AIzaSyAgjvl_cLGKpKOJ85WC3BpzVT_dv_tLOwI");

        Log.d("getUrl",googlePlacesUrl.toString());

        return googlePlacesUrl.toString();
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,new String[]{

                        android.Manifest.permission.ACCESS_FINE_LOCATION

                },MY_PERMISSION_CODE);
            }
            else    {
                ActivityCompat.requestPermissions(this,new String[]{

                        Manifest.permission.ACCESS_FINE_LOCATION

                },MY_PERMISSION_CODE);
            }
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case MY_PERMISSION_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        mMap.setMyLocationEnabled(true);
                        buildLocationRequest();
                        buildLocationCallback();

                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

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

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                    insertLocation(Common.currentResults.getName());

                    startActivity(new Intent(MapsActivity.this, ViewPlaceActivity.class));
                }
                return true;
            }
        });
    }

    private void insertLocation(String locationName) {
        Cursor cursor = DB.selectLocationName(HelperUtilities.filter(locationName));

        if (cursor != null && cursor.getCount() > 0) {
            return;
        }else{
            Boolean insert = DB.insertParkingLocation(locationName);
            if (insert == true) {
                Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
