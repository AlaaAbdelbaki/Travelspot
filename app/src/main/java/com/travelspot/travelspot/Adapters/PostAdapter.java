package com.travelspot.travelspot.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.like.LikeButton;
import com.squareup.picasso.Picasso;
import com.travelspot.travelspot.Models.Post;
import com.travelspot.travelspot.Models.PostsCallBack;
import com.travelspot.travelspot.Models.PostsServices;
import com.travelspot.travelspot.Models.ServicesClient;
import com.travelspot.travelspot.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.FeedHolder> {

    private final ArrayList<Post> posts;
    private Context mContext;
    PostsServices postsServices;
    PostsCallBack postsCallBack;

    public PostAdapter(Context mContext,ArrayList<Post> posts)
    {
        this.mContext=mContext;
        this.posts = posts;
        this.postsServices = ServicesClient.getClient().create(PostsServices.class);

    }


    @NonNull
    @Override
    public PostAdapter.FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(mContext).inflate(R.layout.fragment_feed_item,parent,false);
        return new FeedHolder(item,this);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.FeedHolder holder, int position) {
        Log.d("Recycler", "render Item");
        final Post singleItem = posts.get(position);
        holder.Title.setText("TEST");
        holder.Location.setText("Tunisia");
        holder.Body.setText(singleItem.getBody());
        Picasso.get()
                .load("http://192.168.1.3/Ressources/profile.png")
                .resize(50, 50)
                .centerCrop()
                .into(holder.profilePicture);

        loadPostMedia(postsCallBack,singleItem,holder);

    }
        public void loadPostMedia(final PostsCallBack postsCallBack,Post singleItem,FeedHolder holder)
    {
        Call<ArrayList<String>> call = postsServices.getPostMedia(singleItem.getId());

        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                ArrayList<String> res = response.body();
                assert res != null;
                if(!res.isEmpty())
                {
                    ArrayList<String> media;
                    media=res;

                    for (String item:media) {
                        ImageView i = new ImageView(holder.picturesContainerLayout.getContext());
                        Picasso.get()
                                .load("http://192.168.1.3/Ressources/"+item)
                                .into(i);
                        holder.picturesContainerLayout.addView(i);                    }

                }else{
                    Log.d("Failure","Res is empty");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {

                postsCallBack.onError();
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class FeedHolder extends RecyclerView.ViewHolder{
        public ImageView profilePicture;
        public TextView Title;
        public TextView Location;
        public TextView Body;
        public LinearLayout picturesContainerLayout;
        public LikeButton like;
        public ImageButton comments;
        final PostAdapter mAdapter;


        public FeedHolder(@NonNull View itemView,PostAdapter mAdapter) {
            super(itemView);

            this.profilePicture = itemView.findViewById(R.id.profilePicture);
            this.Title =itemView.findViewById(R.id.Title);
            this.Location=itemView.findViewById(R.id.Location);
            this.Body=itemView.findViewById(R.id.Body);
            this.like=itemView.findViewById(R.id.button_like);
            this.comments=itemView.findViewById(R.id.button_comments);
            this.picturesContainerLayout=itemView.findViewById(R.id.picturesContainerLayout);
            this.mAdapter = mAdapter;



        }
    }
}


