package com.example.tiget.musicplayer.ui;

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
import android.widget.Toast;

import com.example.tiget.musicplayer.R;
import com.example.tiget.musicplayer.ui.Library.LibraryAdapter;
import com.example.tiget.musicplayer.ui.UserLibrary.MiniMediaPlayerFragment;
import com.example.tiget.musicplayer.ui.UserLibrary.RecyclerViewAdapter;

import java.io.IOException;


public class BackgroundService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    public static MediaPlayer mMediaPlayer;
    public static String mSongUri;

    public static final String ACTION_PLAY  = "ACTION_PLAY";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_SET_SONG = "ACTION_SET_SONG";
    public static final String ACTION__CHANGE_SONG = "ACTION__CHANGE_SONG";


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        context = getApplicationContext();
        //TODO

        requestFocus();

        /*
        if(RecyclerViewAdapter.SongUri == null){
            SongUri = LibraryAdapter.SongUri;//Получаем ссылку на песню
        } else if(LibraryAdapter.SongUri == null) {
            SongUri = RecyclerViewAdapter.SongUri;
        }
*/
/*
        try{
            mMediaPlayer = MediaPlayer.create(context, Uri.parse(SongUri));//Создаем MediaPlayer
            mMediaPlayer.setLooping(false);//Песня не будет повторяться
            mMediaPlayer.seekTo(0);//Песня влючится на 0й секунде
            mMediaPlayer.setVolume(0.3f, 0.7f);//Начальная громкость
            mMediaPlayer.start();//Запускаем
        } catch (Exception e) {
            Toast.makeText(context, "Unable to play this song", Toast.LENGTH_SHORT).show();
        }
*/


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


            } else if(intent.getAction().equals(ACTION_SET_SONG)) {
                try{
                    mMediaPlayer = MediaPlayer.create(context, Uri.parse(mSongUri));//Создаем MediaPlayer
                    mMediaPlayer.setLooping(false);//Песня не будет повторяться
                    mMediaPlayer.seekTo(0);//Песня влючится на 0й секунде
                    mMediaPlayer.setVolume(0.3f, 0.7f);//Начальная громкость
                    mMediaPlayer.start();//Запускаем
                } catch (Exception e) {
                    Toast.makeText(context, "Unable to play this song", Toast.LENGTH_SHORT).show();
                }


            } else if(intent.getAction().equals(ACTION__CHANGE_SONG)) {
                    mMediaPlayer.reset();//Сбрасываем текущую песню
                    try {
                        mMediaPlayer.setDataSource(context, Uri.parse(mSongUri));//Включаем ту, на которую нажали
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        mMediaPlayer.prepare();//Что-то делаем, не знаю что, но надо
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mMediaPlayer.start();//Запускаем
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
                            //mMediaPlayer.start();
                            break;


                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):
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
                            //if(MediaPlayerState == true) {
                             //   mMediaPlayer.start();
                            //} else mMediaPlayer.pause();

                            break;


                    }
                }
            };



    public static void setSong(Context context, String SongUri) {
        final Intent intent = new Intent(context, BackgroundService.class);

        intent.setAction(ACTION_SET_SONG);
        mSongUri = SongUri;
        context.startService(intent);
    }


    public static void changeSong(Context context, String SongUri) {
        final Intent intent = new Intent(context, BackgroundService.class);
        intent.setAction(ACTION__CHANGE_SONG);
        mSongUri = SongUri;
        context.startService(intent);
    }




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