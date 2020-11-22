package com.travelspot.travelspot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.iammert.library.readablebottombar.ReadableBottomBar;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ReadableBottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Hello good sir", Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,new HomeFragment()).commit();

        bottomBar = findViewById(R.id.bottomBar);

        bottomBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {
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
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,new ProfileFragment()).commit();
                        break;
                }
            }
        });

    }
}