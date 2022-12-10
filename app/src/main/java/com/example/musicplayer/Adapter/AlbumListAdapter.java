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
import com.example.musicplayer.databinding.AlbumItemLayoutBinding;

import java.util.List;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.ViewHolder> {
    Context context;
    List<MusicFiles> musicFilesList2;

    public AlbumListAdapter(Context context, List<MusicFiles> musicFilesList) {
        this.context = context;
        this.musicFilesList2 = musicFilesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        AlbumItemLayoutBinding albumItemLayoutBinding = AlbumItemLayoutBinding.inflate(layoutInflater,parent,false);
        return new ViewHolder(albumItemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MusicFiles  musicFiles = musicFilesList2.get(position);
        holder.albumItemLayoutBinding.albumNameTV.setText(musicFiles.getAlbum());
        byte[] img = getAlbumArt(musicFiles.getPath());
        if (img!=null){
            Glide.with(context).asBitmap().load(img).into(holder.albumItemLayoutBinding.albumImg    );
        }else{
            //     Glide.with(context).load(R.drawable.ic_launcher_foreground).into(holder.listItemBinding.listSongImg);
        }

    }

    @Override
    public int getItemCount() {
        return musicFilesList2.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        AlbumItemLayoutBinding albumItemLayoutBinding;
        public ViewHolder(AlbumItemLayoutBinding albumItemLayoutBinding) {
            super(albumItemLayoutBinding.getRoot());
            this.albumItemLayoutBinding = albumItemLayoutBinding;
        }
    }

    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(uri.toString());
        byte[] art = mediaMetadataRetriever.getEmbeddedPicture();
        mediaMetadataRetriever.release();
        return art;
    }

}
