package com.example.musicplayer.Views;

import static com.example.musicplayer.Views.MainActivity.PATH_TO_FRAG;
import static com.example.musicplayer.Views.MainActivity.SHOW_MINI_PLAYER;
import static com.example.musicplayer.Views.MainActivity.SONG_NAME;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.databinding.FragmentMiniNowPlayingBinding;

import java.io.IOException;


public class MiniNowPlaying extends Fragment {

    FragmentMiniNowPlayingBinding fragmentMiniNowPlayingBinding;


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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SHOW_MINI_PLAYER){
            if (PATH_TO_FRAG!=null){
                byte[] art = getAlbumArt(PATH_TO_FRAG);
                Glide.with(getContext()).load(art).into(fragmentMiniNowPlayingBinding.miniPlayerImage);
                fragmentMiniNowPlayingBinding.miniSongName.setText(PATH_TO_FRAG);
            }
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
}