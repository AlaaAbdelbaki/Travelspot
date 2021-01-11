package com.travelspot.travelspot.Models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TripServices {

    @POST("services/addTrip")
    Call<Boolean> addTrip(@Body Trip trip);

    @GET("services/getTripsByUser")
    Call<List<Trip>> getTrips(@Query( value = "userId", encoded = true)int userId);
}
