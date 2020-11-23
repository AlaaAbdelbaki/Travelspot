package com.travelspot.travelspot.Models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserServices {

    @GET("services/checkLogin")
    Call<Boolean> checkLogin(@Query(value ="email",encoded = true) String email,@Query(value = "pass",encoded = true) String pass);
}
