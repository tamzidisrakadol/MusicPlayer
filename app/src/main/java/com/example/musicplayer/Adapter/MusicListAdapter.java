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
import com.example.musicplayer.databinding.ListItemBinding;

import java.io.IOException;
import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder>{
    Context context;
    List<MusicFiles> mFileList;
    OnClickItem onClickItem;


    public MusicListAdapter(Context context, List<MusicFiles> musicFilesList,OnClickItem onClickItem) {
        this.context = context;
        this.mFileList = musicFilesList;
        this.onClickItem = onClickItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemBinding listItemBinding = ListItemBinding.inflate(layoutInflater,parent,false);
        return new ViewHolder(listItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MusicFiles musicFiles = mFileList.get(position);
        holder.listItemBinding.listSongNameTV.setText(musicFiles.getTitle());
        byte[] img = getAlbumArt(musicFiles.getPath());
        if (img!=null){
            Glide.with(context).asBitmap().load(img).into(holder.listItemBinding.listSongImg);
        }else{
       //     Glide.with(context).load(R.drawable.ic_launcher_foreground).into(holder.listItemBinding.listSongImg);
        }
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ListItemBinding listItemBinding;
        public ViewHolder(ListItemBinding listItemBinding) {
            super(listItemBinding.getRoot());
            this.listItemBinding =listItemBinding;
            this.listItemBinding.listSongNameTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItem.onItemClick(getAdapterPosition());
                }
            });
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
