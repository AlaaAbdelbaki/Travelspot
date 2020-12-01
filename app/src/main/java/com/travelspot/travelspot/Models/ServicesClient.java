package com.travelspot.travelspot.Models;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicesClient {
    public static Retrofit getClient() {


        return new Retrofit.Builder()
                .baseUrl("http://192.168.1.3:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
