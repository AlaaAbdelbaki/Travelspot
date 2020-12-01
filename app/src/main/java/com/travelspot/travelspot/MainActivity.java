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

import com.cloudinary.android.MediaManager;
import com.iammert.library.readablebottombar.ReadableBottomBar;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ReadableBottomBar bottomBar;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MediaManager.init(this);
        Toast.makeText(this, "Hello good sir", Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,new HomeFragment()).commit();
        bottomBar = findViewById(R.id.bottomBar);
        

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
                    bundle.putString("email",getSharedPreferences("userStates", MODE_PRIVATE).getString("email",null));
                    profile.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,profile).commit();
                    break;
            }
        });

    }



}