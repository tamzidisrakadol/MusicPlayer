package com.example.musicplayer.Views;

import static com.example.musicplayer.Views.MusicList.musicFiles;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicplayer.Model.MusicFiles;
import com.example.musicplayer.R;
import com.example.musicplayer.databinding.ActivityMusicPlayBinding;

import java.util.ArrayList;
import java.util.Random;

public class MusicPlay extends AppCompatActivity {
    ActivityMusicPlayBinding activityMusicPlayBinding;
    ArrayList<MusicFiles> listSong = new ArrayList<>();
    int position = -1;
    Uri uri;
    MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playThread, nextThread, prevThread;
    static boolean shuffleBoolean = false, repeatBoolean = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMusicPlayBinding = ActivityMusicPlayBinding.inflate(getLayoutInflater());
        View view = activityMusicPlayBinding.getRoot();
        setContentView(view);
        getSupportActionBar().hide();
        getIntentMethod();
        activityMusicPlayBinding.songNameTV.setText(listSong.get(position).getTitle());
        activityMusicPlayBinding.songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
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
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
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
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    private void prevThreadBtn() {
        prevThread = new Thread() {
            @Override
            public void run() {
                super.run();
                activityMusicPlayBinding.prevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            if (shuffleBoolean && !repeatBoolean){
                                position = getRandom(listSong.size()-1);
                            }else if(!shuffleBoolean && !repeatBoolean){
                               // position = ((position + 1) % listSong.size());
                                position = ((position - 1) < 0 ? (listSong.size() - 1) : (position - 1));
                            }

                            uri = Uri.parse(listSong.get(position).getPath());
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                            metaData(uri);
                            activityMusicPlayBinding.songNameTV.setText(listSong.get(position).getTitle());
                            activityMusicPlayBinding.songSeekBar.setMax(mediaPlayer.getDuration() / 1000);
                            MusicPlay.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mediaPlayer != null) {
                                        int currentPos = mediaPlayer.getCurrentPosition() / 1000;
                                        activityMusicPlayBinding.songSeekBar.setProgress(currentPos);
                                    }
                                    handler.postDelayed(this, 1000);
                                }
                            });
                            activityMusicPlayBinding.playBtn.setImageResource(R.drawable.pause);
                            mediaPlayer.start();
                        } else {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            if (shuffleBoolean && !repeatBoolean){
                                position = getRandom(listSong.size()-1);
                            }else if(!shuffleBoolean && !repeatBoolean){
                                // position = ((position + 1) % listSong.size());
                                position = ((position - 1) < 0 ? (listSong.size() - 1) : (position - 1));
                            }
                            uri = Uri.parse(listSong.get(position).getPath());
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                            metaData(uri);
                            activityMusicPlayBinding.songNameTV.setText(listSong.get(position).getTitle());
                            activityMusicPlayBinding.songSeekBar.setMax(mediaPlayer.getDuration() / 1000);
                            MusicPlay.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mediaPlayer != null) {
                                        int currentPos = mediaPlayer.getCurrentPosition() / 1000;
                                        activityMusicPlayBinding.songSeekBar.setProgress(currentPos);
                                    }
                                    handler.postDelayed(this, 1000);
                                }
                            });
                            activityMusicPlayBinding.playBtn.setImageResource(R.drawable.play_white);
                        }
                    }
                });
            }
        };
        prevThread.start();
    }

    private void nextThreadBtn() {
        nextThread = new Thread() {
            @Override
            public void run() {
                super.run();
                activityMusicPlayBinding.nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            if (shuffleBoolean && !repeatBoolean){
                                position = getRandom(listSong.size()-1);
                            }else if(!shuffleBoolean && !repeatBoolean){
                                position = ((position + 1) % listSong.size());
                            }
                            uri = Uri.parse(listSong.get(position).getPath());
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                            metaData(uri);
                            activityMusicPlayBinding.songNameTV.setText(listSong.get(position).getTitle());
                            activityMusicPlayBinding.songSeekBar.setMax(mediaPlayer.getDuration() / 1000);
                            MusicPlay.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mediaPlayer != null) {
                                        int currentPos = mediaPlayer.getCurrentPosition() / 1000;
                                        activityMusicPlayBinding.songSeekBar.setProgress(currentPos);
                                    }
                                    handler.postDelayed(this, 1000);
                                }
                            });
                            activityMusicPlayBinding.playBtn.setImageResource(R.drawable.pause);
                            mediaPlayer.start();
                        } else {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            if (shuffleBoolean && !repeatBoolean){
                                position = getRandom(listSong.size()-1);
                            }else if(!shuffleBoolean && !repeatBoolean){
                                position = ((position + 1) % listSong.size());
                            }
                            uri = Uri.parse(listSong.get(position).getPath());
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                            metaData(uri);
                            activityMusicPlayBinding.songNameTV.setText(listSong.get(position).getTitle());
                            activityMusicPlayBinding.songSeekBar.setMax(mediaPlayer.getDuration() / 1000);
                            MusicPlay.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mediaPlayer != null) {
                                        int currentPos = mediaPlayer.getCurrentPosition() / 1000;
                                        activityMusicPlayBinding.songSeekBar.setProgress(currentPos);
                                    }
                                    handler.postDelayed(this, 1000);
                                }
                            });
                            activityMusicPlayBinding.playBtn.setImageResource(R.drawable.play_white);
                        }
                    }
                });
            }
        };
        nextThread.start();
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
                activityMusicPlayBinding.playBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mediaPlayer.isPlaying()) {
                            activityMusicPlayBinding.playBtn.setImageResource(R.drawable.play_white);
                            mediaPlayer.pause();
                            activityMusicPlayBinding.songSeekBar.setMax(mediaPlayer.getDuration() / 1000);
                            MusicPlay.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mediaPlayer != null) {
                                        int currentPos = mediaPlayer.getCurrentPosition() / 1000;
                                        activityMusicPlayBinding.songSeekBar.setProgress(currentPos);
                                    }
                                    handler.postDelayed(this, 1000);
                                }
                            });
                        } else {
                            activityMusicPlayBinding.playBtn.setImageResource(R.drawable.pause);
                            mediaPlayer.start();
                            activityMusicPlayBinding.songSeekBar.setMax(mediaPlayer.getDuration() / 1000);
                            MusicPlay.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mediaPlayer != null) {
                                        int currentPos = mediaPlayer.getCurrentPosition() / 1000;
                                        activityMusicPlayBinding.songSeekBar.setProgress(currentPos);
                                    }
                                    handler.postDelayed(this, 1000);
                                }
                            });
                        }
                    }
                });

            }
        };
        playThread.start();
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
        listSong = musicFiles;
        if (listSong != null) {
            activityMusicPlayBinding.playBtn.setImageResource(R.drawable.pause);
            uri = Uri.parse(listSong.get(position).getPath());
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();

        } else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        activityMusicPlayBinding.songSeekBar.setMax(mediaPlayer.getDuration() / 1000);
        metaData(uri);
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
}
