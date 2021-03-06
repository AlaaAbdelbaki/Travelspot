package com.travelspot.travelspot;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.blongho.country_data.World;
import com.google.android.material.button.MaterialButton;
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
    CircleImageView postProfilePic;
    TextView postNameAndLastName;
    ImageView country1;
    ImageView country2;
    ImageView country3;
    ImageView country4;

    LinearLayout latestPost;
    User user;
    User postUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        //Button disconnect = v.findViewById(R.id.button_disconnect);
        userStates = v.getContext().getSharedPreferences("userStates", MODE_PRIVATE);


        name = v.findViewById(R.id.name);
        countriesCount = v.findViewById(R.id.countriesCount);
        followersCount = v.findViewById(R.id.followersCount);
        followingCount = v.findViewById(R.id.followingCount);
        profilePicture = v.findViewById(R.id.profilePic);
        latestPost = v.findViewById(R.id.latestPost);
        country1 = v.findViewById(R.id.country1);
        country2 = v.findViewById(R.id.country2);
        country3 = v.findViewById(R.id.country3);
        country4 = v.findViewById(R.id.country4);

        //get user details
        if (!UserSession.instance.getU().getEmail().equals(getArguments().getString("email"))) {
            userServices.getUser(getArguments().getString("email")).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    user = response.body();
                    name.setText(user.getFirstName() + " " + user.getLastName() + " ");
                    if(user.getProfilePicture()==null){
                        Picasso.get().load("http://192.168.1.12:3000/uploads/dafault.jpg").into(profilePicture);
                    }else{
                        Picasso.get().load("http://192.168.1.12:3000/"+user.getProfilePicture()).into(profilePicture);
                        profilePicture.setRotation(90);
                    }

                    Call<List<Country>> getCountries = userServices.getCountriesByUser(user.getId());
                    getCountries.enqueue(new Callback<List<Country>>() {
                        @Override
                        public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                            List<Country> countries = response.body();
                            Log.e("count", String.valueOf(countries.size()));
                            countriesCount.setText(countries.size() + " countries visited !");
                            World.init(getContext());


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
                            followersCount.setText("Followers\n" + followers.size());

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
                            followingCount.setText("Following\n" + followings.size());
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
                            //If User has no posts
                            if (posts.size() == 0) {
                                latestPost.removeAllViews();
                                TextView empty = new TextView(getContext());
                                empty.setText("This user has no posts");
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                );
                                params.setMargins(20, 20, 20, 20);
                                params.gravity = Gravity.CENTER;
                                empty.setLayoutParams(params);
                                latestPost.addView(empty);
                                //If user has posts
                            }else{
                                Post post = posts.get(0);

                                userServices.getUser(user.getId()).enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        postUser = response.body();
                                        TextView postNameAndLastName = v.findViewById(R.id.postNameAndLastName);
                                        postNameAndLastName.setText(postUser.getFirstName()+" "+postUser.getLastName());
                                        CircleImageView postProfilePic = v.findViewById(R.id.postProfilePic);
                                        postProfilePic.setRotation(90);
                                        Picasso.get().load("http://192.168.1.12:3000/"+user.getProfilePicture()).into(postProfilePic);
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {

                                    }
                                });
                                TextView postInformation = v.findViewById(R.id.postInformation);
                                postInformation.setText(post.getPosition());
                                TextView postContent = v.findViewById(R.id.postContent);
                                postContent.setText(post.getBody());


                            }
                        }

                        @Override
                        public void onFailure(Call<List<Post>> call, Throwable t) {

                        }
                    });
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        } else {
            user = UserSession.instance.getU();
            name.setText(user.getFirstName() + " " + user.getLastName() + " ");
            if(user.getProfilePicture()==null){
                Picasso.get().load("http://192.168.1.12:3000/uploads/dafault.jpg").into(profilePicture);
            }else{
                Picasso.get().load("http://192.168.1.12:3000/"+user.getProfilePicture()).into(profilePicture);
                profilePicture.setRotation(90);
            }
            //get visited countries
            Call<List<Country>> getCountries = userServices.getCountriesByUser(user.getId());
            getCountries.enqueue(new Callback<List<Country>>() {
                @Override
                public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                    List<Country> countries = response.body();
                    Log.e("count", String.valueOf(countries.size()));
                    countriesCount.setText(countries.size() + " countries visited !");
                    country1.setImageResource(World.getFlagOf(countries.get(0).getName()));
                    country2.setImageResource(World.getFlagOf(countries.get(1).getName()));
                    country3.setImageResource(World.getFlagOf(countries.get(2).getName()));
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
                    followersCount.setText("Followers\n" + followers.size());

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
                    followingCount.setText("Following\n" + followings.size());
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
                    //If User has no posts
                    if (posts.size() == 0) {
                        latestPost.removeAllViews();
                        TextView empty = new TextView(getContext());
                        empty.setText("This user has no posts");
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                        );
                        params.setMargins(20, 20, 20, 20);
                        params.gravity = Gravity.CENTER;
                        empty.setLayoutParams(params);
                        latestPost.addView(empty);
                        //If user has posts
                    }else{
                        Post post = posts.get(0);

                        userServices.getUser(user.getId()).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                postUser = response.body();
                                TextView postNameAndLastName = v.findViewById(R.id.postNameAndLastName);
                                postNameAndLastName.setText(postUser.getFirstName()+" "+postUser.getLastName());
                                CircleImageView postProfilePic = v.findViewById(R.id.postProfilePic);
                                postProfilePic.setRotation(90);
                                Picasso.get().load("http://192.168.1.12:3000/"+user.getProfilePicture()).into(postProfilePic);
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });
                        TextView postInformation = v.findViewById(R.id.postInformation);
                        postInformation.setText(post.getPosition());
                        TextView postContent = v.findViewById(R.id.postContent);
                        postContent.setText(post.getBody());


                    }
                }

                @Override
                public void onFailure(Call<List<Post>> call, Throwable t) {

                }
            });
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (UserSession.instance.getU().getEmail().equals(getArguments().getString("email"))) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer, new EditProfileFragment()).addToBackStack("prev").commit();
                    }
                }
            });

            profilePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (UserSession.instance.getU().getEmail().equals(getArguments().getString("email"))) {
                        BottomSheetDialog bottomSheetDialog = BottomSheetDialog.newInstance();
                        bottomSheetDialog.show(getActivity().getSupportFragmentManager(), "add_photo_dialog_fragment");
                    }

                }
            });
        }







//        disconnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                userStatesEdit=userStates.edit();
//                userStatesEdit.clear();
//                userStatesEdit.apply();
//                Intent intent = new Intent(getActivity(), FirstActivity.class);
//                startActivity(intent);
//                getActivity().finish()
//            }
//        });

        return v;
    }


}