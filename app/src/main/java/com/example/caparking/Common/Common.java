package com.example.caparking.Common;


import com.example.caparking.Model.Results;
import com.example.caparking.Remote.IGoogleAPIService;
import com.example.caparking.Remote.RetrofitClient;
import com.example.caparking.Remote.RetrofitScalarClient;

public class Common {

    public static Results currentResults;

    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/";

    public static IGoogleAPIService getGoogleAPIService()   {
        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }

    public static IGoogleAPIService getGoogleAPIServiceScalars()   {
        return RetrofitScalarClient.getScalarClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }
}
