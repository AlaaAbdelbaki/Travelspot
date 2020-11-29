package com.travelspot.travelspot.Models;


import org.json.JSONArray;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface PostsServices {

    @GET("services/getPosts")
    Call<ArrayList<Post>> getPosts();

    @GET("services/getPostComments")
    Call<JSONArray> getPostComments(@Query(value = "post",encoded = true) int postid);

    @GET("services/getPostLikes")
    Call<Integer> getPostLikes(@Query(value = "post",encoded = true) int postid);
}
