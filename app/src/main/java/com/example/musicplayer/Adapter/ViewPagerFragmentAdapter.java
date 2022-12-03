package com.example.musicplayer.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.musicplayer.Views.MusicList;
import com.example.musicplayer.Views.Album;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {
    private String[] titles ={"List","Song"};

    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new MusicList();
            case 1:
                return new Album();
        }
        return new MusicList();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
