package com.example.musicplayer.Views;

import static com.example.musicplayer.Views.MainActivity.musicFiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.musicplayer.Adapter.AlbumDetailsAdapter;
import com.example.musicplayer.Adapter.OnClickItem;
import com.example.musicplayer.Model.MusicFiles;
import com.example.musicplayer.databinding.ActivityAlbumDetailsBinding;

import java.util.ArrayList;


public class AlbumDetails extends AppCompatActivity implements OnClickItem {
    ActivityAlbumDetailsBinding activityAlbumDetailsBinding;
    ArrayList<MusicFiles> albumFiles = new ArrayList<>();
    String albumName;
    AlbumDetailsAdapter albumDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAlbumDetailsBinding = ActivityAlbumDetailsBinding.inflate(getLayoutInflater());
        View  view = activityAlbumDetailsBinding.getRoot();
        setContentView(view);
        getSupportActionBar().hide();
        albumName = getIntent().getStringExtra("album");
        int j=0;
        for (int i=0;i<musicFiles.size();i++){
            if (albumName.equals(musicFiles.get(i).getAlbum())){
                albumFiles.add(j,musicFiles.get(i));
                j++;
            }
        }
        byte[] image = getAlbumArt(albumFiles.get(0).getPath());
        if (image !=null){
            Glide.with(this).load(image).into(activityAlbumDetailsBinding.albumDetailImg);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!(albumFiles.size()<1)){
            albumDetailsAdapter = new AlbumDetailsAdapter(this,albumFiles,this);
            activityAlbumDetailsBinding.albumDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
            activityAlbumDetailsBinding.albumDetailsRecyclerView.setAdapter(albumDetailsAdapter);
        }
    }

    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(uri.toString());
        byte[] art = mediaMetadataRetriever.getEmbeddedPicture();
        mediaMetadataRetriever.release();
        return art;
    }

    @Override
    public void onItemClick(int position) {

    }
}