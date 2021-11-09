package com.example.adjust.model;

import com.google.gson.annotations.SerializedName;

public class AdjustResponse {
    public int getSeconds() {
        return seconds;
    }

    public int getId() {
        return id;
    }

    @SerializedName("seconds")
    private int seconds;
    @SerializedName("id")
    private int id;
}
