package com.example.musicplayer.services;

import static com.example.musicplayer.Views.MusicPlay.listSong;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Switch;

import androidx.annotation.Nullable;

import com.example.musicplayer.Adapter.ActionPlaying;
import com.example.musicplayer.Model.MusicFiles;

import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {
    IBinder mBinder = new MyBinder();
    MediaPlayer mediaPlayer;
    ArrayList<MusicFiles> musicFiles = new ArrayList<>();
    Uri uri;
    int position = -1;
    ActionPlaying actionPlaying;


    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }



    public class MyBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int myPos = intent.getIntExtra("servicePosition",-1);
        String actionName = intent.getStringExtra("ActionName");
        if (myPos != -1){
            playMedia(myPos);
        }
        if (actionName!=null){
            switch (actionName){
                case "playPause":
                    if (actionName!=null){
                        Log.d("playPause","PLAY & PAUSE");
                        actionPlaying.btn_play();
                    }
                    break;

                case "next":
                    if (actionName!=null){
                        actionPlaying.btn_next();
                    }
                    break;

                case "previous":
                    if (actionName!=null){
                        actionPlaying.btn_prev();
                    }
                    break;
            }
        }

        return START_STICKY;
    }

    private void playMedia(int startPosition) {
        musicFiles = listSong;
        position = startPosition;
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            if (musicFiles != null){
                createMediaPlayer(position);
                mediaPlayer.start();
            }
        }else{
            createMediaPlayer(position);
            mediaPlayer.start();
        }
    }

    public void onStart(){
        mediaPlayer.start();
    }
    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }
    public void stop(){
        mediaPlayer.stop();
    }
    public void release(){
        mediaPlayer.release();
    }
   public int getDuration(){
       return mediaPlayer.getDuration();
    }

   public void seekTo(int position){
        mediaPlayer.seekTo(position);
    }
   public void createMediaPlayer(int position){
        uri = Uri.parse(musicFiles.get(position).getPath());
        mediaPlayer = MediaPlayer.create(getBaseContext(),uri);
    }
    public int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    public void onPause(){
        mediaPlayer.pause();
    }

    public void onCompleted(){
        mediaPlayer.setOnCompletionListener(this);

    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        if (actionPlaying !=null){
        actionPlaying.btn_next();
        }

        createMediaPlayer(position);
        mediaPlayer.start();
        onCompleted();
    }
    public void setCallback(ActionPlaying actionPlaying){
        this.actionPlaying = actionPlaying;
    }

}
