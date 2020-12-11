package com.travelspot.travelspot.Models;


import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface PostsServices {

    @GET("services/getPosts")
    Call<ArrayList<Post>> getPosts();

    @Headers("Content-Type: text/plain")
    @POST("services/getPostMedia")
    Call<ArrayList<String>> getPostMedia(@Query(value = "postid",encoded = true) int postid);

    @GET("/services/getpostsbyuser")
    Call<List<Post>> getPostsByUser(@Query(value = "id",encoded = true)int userId);

    @GET("services/getPostComments")
    Call<JSONArray> getPostComments(@Query(value = "post",encoded = true) int postid);

    @GET("services/getPostLikes")
    Call<Integer> getPostLikes(@Query(value = "post",encoded = true) int postid);

    @Headers("Content-Type: application/json")
    @POST("services/likePost")
    Call<Boolean> likePost(@Body JsonObject parms);

    @Headers("Content-Type: application/json")
    @POST("services/unlikePost")
    Call<Boolean> unlikePost(@Body JsonObject parms);
}
