package com.travelspot.travelspot.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import com.travelspot.travelspot.MainActivity;
import com.travelspot.travelspot.Models.Trip;
import com.travelspot.travelspot.Models.User;
import com.travelspot.travelspot.ProfileFragment;
import com.travelspot.travelspot.R;
import com.travelspot.travelspot.UserSession;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder>{

    private List<User> users;
    private Context mContext;

    public UserAdapter(Context mContext, List<User> users){
        this.users = users;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.UserHolder(item,this);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        final User singleItem = users.get(position);
        if(singleItem.getProfilePicture()==null){
            Picasso.get().load("http://192.168.1.12:3000/uploads/dafault.jpg").into(holder.profilePic);
        }else{
            Picasso.get().load("http://192.168.1.12:3000/"+singleItem.getProfilePicture()).into(holder.profilePic);
            holder.profilePic.setRotation(90);
        }
        holder.name.setText(singleItem.getFirstName()+" "+singleItem.getLastName());
        holder.viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment profile = new ProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("email", singleItem.getEmail());
                profile.setArguments(bundle);
                if(mContext instanceof MainActivity){
                    ((MainActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,profile).commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public static class UserHolder extends RecyclerView.ViewHolder{

        public ImageView profilePic;
        public TextView name;
        public MaterialButton viewProfile;
        final UserAdapter mAdapter;

        public UserHolder(@NonNull View itemView, UserAdapter mAdapter) {
            super(itemView);

            this.profilePic = itemView.findViewById(R.id.profilePic);
            this.name = itemView.findViewById(R.id.nameAndLastName);
            this.viewProfile = itemView.findViewById(R.id.viewProfile);
            this.mAdapter = mAdapter;
        }
    }
}
