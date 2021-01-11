package com.travelspot.travelspot.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;
import com.travelspot.travelspot.BottomSheetDialog;
import com.travelspot.travelspot.CommentSheet;
import com.travelspot.travelspot.FeedFragment;
import com.travelspot.travelspot.MainActivity;
import com.travelspot.travelspot.Models.Post;
import com.travelspot.travelspot.Models.PostsCallBack;
import com.travelspot.travelspot.Models.PostsServices;
import com.travelspot.travelspot.Models.ServicesClient;
import com.travelspot.travelspot.Models.User;
import com.travelspot.travelspot.R;
import com.travelspot.travelspot.UserSession;
import com.travelspot.travelspot.commentsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.FeedHolder> {

    private final ArrayList<Post> posts;
    private Context mContext;
    PostsServices postsServices;
    PostsCallBack postsCallBack;
    User u = UserSession.instance.getU();

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
        final Post singleItem = posts.get(position);

        getTitle(singleItem.getUserId(),singleItem.getId(),holder);

        holder.Location.setText(singleItem.getPosition());
        holder.Body.setText(singleItem.getBody());
        holder.Body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentSheet commentSheet = new CommentSheet();
                Bundle bundle = new Bundle();
                String myMessage = (String) holder.Body.getText();
                bundle.putString("Post", myMessage );
                commentSheet.setArguments(bundle);
                commentSheet.show(((MainActivity)mContext).getSupportFragmentManager(),"full_comment_dialog");
            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,new commentsFragment(singleItem.getId())).addToBackStack("back_to_posts").commit();
            }
        });
        loadPostMedia(postsCallBack,singleItem,holder);
        try {
            setLikePost(singleItem,holder);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getTitle(int userid,int postid,FeedHolder holder)
    {

        JsonObject params = new JsonObject();
        params.addProperty("userid",userid);
        params.addProperty("postid",postid);

        Call<User> call = postsServices.getPostUser(params);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                assert user != null;
                holder.Title.setText(String.format("%s added a new post", user.getFirstName()));
                Picasso.get()
                        .load("http://192.168.1.17:3000/"+user.getProfilePicture())
                        .resize(50, 50)
                        .centerCrop()
                        .rotate(90)
                        .into(holder.profilePicture);
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
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
                        //Log.e("pictureName: ", item);
                        ImageView i = new ImageView(holder.picturesContainerLayout.getContext());
                        Picasso.get()
                                .load("http://192.168.1.17:3000/"+item)
                                .rotate(90)
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

    public void setLikePost(Post singleItem,FeedHolder holder) throws JSONException {
        JsonObject params = new JsonObject();
        params.addProperty("postid",singleItem.getId());
        params.addProperty("userid",u.getId());

        holder.like.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Call<Boolean> call = postsServices.likePost(params);
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Boolean res = response.body();
                        if(res)
                        {
                            Toast.makeText(mContext,"Liked", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(mContext,"Allready Liked", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Call<Boolean> call = postsServices.unlikePost(params);
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Boolean res = response.body();
                        if(res)
                        {
                            Toast.makeText(mContext,"Unliked", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(mContext,"Liked", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });
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


