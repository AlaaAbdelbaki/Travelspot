package com.travelspot.travelspot.Models;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MediaServices {
    @Multipart
    @POST("services/uploadMedia")
    Call<Boolean> uploadMedia(@Part("postId") RequestBody id, @Part MultipartBody.Part file);
}
