package com.example.tiget.musicplayer;


import android.app.Service;
import android.content.*;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.*;


public class BackgroundService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onCreate() {
        context = getApplicationContext();
        //TODO

        final String SongUri = RecyclerViewAdapter.SongUri;

        RecyclerViewAdapter.mMediaPlayer = MediaPlayer.create(context, Uri.parse("http://blogstop.city/srhj67rbsexual/ayo/ayo%20and%20teo%20-%20better%20off%20alone.mp3"));
        RecyclerViewAdapter.mMediaPlayer.setLooping(false);
        RecyclerViewAdapter.mMediaPlayer.seekTo(0);
        RecyclerViewAdapter.mMediaPlayer.setVolume(0.3f, 0.7f);
        RecyclerViewAdapter.mMediaPlayer.start();


    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onStart(Intent intent, int startid) {

    }


}