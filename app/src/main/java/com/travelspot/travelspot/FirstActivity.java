package com.travelspot.travelspot;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.common.SignInButton;

import java.util.concurrent.TimeUnit;

public class FirstActivity extends AppCompatActivity {

    VideoView videoBg;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;
    SignInButton googleSingIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            TimeUnit.MILLISECONDS.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        googleSingIn = findViewById(R.id.signIn);
        TextView textview = (TextView) googleSingIn.getChildAt(0);
        textview.setText("Sign in with Google");

        playVideo();


        videoBg.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
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
    /*================================ Important Section! ================================
    We must override onPause(), onResume(), and onDestroy() to properly handle our
    VideoView.
     */

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