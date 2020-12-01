package com.travelspot.travelspot.Models;

import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserServices {

    @Headers("Content-Type: application/json")
    @POST("services/checkLogin")
    Call<Boolean> checkLogin(@Body User user);


    @GET("services/getUser")
    Call<User> getUser(@Query(value = "email",encoded = true) String email);

    @GET("services/getCountries")
    Call<List<Country>> getCountriesByUser(@Query(value = "id",encoded = true)int userId);

    @PUT("services/updateUser")
    Call<Boolean> updateUser(@Body User user);

    @GET("services/getFollowers")
    Call<List<Follower>> getFollowers(@Query(value = "id",encoded = true)int userId);
    @GET("services/getFollowings")
    Call<List<Follower>> getFollowings(@Query(value = "id",encoded = true)int userId);



}
