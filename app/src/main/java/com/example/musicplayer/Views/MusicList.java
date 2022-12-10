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
  //  static ArrayList<MusicFiles> musicFiles;
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
     //   musicFiles = new ArrayList<>();
       // runTimePermission();
        musicListAdapter = new MusicListAdapter(getContext(),musicFiles,this);
        fragmentMusicListBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        fragmentMusicListBinding.recyclerView.setAdapter(musicListAdapter);
//        if (!(musicFiles.size()<1)){
//
//        }
    }

//    private void runTimePermission() {
//        //to check permission
//        Dexter.withContext(getContext())
//                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//
//                        //read all audio files from phone
//                        musicFiles = getAllAudio(getContext());
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                        permissionToken.continuePermissionRequest();
//                    }
//                }).check();
//    }
//
//    public static ArrayList<MusicFiles> getAllAudio(Context context){
//        ArrayList<MusicFiles> tempAudioFile = new ArrayList<>();
//        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        String[] projection= {
//                MediaStore.Audio.Media.ALBUM,
//                MediaStore.Audio.Media.TITLE,
//                MediaStore.Audio.Media.DURATION,
//                MediaStore.Audio.Media.DATA,
//                MediaStore.Audio.Media.ARTIST,
//        };
//        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);
//        if (cursor!=null){
//            while (cursor.moveToNext()){
//                String album = cursor.getString(0);
//                String title= cursor.getString(1);
//                String duration = cursor.getString(2);
//                String path = cursor.getString(3);
//                String artist = cursor.getString(4);
//
//                MusicFiles musicFiles = new MusicFiles(path,title,artist,album,duration);
//                Log.e("Path : "+ path,"Album :"+ album );
//                tempAudioFile.add(musicFiles);
//            }
//            cursor.close();
//        }
//        return tempAudioFile;
//    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(),MusicPlay.class);
        intent.putExtra("position",position);
        startActivity(intent);

    }
}