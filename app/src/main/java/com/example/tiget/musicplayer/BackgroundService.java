package com.example.tiget.musicplayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

public class BackgroundService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    public static MediaPlayer mMediaPlayer;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onCreate() {
        context = getApplicationContext();
        //TODO


        final String SongUri = RecyclerViewAdapter.SongUri;//Получаем ссылку на песню

        mMediaPlayer = null;
        mMediaPlayer = MediaPlayer.create(context, Uri.parse(SongUri));//Создаем MediaPlayer
        mMediaPlayer.setLooping(false);//Песня не будет повторяться
        mMediaPlayer.seekTo(0);//Песня влючится на 0й секунде
        mMediaPlayer.setVolume(0.3f, 0.7f);//Начальная громкость
        mMediaPlayer.start();//Запускаем



    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        handler.removeCallbacks(runnable);//заканчивает процесс при выключении приложения(Можно убрать, тогда будет работать всегда)
    }

    @Override
    public void onStart(Intent intent, int startid) {

    }


}