package com.example.caparking;


import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.caparking.Common.Common;
import com.example.caparking.Model.MyPlaces;
import com.example.caparking.Model.Results;
import com.example.caparking.Remote.IGoogleAPIService;

import java.io.Closeable;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class MainViewModel extends ViewModel {

    @Inject
    MainRepository mainRepository;


    @Inject
    public MainViewModel(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }


    private final MutableLiveData<Results[]> modelResultsMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<MyPlaces> modelResults = new MutableLiveData<>();

    public void setMarkerLocation(Double latitude, Double longitude, String placeType) {

        IGoogleAPIService apiService = Common.getGoogleAPIService();
        String url = Common.getUrl(latitude, longitude, placeType);
        Log.d("url", url);
        apiService.getNearByPlaces(url)
                .enqueue(new Callback<MyPlaces>() {
                    @Override
                    public void onResponse(Call<MyPlaces> call, Response<MyPlaces> response) {
                        if (!response.isSuccessful()) {
                            Log.e("response", response.toString());
                        } else if (response.body() != null) {
                            MyPlaces currentPlaces = response.body();
                            modelResults.postValue(currentPlaces);
                            Results[] items = response.body().getResults();
                            modelResultsMutableLiveData.postValue(items);
                            Log.d("items", String.valueOf(response.body().getResults().length));
                        }
                    }

                    @Override
                    public void onFailure(Call<MyPlaces> call, Throwable t) {

                    }
                });
    }

    public LiveData<Results[]> getMarkerLocation() {
        return modelResultsMutableLiveData;
    }
    public LiveData<MyPlaces> getMarker() {
        return modelResults;
    }
}
