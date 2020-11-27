package com.travelspot.travelspot.Models;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserServices {

    @Headers("Content-Type: application/json")
    @POST("services/checkLogin")
    Call<Boolean> checkLogin(@Body User user);
}
