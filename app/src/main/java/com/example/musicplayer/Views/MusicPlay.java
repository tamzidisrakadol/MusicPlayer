package com.example.musicplayer.Views;



import static com.example.musicplayer.Adapter.AlbumDetailsAdapter.albumMusicFilesList;
import static com.example.musicplayer.Views.MainActivity.musicFiles;
import static com.example.musicplayer.services.ApplicationClass.ACTION_NEXT;
import static com.example.musicplayer.services.ApplicationClass.ACTION_PLAY;
import static com.example.musicplayer.services.ApplicationClass.ACTION_PREVIOUS;
import static com.example.musicplayer.services.ApplicationClass.CHANNEL_ID_2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.palette.graphics.Palette;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicplayer.Adapter.ActionPlaying;
import com.example.musicplayer.Model.MusicFiles;
import com.example.musicplayer.R;
import com.example.musicplayer.databinding.ActivityMusicPlayBinding;
import com.example.musicplayer.services.MusicService;
import com.example.musicplayer.services.NotificationReciever;

import java.util.ArrayList;
import java.util.Random;

public class MusicPlay extends AppCompatActivity implements ActionPlaying, ServiceConnection{
    ActivityMusicPlayBinding activityMusicPlayBinding;
    public static ArrayList<MusicFiles> listSong = new ArrayList<>();
    int position = -1;
    Uri uri;
    private Handler handler = new Handler();
    private Thread playThread, nextThread, prevThread;
    static boolean shuffleBoolean = false, repeatBoolean = false;
    MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMusicPlayBinding = ActivityMusicPlayBinding.inflate(getLayoutInflater());
        View view = activityMusicPlayBinding.getRoot();
        setContentView(view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        getIntentMethod();

        activityMusicPlayBinding.songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (musicService != null && fromUser) {
                    musicService.seekTo(progress * 1000);
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
                if (musicService != null) {
                    int currentPosition = musicService.getCurrentPosition() / 1000;
                    activityMusicPlayBinding.songSeekBar.setProgress(currentPosition);
                    activityMusicPlayBinding.durationTv.setText(formattedTime(currentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });
        activityMusicPlayBinding.shuffleBtn.setOnClickListener(v -> {
            if (shuffleBoolean){
                shuffleBoolean = false;
                activityMusicPlayBinding.shuffleBtn.setImageResource(R.drawable.shuffle_white);
            }else{
                shuffleBoolean = true;
                activityMusicPlayBinding.shuffleBtn.setImageResource(R.drawable.shuffle_orange);
            }
        });
        activityMusicPlayBinding.repeatBtn.setOnClickListener(v -> {
            if (repeatBoolean){
                repeatBoolean = false;
                activityMusicPlayBinding.repeatBtn.setImageResource(R.drawable.repeat_white);
            }else{
                repeatBoolean = true;
                activityMusicPlayBinding.repeatBtn.setImageResource(R.drawable.repeat_orange);
            }
        });

    }

    @Override
    protected void onResume() {
        Intent intent  = new Intent(this,MusicService.class);
        bindService(intent,this,BIND_AUTO_CREATE);
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    private void prevThreadBtn() {
        prevThread = new Thread() {
            @Override
            public void run() {
                super.run();
                btn_prev();
            }
        };
        prevThread.start();
    }
    public void btn_prev(){
        activityMusicPlayBinding.prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicService.isPlaying()) {
                    musicService.stop();
                    musicService.release();
                    if (shuffleBoolean && !repeatBoolean){
                        position = getRandom(listSong.size()-1);
                    }else if(!shuffleBoolean && !repeatBoolean){
                        // position = ((position + 1) % listSong.size());
                        position = ((position - 1) < 0 ? (listSong.size() - 1) : (position - 1));
                    }

                    uri = Uri.parse(listSong.get(position).getPath());
                    musicService.createMediaPlayer(position);
                    metaData(uri);
                    activityMusicPlayBinding.songNameTV.setText(listSong.get(position).getTitle());
                    activityMusicPlayBinding.songSeekBar.setMax(musicService.getDuration() / 1000);
                    MusicPlay.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (musicService != null) {
                                int currentPos = musicService.getCurrentPosition() / 1000;
                                activityMusicPlayBinding.songSeekBar.setProgress(currentPos);
                            }
                            handler.postDelayed(this, 1000);
                        }
                    });
                    musicService.onCompleted();
                    activityMusicPlayBinding.playBtn.setImageResource(R.drawable.pause);
                    musicService.onStart();
                } else {
                    musicService.stop();
                    musicService.release();
                    if (shuffleBoolean && !repeatBoolean){
                        position = getRandom(listSong.size()-1);
                    }else if(!shuffleBoolean && !repeatBoolean){
                        // position = ((position + 1) % listSong.size());
                        position = ((position - 1) < 0 ? (listSong.size() - 1) : (position - 1));
                    }
                    uri = Uri.parse(listSong.get(position).getPath());
                    musicService.createMediaPlayer(position);
                    metaData(uri);
                    activityMusicPlayBinding.songNameTV.setText(listSong.get(position).getTitle());
                    activityMusicPlayBinding.songSeekBar.setMax(musicService.getDuration() / 1000);
                    MusicPlay.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (musicService != null) {
                                int currentPos = musicService.getCurrentPosition() / 1000;
                                activityMusicPlayBinding.songSeekBar.setProgress(currentPos);
                            }
                            handler.postDelayed(this, 1000);
                        }
                    });
                    musicService.onCompleted();
                    activityMusicPlayBinding.playBtn.setImageResource(R.drawable.play_white);
                }
            }
        });
    }

    private void nextThreadBtn() {
        nextThread = new Thread() {
            @Override
            public void run() {
                super.run();
               btn_next();
            }
        };
        nextThread.start();
    }
    public void btn_next(){
        activityMusicPlayBinding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicService.isPlaying()) {
                    musicService.stop();
                    musicService.release();
                    if (shuffleBoolean && !repeatBoolean){
                        position = getRandom(listSong.size()-1);
                    }else if(!shuffleBoolean && !repeatBoolean){
                        position = ((position + 1) % listSong.size());
                    }
                    uri = Uri.parse(listSong.get(position).getPath());
                    musicService.createMediaPlayer(position);
                    metaData(uri);
                    activityMusicPlayBinding.songNameTV.setText(listSong.get(position).getTitle());
                    activityMusicPlayBinding.songSeekBar.setMax(musicService.getDuration() / 1000);
                    MusicPlay.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (musicService != null) {
                                int currentPos = musicService.getCurrentPosition() / 1000;
                                activityMusicPlayBinding.songSeekBar.setProgress(currentPos);
                            }
                            handler.postDelayed(this, 1000);
                        }
                    });
                    musicService.onCompleted();
                    activityMusicPlayBinding.playBtn.setImageResource(R.drawable.pause);
                    musicService.onStart();
                } else {
                    musicService.stop();
                    musicService.release();
                    if (shuffleBoolean && !repeatBoolean){
                        position = getRandom(listSong.size()-1);
                    }else if(!shuffleBoolean && !repeatBoolean){
                        position = ((position + 1) % listSong.size());
                    }
                    uri = Uri.parse(listSong.get(position).getPath());
                    musicService.createMediaPlayer(position);
                    metaData(uri);
                    activityMusicPlayBinding.songNameTV.setText(listSong.get(position).getTitle());
                    activityMusicPlayBinding.songSeekBar.setMax(musicService.getDuration() / 1000);
                    MusicPlay.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (musicService != null) {
                                int currentPos = musicService.getCurrentPosition() / 1000;
                                activityMusicPlayBinding.songSeekBar.setProgress(currentPos);
                            }
                            handler.postDelayed(this, 1000);
                        }
                    });
                    musicService.onCompleted();
                    activityMusicPlayBinding.playBtn.setImageResource(R.drawable.play_white);
                }
            }
        });
    }


    private int getRandom(int i) {
        Random  random = new Random();
        return random.nextInt(i+1);
    }


    private void playThreadBtn() {
        playThread = new Thread() {
            @Override
            public void run() {
                super.run();
                btn_play();

            }
        };
        playThread.start();
    }
    public void btn_play(){
        activityMusicPlayBinding.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicService.isPlaying()) {
                    activityMusicPlayBinding.playBtn.setImageResource(R.drawable.play_white);
                   musicService.onPause();
                    activityMusicPlayBinding.songSeekBar.setMax(musicService.getDuration() / 1000);
                    MusicPlay.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (musicService != null) {
                                int currentPos = musicService.getCurrentPosition() / 1000;
                                activityMusicPlayBinding.songSeekBar.setProgress(currentPos);
                            }
                            handler.postDelayed(this, 1000);
                        }
                    });
                } else {
                    activityMusicPlayBinding.playBtn.setImageResource(R.drawable.pause);
                    musicService.onStart();
                    activityMusicPlayBinding.songSeekBar.setMax(musicService.getDuration() / 1000);
                    MusicPlay.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (musicService != null) {
                                int currentPos = musicService.getCurrentPosition() / 1000;
                                activityMusicPlayBinding.songSeekBar.setProgress(currentPos);
                            }
                            handler.postDelayed(this, 1000);
                        }
                    });
                }
            }
        });
    }

    private String formattedTime(int currentPosition) {
        String totalOut = "";
        String totalNew = "";
        String sec = String.valueOf(currentPosition % 60);
        String min = String.valueOf(currentPosition / 60);
        totalOut = min + ":" + sec;
        totalNew = min + ":" + "0" + sec;

        if (sec.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position", -1);
        String sender = getIntent().getStringExtra("sender");
        if (sender!=null && sender.equals("albumDetails")){
            listSong = (ArrayList<MusicFiles>) albumMusicFilesList;
        }else{
            listSong = musicFiles;
        }
        if (listSong != null) {
            activityMusicPlayBinding.playBtn.setImageResource(R.drawable.pause);
            uri = Uri.parse(listSong.get(position).getPath());
        }
        Intent intent = new Intent(this,MusicService.class);
        intent.putExtra("servicePosition",position);
        startService(intent);

    }

    private void metaData(Uri uri) {
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(listSong.get(position).getDuration()) / 1000;
        activityMusicPlayBinding.songDurationTv.setText(formattedTime(durationTotal));
        byte[] art = metadataRetriever.getEmbeddedPicture();
        Bitmap bitmap;
        if (art != null) {
            Glide.with(this).asBitmap().load(art).into(activityMusicPlayBinding.circleImgView);
            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch swatch = palette.getDominantSwatch();
                    if (swatch != null) {
                        activityMusicPlayBinding.circleImgView.setBackgroundResource(R.drawable.gradient);
                        activityMusicPlayBinding.constraintLayout.setBackgroundResource(R.drawable.container_bg);
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(), 0x00000000});
                        activityMusicPlayBinding.circleImgView.setBackground(gradientDrawable);
                        GradientDrawable gradientDrawable2 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(), swatch.getRgb()});
                        activityMusicPlayBinding.constraintLayout.setBackground(gradientDrawable2);
                    } else {
                        activityMusicPlayBinding.circleImgView.setBackgroundResource(R.drawable.gradient);
                        activityMusicPlayBinding.constraintLayout.setBackgroundResource(R.drawable.container_bg);
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000, 0x00000000});
                        activityMusicPlayBinding.circleImgView.setBackground(gradientDrawable);
                        GradientDrawable gradientDrawable2 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000, 0xff000000});
                        activityMusicPlayBinding.constraintLayout.setBackground(gradientDrawable2);
                    }
                }
            });
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
        musicService = myBinder.getService();
        Toast.makeText(this, "Connected"+musicService, Toast.LENGTH_SHORT).show();
        activityMusicPlayBinding.songSeekBar.setMax(musicService.getDuration() / 1000);
        metaData(uri);
        activityMusicPlayBinding.songNameTV.setText(listSong.get(position).getTitle());
        activityMusicPlayBinding.songNameTV.setSelected(true);
        activityMusicPlayBinding.backImgBtn.setOnClickListener(v -> onBackPressed());
        musicService.onCompleted();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;

    }
    void showNotification(int playPause){
        Intent intent = new Intent(this,MusicPlay.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,intent,0);
        Intent prevIntent = new Intent(this, NotificationReciever.class).setAction(ACTION_PREVIOUS);
        PendingIntent prevPending = PendingIntent.getBroadcast(this,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Intent pauseIntent = new Intent(this, NotificationReciever.class).setAction(ACTION_PLAY);
        PendingIntent pausePending = PendingIntent.getBroadcast(this,0,pauseIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Intent nextIntent = new Intent(this, NotificationReciever.class).setAction(ACTION_NEXT);
        PendingIntent nextPending = PendingIntent.getBroadcast(this,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        byte[] picture = null;
        picture = getAlbumArt(musicFiles.get(position).getPath());
        Bitmap thumb = null;
        if (picture != null){
            thumb = BitmapFactory.decodeByteArray(picture,0,picture.length);
        }
//        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID_2)
//                .setSmallIcon(playPause)
//                .setLargeIcon(thumb)
//                .setContentTitle(musicFiles.get(position).getTitle())
//                .addAction(R.drawable.prev,"previous",prevPending)
//                .addAction(playPause,"play",prevPending)
//                .addAction(R.drawable.next,"next",nextPending)
//                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle())
//                .

    }
    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(uri.toString());
        byte[] art = mediaMetadataRetriever.getEmbeddedPicture();
        mediaMetadataRetriever.release();
        return art;
    }


}
