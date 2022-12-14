package com.example.musicplayer.Adapter;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.Model.MusicFiles;
import com.example.musicplayer.databinding.AlbumDetailsItemBinding;

import java.io.IOException;
import java.util.List;

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.Viewholder>{
    Context context;
    public static List<MusicFiles> albumMusicFilesList;
    OnClickItem onClickItem;

    public AlbumDetailsAdapter(Context context, List<MusicFiles> musicFilesList,OnClickItem onClickItem) {
        this.context = context;
        this.albumMusicFilesList = musicFilesList;
        this.onClickItem = onClickItem;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        AlbumDetailsItemBinding albumDetailsItemBinding = AlbumDetailsItemBinding.inflate(layoutInflater,parent,false);
        return new Viewholder(albumDetailsItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        MusicFiles musicFiles = albumMusicFilesList.get(position);
        holder.albumDetailsItemBinding.albumListSongNameTV.setText(musicFiles.getTitle());
        byte[] img = getAlbumArt(musicFiles.getPath());
        if (img!=null){
            Glide.with(context).asBitmap().load(img).into(holder.albumDetailsItemBinding.albumListSongImg);
        }

    }

    @Override
    public int getItemCount() {
        return albumMusicFilesList.size();
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

    public class Viewholder extends RecyclerView.ViewHolder{
        AlbumDetailsItemBinding albumDetailsItemBinding;
        public Viewholder(AlbumDetailsItemBinding albumDetailsItemBinding) {
            super(albumDetailsItemBinding.getRoot());
            this.albumDetailsItemBinding = albumDetailsItemBinding;
            this.albumDetailsItemBinding.albumListSongNameTV.setOnClickListener(v -> {
                onClickItem.onItemClick(getAdapterPosition());
            });
        }
    }
}
