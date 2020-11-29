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
import com.travelspot.travelspot.Models.PostsServices;
import com.travelspot.travelspot.Models.ServicesClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FeedFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Post> posts;
    PostAdapter mAdapter;
    PostsServices postsServices;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        postsServices = ServicesClient.getClient().create(PostsServices.class);

        View v =inflater.inflate(R.layout.fragment_feed, container, false);
        recyclerView = v.findViewById(R.id.feed);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));

       /* Call<ArrayList<Post>> call = postsServices.getPosts();
        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                ArrayList<Post> res = response.body();
                if(!res.isEmpty())
                {
                    posts=res;
                    Log.d("POSTS", posts.toString());

                }else{
                    Log.d("Failure","Res is empty");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {

            }
        });*/

        posts.add(new Post(1));
        posts.add(new Post(2));
        posts.add(new Post(3));
        posts.add(new Post(4));
        posts.add(new Post(5));
        mAdapter = new PostAdapter(getActivity(),posts);
        recyclerView.setAdapter(mAdapter);



        return v;
    }


}