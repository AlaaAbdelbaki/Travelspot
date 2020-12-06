package com.travelspot.travelspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Toast;

import com.iammert.library.readablebottombar.ReadableBottomBar;
import com.travelspot.travelspot.Models.ServicesClient;
import com.travelspot.travelspot.Models.User;
import com.travelspot.travelspot.Models.UserServices;

import java.io.File;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ReadableBottomBar bottomBar;
    public static User userStat;
    UserServices userServices = ServicesClient.getClient().create(UserServices.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Hello good sir", Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,new HomeFragment()).commit();
        bottomBar = findViewById(R.id.bottomBar);
        Call<User> getUserDetails = userServices.getUser(getSharedPreferences("userStates", MODE_PRIVATE).getString("email",null));
        getUserDetails.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                UserSession.getInstance(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        bottomBar.setOnItemSelectListener(i -> {
            switch(i){
                case 0:
                    getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,new HomeFragment()).commit();
                    break;
                case 1:
                    getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,new FeedFragment()).commit();
                    break;
                case 2:
                    getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,new AddTripFragment()).commit();
                    break;
                case 3:
                    getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,new DiscoverFragment()).commit();
                    break;
                case 4:
                    Fragment profile = new ProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("email",UserSession.instance.getU().getEmail());
                    profile.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,profile).commit();
                    break;
            }
        });

    }



}