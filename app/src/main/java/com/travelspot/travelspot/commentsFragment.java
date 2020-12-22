package com.travelspot.travelspot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.travelspot.travelspot.Adapters.CommentAdapter;
import com.travelspot.travelspot.Models.Comment;
import com.travelspot.travelspot.Models.PostsServices;
import com.travelspot.travelspot.Models.ServicesClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class commentsFragment extends Fragment {

    RecyclerView recyclerView;
    CommentAdapter mAdapter;
    PostsServices postsServices;
    private static int postid;

    public commentsFragment(int postid) {
        commentsFragment.postid = postid;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postsServices = ServicesClient.getClient().create(PostsServices.class);


    }




    public void getPostComments(){
        JsonObject params = new JsonObject();
        params.addProperty("postid",postid);
        Call<ArrayList<Comment>> call = postsServices.getPostComments(params);
        call.enqueue(new Callback<ArrayList<Comment>>() {
            @Override
            public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                ArrayList<Comment> res = response.body();
                if(!res.isEmpty())
                {
                    Log.d("Comments", res.toString());
                    ArrayList<Comment> comments;
                    comments=res;
                    mAdapter = new CommentAdapter(comments,getContext(),postid);

                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {

            }
        });


    }
    public static commentsFragment newInstance(){ return new commentsFragment(postid);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v =inflater.inflate(R.layout.fragment_comments, container, false);
        recyclerView = v.findViewById(R.id.Comments);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getPostComments();

        return v;
    }
}