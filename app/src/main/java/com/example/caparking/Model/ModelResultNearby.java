package com.example.caparking.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelResultNearby {

    @SerializedName("results")
    private List<Results> modelResults;

    public List<Results> getModelResults() {
        return modelResults;
    }

    public void setModelResults(List<Results> modelResults) {
        this.modelResults = modelResults;
    }

}