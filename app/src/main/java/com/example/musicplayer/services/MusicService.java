package com.example.musicplayer.services;

import static com.example.musicplayer.Views.MusicPlay.listSong;
import static com.example.musicplayer.services.ApplicationClass.ACTION_NEXT;
import static com.example.musicplayer.services.ApplicationClass.ACTION_PLAY;
import static com.example.musicplayer.services.ApplicationClass.ACTION_PREVIOUS;
import static com.example.musicplayer.services.ApplicationClass.CHANNEL_ID_2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.musicplayer.Adapter.ActionPlaying;
import com.example.musicplayer.Model.MusicFiles;
import com.example.musicplayer.R;
import com.example.musicplayer.Views.MusicPlay;

import java.io.IOException;
import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {
    IBinder mBinder = new MyBinder();
    MediaPlayer mediaPlayer;
    ArrayList<MusicFiles> musicFiles = new ArrayList<>();
    Uri uri;
    int position = -1;
    ActionPlaying actionPlaying;
    MediaSessionCompat mediaSessionCompat;
    public static final String MUSIC_FILE_LAST_PLAYED ="Last Played";
    public static final String MUSIC_FILE = "Stored Music";
    public static final String SONG_NAME = "Song Name";


    @Override
    public void onCreate() {
        super.onCreate();
        mediaSessionCompat = new MediaSessionCompat(getBaseContext(),"My Audio");
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


   public void createMediaPlayer(int positionInner){
        position = positionInner;
        uri = Uri.parse(musicFiles.get(position).getPath());

        SharedPreferences.Editor editor = getSharedPreferences(MUSIC_FILE_LAST_PLAYED,MODE_PRIVATE).edit();
        editor.putString(MUSIC_FILE,uri.toString());
        editor.apply();

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
            if(mediaPlayer !=null){
                createMediaPlayer(position);
                mediaPlayer.start();
                onCompleted();
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
   public void showNotification(int playPause){
        Intent intent = new Intent(this, MusicPlay.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent contentIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT );
        }
        Intent prevIntent = new Intent(this, NotificationReciever.class).setAction(ACTION_PREVIOUS);
        PendingIntent prevPending = PendingIntent.getBroadcast(this,0,prevIntent, PendingIntent.FLAG_IMMUTABLE);
        Intent pauseIntent = new Intent(this, NotificationReciever.class).setAction(ACTION_PLAY);
        PendingIntent pausePending = PendingIntent.getBroadcast(this,0,pauseIntent,PendingIntent.FLAG_IMMUTABLE);
        Intent nextIntent = new Intent(this, NotificationReciever.class).setAction(ACTION_NEXT);
        PendingIntent nextPending = PendingIntent.getBroadcast(this,0,nextIntent,PendingIntent.FLAG_IMMUTABLE);


        byte[] picture = null;
        picture = getAlbumArt(listSong.get(position).getPath());
        Bitmap thumb = null;
        if (picture != null){
            thumb = BitmapFactory.decodeByteArray(picture,0,picture.length);
        }
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID_2)
                .setSmallIcon(playPause)
                .setLargeIcon(thumb)
                .setContentTitle(musicFiles.get(position).getTitle())
                .addAction(R.drawable.prev,"previous",prevPending)
                .addAction(playPause,"play",pausePending)
                .addAction(R.drawable.next,"next",nextPending)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSessionCompat.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();
                startForeground(2,notification);

    }
    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(uri.toString());
        byte[] art = mediaMetadataRetriever.getEmbeddedPicture();
        try {
            mediaMetadataRetriever.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return art;
    }

    public void setCallback(ActionPlaying actionPlaying){
        this.actionPlaying = actionPlaying;
    }

}
