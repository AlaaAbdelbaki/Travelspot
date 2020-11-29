package com.travelspot.travelspot.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travelspot.travelspot.Models.Post;
import com.travelspot.travelspot.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.FeedHolder> {

    private final ArrayList<Post> posts;
    private Context mContext;

    public PostAdapter(Context mContext,ArrayList<Post> posts)
    {
        this.mContext=mContext;
        this.posts = posts;
    }


    @NonNull
    @Override
    public PostAdapter.FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(mContext).inflate(R.layout.fragment_feed_item,parent,false);
        return new FeedHolder(item,this);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.FeedHolder holder, int position) {
        final Post singleItem = posts.get(position);
        holder.Title.setText("TEST");
        holder.Location.setText("Tunisia");
        holder.Body.setText("TEST BODY");

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class FeedHolder extends RecyclerView.ViewHolder{
        public ImageView profilePicture;
        public TextView Title;
        public TextView Location;
        public TextView Body;
        public LinearLayout picturesContainerLayout;
        public ImageButton like;
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


