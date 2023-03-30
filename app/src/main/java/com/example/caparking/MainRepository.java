package com.example.caparking;

import com.example.caparking.Remote.IGoogleAPIService;

public class MainRepository {

    IGoogleAPIService requestApi;

    public MainRepository(IGoogleAPIService requestApi) {
        this.requestApi = requestApi;
    }
}
