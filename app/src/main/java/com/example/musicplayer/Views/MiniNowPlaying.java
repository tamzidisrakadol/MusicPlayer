package com.example.musicplayer.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayer.R;
import com.example.musicplayer.databinding.FragmentMiniNowPlayingBinding;


public class MiniNowPlaying extends Fragment {

    FragmentMiniNowPlayingBinding fragmentMiniNowPlayingBinding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentMiniNowPlayingBinding = FragmentMiniNowPlayingBinding.inflate(inflater,container,false);
        return fragmentMiniNowPlayingBinding.getRoot();
    }
}