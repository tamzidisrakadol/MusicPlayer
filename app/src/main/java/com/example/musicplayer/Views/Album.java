package com.example.musicplayer.Views;



import static com.example.musicplayer.Views.MainActivity.musicFiles;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayer.Adapter.AlbumListAdapter;
import com.example.musicplayer.Model.MusicFiles;
import com.example.musicplayer.databinding.FragmentAlbumBinding;

import java.util.ArrayList;
import java.util.List;


public class Album extends Fragment {
    FragmentAlbumBinding fragmentSongPlayBinding;
    AlbumListAdapter albumListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentSongPlayBinding = FragmentAlbumBinding.inflate(inflater,container,false);
        return fragmentSongPlayBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        albumListAdapter = new AlbumListAdapter(getContext(),musicFiles);
        fragmentSongPlayBinding.albumRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        fragmentSongPlayBinding.albumRecyclerView.setAdapter(albumListAdapter);
    }
}