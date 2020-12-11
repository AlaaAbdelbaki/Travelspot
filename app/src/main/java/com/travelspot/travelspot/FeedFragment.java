package com.travelspot.travelspot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travelspot.travelspot.Adapters.PostAdapter;
import com.travelspot.travelspot.Models.Post;
import com.travelspot.travelspot.Models.PostsCallBack;
import com.travelspot.travelspot.Models.PostsServices;
import com.travelspot.travelspot.Models.ServicesClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FeedFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Post> posts = new ArrayList<Post>();
    PostAdapter mAdapter;
    PostsServices postsServices;
    PostsCallBack postsCallBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        postsServices = ServicesClient.getClient().create(PostsServices.class);

        View v =inflater.inflate(R.layout.fragment_feed, container, false);
        v.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("FOCUS", v.getContext().getPackageName());
            }
        });
        recyclerView = v.findViewById(R.id.feed);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadPostsData(postsCallBack);

        return v;
    }

    public void loadPostsData(final PostsCallBack postsCallBack)
    {
        Call<ArrayList<Post>> call = postsServices.getPosts();

        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                ArrayList<Post> res = response.body();
                if(!res.isEmpty())
                {
                    ArrayList<Post> posts;
                    posts=res;
                    mAdapter = new PostAdapter(getContext(),posts);
                    recyclerView.setAdapter(mAdapter);



                }else{
                    Log.d("Failure","Res is empty");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {

                postsCallBack.onError();
            }
        });
    }


}