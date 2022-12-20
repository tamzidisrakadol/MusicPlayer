package com.example.musicplayer.Views;

import static com.example.musicplayer.Views.MainActivity.musicFiles;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.musicplayer.Adapter.MusicListAdapter;
import com.example.musicplayer.Adapter.OnClickItem;
import com.example.musicplayer.Model.MusicFiles;
import com.example.musicplayer.R;
import com.example.musicplayer.databinding.FragmentMusicListBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;


public class MusicList extends Fragment implements OnClickItem {
    FragmentMusicListBinding fragmentMusicListBinding;
    MusicListAdapter musicListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentMusicListBinding = FragmentMusicListBinding.inflate(inflater,container,false);
        return fragmentMusicListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        musicListAdapter = new MusicListAdapter(getContext(),musicFiles,this);
        fragmentMusicListBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        fragmentMusicListBinding.recyclerView.setAdapter(musicListAdapter);
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(),MusicPlay.class);
        intent.putExtra("position",position);
        startActivity(intent);

    }
}