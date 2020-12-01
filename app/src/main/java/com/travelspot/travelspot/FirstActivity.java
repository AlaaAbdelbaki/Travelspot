package com.travelspot.travelspot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;

import java.util.concurrent.TimeUnit;

public class FirstActivity extends AppCompatActivity {

    VideoView videoBg;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;
    GoogleSignInClient mGoogleSignInClient;
    SharedPreferences userStates;
    SharedPreferences.Editor userStatesEdit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);


        mMediaPlayer = new MediaPlayer();
        userStates = getApplicationContext().getSharedPreferences("userStates", MODE_PRIVATE);
        if(userStates.getBoolean("Remembered", false))
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }


            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            //For transparent status bar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window w = getWindow();
                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }

            //For 500 ms splash screen
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            setTheme(R.style.AppTheme);


            //Load first fragment
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                    R.anim.fade_out).add(R.id.fragment_container, new FirstFragment()).commit();


            //play background video
            playVideo();

            //loop background video
            videoBg.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mMediaPlayer = mediaPlayer;
                    // We want our video to play over and over so we set looping to true.
                    mMediaPlayer.setLooping(true);
                    // We then seek to the current posistion if it has been set and play the video.
                    if (mCurrentVideoPosition != 0) {
                        mMediaPlayer.seekTo(mCurrentVideoPosition);
                        mMediaPlayer.start();
                    }
                }
            });
    }

    void playVideo(){
        videoBg = findViewById(R.id.bgVideo);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.bgvid);
        videoBg.setVideoURI(uri);
        videoBg.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Capture the current video position and pause the video.
        mCurrentVideoPosition = mMediaPlayer.getCurrentPosition();
        videoBg.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restart the video when resuming the Activity
        videoBg.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // When the Activity is destroyed, release our MediaPlayer and set it to null.
        mMediaPlayer.release();
        mMediaPlayer = null;
    }
}