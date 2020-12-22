package com.travelspot.travelspot.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.travelspot.travelspot.Models.Comment;
import com.travelspot.travelspot.Models.Post;
import com.travelspot.travelspot.Models.PostsServices;
import com.travelspot.travelspot.Models.ServicesClient;
import com.travelspot.travelspot.Models.User;
import com.travelspot.travelspot.Models.UserServices;
import com.travelspot.travelspot.R;
import com.travelspot.travelspot.UserSession;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private final ArrayList<Comment> comments;
    private final Context mContext;
    PostsServices postsServices;
    UserServices userServices;
    User u = UserSession.instance.getU();
    private final int postid;

    public CommentAdapter(ArrayList<Comment> comments,Context mContext,int postid) {
        this.mContext=mContext;
        this.comments = comments;
        this.postid=postid;
        this.userServices = ServicesClient.getClient().create(UserServices.class);
        this.postsServices = ServicesClient.getClient().create(PostsServices.class);
    }

    @NonNull
    @Override
    public CommentAdapter.CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(mContext).inflate(R.layout.fragment_comment_item,parent,false);
        return new CommentHolder(item,this);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentHolder holder, int position) {
        final Comment singleItem = comments.get(position);
        getData(singleItem,holder,singleItem.getUserId(),singleItem.getPostId());

    }

    public void getData(Comment singleItem,CommentHolder holder,int userid,int postid)
    {
        JsonObject params = new JsonObject();
        params.addProperty("userid",userid);
        params.addProperty("postid",postid);

        Call<User> call = userServices.getUserP(params);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                assert user != null;
                holder.comment.setText(String.format("%s: %s", user.getFirstName(), singleItem.getContent()));
                Picasso.get()
                        .load("http://192.168.1.5/Ressources/"+user.getProfilePicture())
                        .resize(50, 50)
                        .centerCrop()
                        .into(holder.profilePicture);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
    public static class CommentHolder extends RecyclerView.ViewHolder{

        final CommentAdapter mAdapter;
        public CircleImageView profilePicture;
        public TextView comment;
        public CommentHolder(@NonNull View itemView,CommentAdapter mAdapter) {
            super(itemView);
            this.mAdapter=mAdapter;
            this.profilePicture = itemView.findViewById(R.id.profilePictureComment);
            this.comment = itemView.findViewById(R.id.Comment);
        }
    }
}
