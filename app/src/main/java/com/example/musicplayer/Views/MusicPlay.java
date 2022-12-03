package com.example.musicplayer.Views;

import static com.example.musicplayer.Views.MusicList.musicFiles;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;

import com.example.musicplayer.Model.MusicFiles;
import com.example.musicplayer.R;
import com.example.musicplayer.databinding.ActivityMusicPlayBinding;

import java.util.ArrayList;

public class MusicPlay extends AppCompatActivity {
    ActivityMusicPlayBinding activityMusicPlayBinding;
    ArrayList<MusicFiles> listSong = new ArrayList<>();
    int position =-1;
    Uri uri;
    MediaPlayer mediaPlayer;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMusicPlayBinding = ActivityMusicPlayBinding.inflate(getLayoutInflater());
        View view = activityMusicPlayBinding.getRoot();
        setContentView(view);
        getSupportActionBar().hide();
        getIntentMethod();
        activityMusicPlayBinding.songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        MusicPlay.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null){
                    int currentPosition = mediaPlayer.getCurrentPosition()/1000;
                    activityMusicPlayBinding.songSeekBar.setProgress(currentPosition);
                    activityMusicPlayBinding.durationTv.setText(formattedTime(currentPosition));
                }
                handler.postDelayed(this,1000);
            }
        });
    }

    private String formattedTime(int currentPosition) {
        String totalOut ="";
        String totalNew ="";
        String sec = String.valueOf(currentPosition%60);
        String min = String.valueOf(currentPosition/60);
        totalOut = min +":"+sec;
        totalNew = min+":"+"0"+sec;

        if (sec.length()==1){
            return totalNew;
        }else{
          return   totalOut;
        }
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position",-1);
        listSong = musicFiles;
        if (listSong != null){
            activityMusicPlayBinding.playBtn.setImageResource(R.drawable.pause);
            uri = Uri.parse(listSong.get(position).getPath());
        }
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }else{
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        activityMusicPlayBinding.songSeekBar.setMax(mediaPlayer.getDuration()/1000);
    }
}
