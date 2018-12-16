package com.example.tiget.musicplayer.ui.Library;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;


public class BackgroundService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    public static MediaPlayer mMediaPlayer;
    private final String mTAG = "AudioFocus";


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        context = getApplicationContext();
        //TODO

        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

// Request audio focus for playback
        int result = am.requestAudioFocus(focusChangeListener,
// Use the music stream.
                AudioManager.STREAM_MUSIC,
// Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

/*
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {


            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 1000, 1000);
        */
        final String SongUri = RecyclerViewAdapter.SongUri;//Получаем ссылку на песню
        mMediaPlayer = MediaPlayer.create(context, Uri.parse(SongUri));//Создаем MediaPlayer
        mMediaPlayer.setLooping(false);//Песня не будет повторяться
        mMediaPlayer.seekTo(0);//Песня влючится на 0й секунде
        mMediaPlayer.setVolume(0.3f, 0.7f);//Начальная громкость
        mMediaPlayer.start();//Запускаем


    }


    private AudioManager.OnAudioFocusChangeListener focusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    switch (focusChange) {

                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
                            // Lower the volume while ducking.
                            Log.v(mTAG, "can duck");
                            mMediaPlayer.start();
                            break;


                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):
                            Log.v(mTAG, "loss transient");
                            mMediaPlayer.pause();
                            break;


                        //Если что-то другое включилось
                        case (AudioManager.AUDIOFOCUS_LOSS):
                            Log.v(mTAG, "Loss");
                            ComponentName component = new ComponentName(BackgroundService.this, BackgroundService.class);
                            am.unregisterMediaButtonEventReceiver(component);
                            mMediaPlayer.pause();
                            break;

                        //Если аудиоканал свободен(он занят, даже если другая музыка стоит на паузе)
                        case (AudioManager.AUDIOFOCUS_GAIN):
                            // Return the volume to normal and resume if paused.
                            Log.v(mTAG, "gain");
                            mMediaPlayer.start();
                            break;


                    }
                }
            };


    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);//заканчивает процесс при выключении приложения(Можно убрать, тогда будет работать всегда)
    }

    @Override
    public void onStart(Intent intent, int startid) {

    }
}