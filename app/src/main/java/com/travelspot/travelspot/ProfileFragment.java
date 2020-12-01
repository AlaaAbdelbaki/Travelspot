package com.travelspot.travelspot;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PostProcessor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.travelspot.travelspot.Models.Country;
import com.travelspot.travelspot.Models.Follower;
import com.travelspot.travelspot.Models.Post;
import com.travelspot.travelspot.Models.PostsServices;
import com.travelspot.travelspot.Models.ServicesClient;
import com.travelspot.travelspot.Models.User;
import com.travelspot.travelspot.Models.UserServices;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    SharedPreferences userStates;
    SharedPreferences.Editor userStatesEdit;
    UserServices userServices = ServicesClient.getClient().create(UserServices.class);
    PostsServices postsServices = ServicesClient.getClient().create(PostsServices.class);


    TextView name;
    TextView countriesCount;
    MaterialButton followersCount;
    MaterialButton followingCount;
    CircleImageView profilePicture;
    LinearLayout latestPost;
    User user;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_profile, container, false);
        //Button disconnect = v.findViewById(R.id.button_disconnect);
        userStates = v.getContext().getSharedPreferences("userStates", MODE_PRIVATE);



        name = v.findViewById(R.id.name);
        countriesCount = v.findViewById(R.id.countriesCount);
        followersCount = v.findViewById(R.id.followersCount);
        followingCount = v.findViewById(R.id.followingCount);
        profilePicture = v.findViewById(R.id.profilePic);
        latestPost = v.findViewById(R.id.latestPost);

        //get user details
        Call<User> getUserDetails = userServices.getUser(getArguments().getString("email"));
        getUserDetails.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.e("Email",getArguments().getString("email"));
                Log.e("response Body: ",new Gson().toJson(response.body()));
                if(!response.isSuccessful()){
                    //Response unsuccessful
                }else{
                    user = response.body();
                    Log.e("User: ",user.toString());
                    name.setText(user.getFirstName()+" "+user.getLastName()+" ");
                    Picasso.get().load("https://res.cloudinary.com/alaaab/image/upload/v1606730912/sample.jpg").into(profilePicture);

                    //get visited countries
                    Call<List<Country>> getCountries = userServices.getCountriesByUser(user.getId());
                    getCountries.enqueue(new Callback<List<Country>>() {
                        @Override
                        public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                            List<Country> countries = response.body();
                            Log.e("count", String.valueOf(countries.size()));
                            countriesCount.setText(countries.size()+" countries visited !");
                        }

                        @Override
                        public void onFailure(Call<List<Country>> call, Throwable t) {

                        }
                    });
                    //get followers
                    Call<List<Follower>> getFollowers = userServices.getFollowers(user.getId());
                    getFollowers.enqueue(new Callback<List<Follower>>() {
                        @Override
                        public void onResponse(Call<List<Follower>> call, Response<List<Follower>> response) {
                            List<Follower> followers = response.body();
                            followersCount.setText("Followers\n"+followers.size());

                        }

                        @Override
                        public void onFailure(Call<List<Follower>> call, Throwable t) {

                        }
                    });
                    //get user's followings
                    Call<List<Follower>> getFollowing = userServices.getFollowings(user.getId());
                    getFollowing.enqueue(new Callback<List<Follower>>() {
                        @Override
                        public void onResponse(Call<List<Follower>> call, Response<List<Follower>> response) {
                            List<Follower> followings = response.body();
                            followingCount.setText("Following\n"+followings.size());
                        }

                        @Override
                        public void onFailure(Call<List<Follower>> call, Throwable t) {

                        }
                    });
                    //get user's posts
                    Call<List<Post>> getPosts = postsServices.getPostsByUser(user.getId());
                    getPosts.enqueue(new Callback<List<Post>>() {
                        @Override
                        public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                            List<Post> posts = response.body();
                            if(posts.size()==0){
                                latestPost.removeAllViews();
                                TextView empty = new TextView(getContext());
                                empty.setText("This user has no posts");
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                );
                                params.setMargins(20,20,20,20);
                                params.gravity = Gravity.CENTER;
                                empty.setLayoutParams(params);
                                latestPost.addView(empty);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Post>> call, Throwable t) {

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });



        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,new EditProfileFragment()).addToBackStack("prev").commit();
            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetDialog bottomSheetDialog = BottomSheetDialog.newInstance();
                bottomSheetDialog.show(getActivity().getSupportFragmentManager(),
                        "add_photo_dialog_fragment");
            }
        });



//        disconnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                userStatesEdit=userStates.edit();
//                userStatesEdit.clear();
//                userStatesEdit.apply();
//                Intent intent = new Intent(getActivity(), FirstActivity.class);
//                startActivity(intent);
//            }
//        });

        return v;
    }



}