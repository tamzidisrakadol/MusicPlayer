package com.example.musicplayer.Views;

import static android.content.Context.MODE_PRIVATE;
import static com.example.musicplayer.Views.MainActivity.PATH_TO_FRAG;
import static com.example.musicplayer.Views.MainActivity.SHOW_MINI_PLAYER;
import static com.example.musicplayer.Views.MainActivity.SONG_NAME;
import static com.example.musicplayer.Views.MainActivity.SONG_NAME_TO_FRAG;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.databinding.FragmentMiniNowPlayingBinding;
import com.example.musicplayer.services.MusicService;

import java.io.IOException;


public class MiniNowPlaying extends Fragment implements ServiceConnection {

    FragmentMiniNowPlayingBinding fragmentMiniNowPlayingBinding;
    MusicService musicService;
    public static final String MUSIC_FILE_LAST_PLAYED ="Last Played";
    public static final String MUSIC_FILE = "Stored Music";
    public static final String SONG_NAME = "Song Name";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentMiniNowPlayingBinding = FragmentMiniNowPlayingBinding.inflate(inflater,container,false);
        return fragmentMiniNowPlayingBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentMiniNowPlayingBinding.miniNextButton.setOnClickListener(view1 -> {
            if (musicService!=null){
                musicService.nextBtnClicking();

                if (getActivity() !=null){
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(MUSIC_FILE_LAST_PLAYED,MODE_PRIVATE).edit();
                    editor.putString(MUSIC_FILE, MusicService.musicFiles.get(musicService.getCurrentPosition()).getPath());
                    editor.putString(SONG_NAME, MusicService.musicFiles.get(musicService.getCurrentPosition()).getTitle());
                    editor.apply();
                    SharedPreferences preferences =getActivity().getSharedPreferences(MUSIC_FILE_LAST_PLAYED,MODE_PRIVATE);
                    String value = preferences.getString(MUSIC_FILE,null);
                    String songName = preferences.getString(SONG_NAME,null);
                    if (value!=null){
                        SHOW_MINI_PLAYER = true;
                        PATH_TO_FRAG = value;
                        SONG_NAME_TO_FRAG= songName;
                    }else{
                        SHOW_MINI_PLAYER = false;
                        PATH_TO_FRAG=null;
                        SONG_NAME_TO_FRAG= null;
                    }
                    if (SHOW_MINI_PLAYER) {
                        if (PATH_TO_FRAG!=null){
                            byte[] art = getAlbumArt(PATH_TO_FRAG);
                            Glide.with(getContext()).load(art).into(fragmentMiniNowPlayingBinding.miniPlayerImage);
                            fragmentMiniNowPlayingBinding.miniSongName.setText(SONG_NAME_TO_FRAG);
                        }
                    }
                }
            }
        });

        fragmentMiniNowPlayingBinding.miniPlayButton.setOnClickListener(view12 -> {
            if (musicService!=null){
                musicService.playPauseBtnClicking();
                if (musicService.isPlaying()){
                    fragmentMiniNowPlayingBinding.miniPlayButton.setImageResource(R.drawable.mini_pause);
                }else{
                    fragmentMiniNowPlayingBinding.miniPlayButton.setImageResource(R.drawable.mini_play);

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SHOW_MINI_PLAYER){
            if (PATH_TO_FRAG!=null){
                byte[] art = getAlbumArt(PATH_TO_FRAG);
                Glide.with(getContext()).load(art).into(fragmentMiniNowPlayingBinding.miniPlayerImage);
                fragmentMiniNowPlayingBinding.miniSongName.setText(SONG_NAME_TO_FRAG);
                Intent intent = new Intent(getContext(),MusicService.class);
                if (getContext()!=null){
                    getContext().bindService(intent, this, Context.BIND_AUTO_CREATE);

                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getContext() != null){
            getContext().unbindService(this);
        }
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

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MyBinder binder = (MusicService.MyBinder) iBinder;
        musicService = binder.getService();

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService = null;
    }
}