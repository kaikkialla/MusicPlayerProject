package com.example.tiget.musicplayer.ui.UserLibrary;

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

import com.example.tiget.musicplayer.R;


public class BackgroundService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    public static MediaPlayer mMediaPlayer;
    private final String AUDIO_FOCUS_TAG = "BSJOOHSVMQHNFUV";

    public static final String ACTION_PLAY  = "ACTION_PLAY";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static Boolean MediaPlayerState;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        context = getApplicationContext();
        //TODO

        requestFocus();

        final String SongUri = RecyclerViewAdapter.SongUri;//Получаем ссылку на песню
        mMediaPlayer = MediaPlayer.create(context, Uri.parse(SongUri));//Создаем MediaPlayer
        mMediaPlayer.setLooping(false);//Песня не будет повторяться
        mMediaPlayer.seekTo(0);//Песня влючится на 0й секунде
        mMediaPlayer.setVolume(0.3f, 0.7f);//Начальная громкость
        mMediaPlayer.start();//Запускаем


    }

    public void onDestroy() {
        handler.removeCallbacks(runnable);//заканчивает процесс при выключении приложения(Можно убрать, тогда будет работать всегда)
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null) {
            if (intent.getAction().equals(ACTION_PLAY)) {
                // фокус нужно запрашивать ПЕРЕД КАЖДЫМ PLAY
                // поэтому у тебя был баг, ты же только один раз запрашивал
                requestFocus();
                mMediaPlayer.start();
                //MediaPlayerState = true;
            } else if (intent.getAction().equals(ACTION_PAUSE)) {
                mMediaPlayer.pause();
                //MediaPlayerState = false;
            }
        }

        return START_STICKY_COMPATIBILITY;
    }


    private AudioManager.OnAudioFocusChangeListener focusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    switch (focusChange) {


                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
                            // Lower the volume while ducking.
                            Log.v(AUDIO_FOCUS_TAG, "can duck");
                            //mMediaPlayer.start();
                            break;


                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):
                            Log.v(AUDIO_FOCUS_TAG, "loss transient");
                            mMediaPlayer.pause();
                            break;


                        //Если что-то другое включилось
                        case (AudioManager.AUDIOFOCUS_LOSS):
                            ComponentName component = new ComponentName(BackgroundService.this, BackgroundService.class);

                            if (am != null) {
                                am.unregisterMediaButtonEventReceiver(component);
                            }
                            MiniMediaPlayerFragment.playBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);//Меняем иконку
                            mMediaPlayer.pause();

                            break;

                        //Если аудиоканал свободен(он занят, даже если другая музыка стоит на паузе)
                        case (AudioManager.AUDIOFOCUS_REQUEST_GRANTED):
                            // Return the volume to normal and resume if paused.
                            Log.v(AUDIO_FOCUS_TAG, "gain");
                            //if(MediaPlayerState == true) {
                             //   mMediaPlayer.start();
                            //} else mMediaPlayer.pause();

                            break;


                    }
                }
            };




    //Метод для возобновления музыки, вызывается из активности
    public static void resume(Context context) {
        final Intent intent = new Intent(context, BackgroundService.class);
        intent.setAction(ACTION_PLAY);
        context.startService(intent);
    }

    //Метод для паузы музыки, вызывается из активности
    public static void pause(Context context) {
        final Intent intent = new Intent(context, BackgroundService.class);
        intent.setAction(ACTION_PAUSE);
        context.startService(intent);
    }

    private void requestFocus() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = am.requestAudioFocus(focusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        // в result тебе может вернуться AUDIOFOCUS_REQUEST_FAILED, в этом случае звук не нужно проигрывать
        // см, например,
    }
}