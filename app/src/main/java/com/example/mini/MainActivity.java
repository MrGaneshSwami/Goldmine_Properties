package com.example.mini;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout preloader;
    private VideoView preloaderVideo;
    private TextView textView;
    private Handler handler = new Handler();
    private int videoRepeatCount = 3;
    private int currentRepeatCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference to views
        preloader = findViewById(R.id.preloader);
        preloaderVideo = findViewById(R.id.preloaderVideo);
        textView = findViewById(R.id.textView);

        // Set up the video
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vid); // Replace with your video file
        preloaderVideo.setVideoURI(videoUri);

        // Start the video playback
        preloader.setVisibility(RelativeLayout.VISIBLE);
        preloaderVideo.start();

        // Listener for video completion
        preloaderVideo.setOnCompletionListener(mediaPlayer -> {
            currentRepeatCount++;
            if (currentRepeatCount < videoRepeatCount) {
                preloaderVideo.seekTo(0); // Restart video from the beginning
                preloaderVideo.start(); // Start video playback again
            } else {
                preloader.setVisibility(RelativeLayout.GONE);
                textView.setVisibility(TextView.VISIBLE); // Show main content after video ends
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preloaderVideo.isPlaying()) {
            preloaderVideo.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (preloaderVideo.isPlaying()) {
            preloaderVideo.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preloaderVideo.suspend();
        handler.removeCallbacksAndMessages(null); // Remove all callbacks to prevent memory leaks
    }
}
