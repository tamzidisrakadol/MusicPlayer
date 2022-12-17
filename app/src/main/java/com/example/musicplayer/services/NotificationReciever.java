package com.example.musicplayer.services;

import static com.example.musicplayer.services.ApplicationClass.ACTION_NEXT;
import static com.example.musicplayer.services.ApplicationClass.ACTION_PLAY;
import static com.example.musicplayer.services.ApplicationClass.ACTION_PREVIOUS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String actionName = intent.getAction();
        Intent serviceIntent = new Intent(context,MusicService.class);
        if (actionName!=null){
            switch (actionName){
                case ACTION_PLAY:
                    Toast.makeText(context, "Pause", Toast.LENGTH_SHORT).show();
                    serviceIntent.putExtra("ActionName","playPause");
                    context.startService(serviceIntent);
                    break;
                case ACTION_NEXT:
                    Toast.makeText(context, "next", Toast.LENGTH_SHORT).show();
                    serviceIntent.putExtra("ActionName","next");
                    context.startService(serviceIntent);
                    break;
                case ACTION_PREVIOUS:
                    Toast.makeText(context, "prev", Toast.LENGTH_SHORT).show();
                    serviceIntent.putExtra("ActionName","prev");
                    context.startService(serviceIntent);
                    break;
            }
        }
    }
}
