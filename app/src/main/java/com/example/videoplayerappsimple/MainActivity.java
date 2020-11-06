package com.example.videoplayerappsimple;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuWrapperICS;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    private VideoView videoView;
    private Button play,musicPlay,pauseMusic;
    private SeekBar seekBar,musicController;

    private MediaController mediaController;  //video controller

    private AudioManager audioManager;   //for the volume and its a service

    private MediaPlayer mediaPlayer;  //for the music

    private Timer timer;  // its concept based on the thread

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.button);
        videoView = findViewById(R.id.videoView);
        musicPlay = findViewById(R.id.play);
        pauseMusic = findViewById(R.id.pause);
        seekBar = findViewById(R.id.seekBar);
        musicController = findViewById(R.id.musicController);

        play.setOnClickListener(MainActivity.this);
        musicPlay.setOnClickListener(MainActivity.this);
        pauseMusic.setOnClickListener(MainActivity.this);


        mediaController = new MediaController(MainActivity.this);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        seekBar.setMax(maxVolume);
        seekBar.setProgress(currentVolume);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser)
                {
//                    Toast.makeText(MainActivity.this, ""+progress  , Toast.LENGTH_SHORT).show();

                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
                }



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaPlayer = MediaPlayer.create(this,R.raw.music);

        musicController.setOnSeekBarChangeListener(MainActivity.this);
        musicController.setMax(mediaPlayer.getDuration());

        mediaPlayer.setOnCompletionListener(MainActivity.this);

    }


    //first make raw directory and paste a video in it
    @Override
    public void onClick(View btnViews) {

        switch (btnViews.getId())
        {
            case R.id.button:
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pointing_blue);

                videoView.setVideoURI(uri);

                videoView.setMediaController(mediaController);
                mediaController.setAnchorView(videoView);

                videoView.start();

                break;

            case R.id.play:

                mediaPlayer.start();

                //when we play the music button, at that time music seekbar will run.
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        musicController.setProgress(mediaPlayer.getCurrentPosition());
                    }
                },0,1000);


                break;

            case R.id.pause:

                mediaPlayer.pause();
                timer.cancel();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (fromUser)
        {
//                Toast.makeText(MainActivity.this,Integer.toString(progress),Toast.LENGTH_SHORT).show();
                    mediaPlayer.seekTo(progress);


        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        mediaPlayer.pause();

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        mediaPlayer.start();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        timer.cancel();
        Toast.makeText(this, "Music ended!", Toast.LENGTH_SHORT).show();
    }
}
