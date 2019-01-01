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
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class BackgroundService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    public static MediaPlayer mMediaPlayer;
    public static String mSongUri;
    public static String mSongName;
    public static String mAuthorName;
    public static int mResId;

    public static final String ACTION_PLAY  = "ACTION_PLAY";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_SET_SONG = "ACTION_SET_SONG";
    public static final String ACTION__CHANGE_SONG = "ACTION__CHANGE_SONG";

    public static List<Song> mSongs = new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        context = getApplicationContext();
        requestFocus();
    }

    public void onDestroy() {
        handler.removeCallbacks(runnable);//заканчивает процесс при выключении приложения(Можно убрать, тогда будет работать всегда)
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null) {
            if (intent.getAction().equals(ACTION_PLAY)) {
                requestFocus();
                mMediaPlayer.start();

                if(MiniMediaPlayerFragment.mPauseButton != null) {
                    t.checkPlayButtonState(context, MiniMediaPlayerFragment.mPauseButton, 1);
                }
                if(MediaPlayerFragment.mPauseButton != null) {
                    t.checkPlayButtonState(context, MediaPlayerFragment.mPauseButton, 0);
                }



            } else if (intent.getAction().equals(ACTION_PAUSE)) {
                mMediaPlayer.pause();

                if(MiniMediaPlayerFragment.mPauseButton != null) {
                    t.checkPlayButtonState(context, MiniMediaPlayerFragment.mPauseButton, 1);
                }
                if(MediaPlayerFragment.mPauseButton != null) {
                    t.checkPlayButtonState(context, MediaPlayerFragment.mPauseButton, 0);
                }


            } else if(intent.getAction().equals(ACTION_SET_SONG)) {
                try{
                    mMediaPlayer = MediaPlayer.create(context, Uri.parse(mSongUri));//Создаем MediaPlayer
                    mMediaPlayer.setLooping(false);//Песня не будет повторяться
                    mMediaPlayer.seekTo(0);//Песня влючится на 0й секунде
                    mMediaPlayer.setVolume(0.3f, 0.7f);//Начальная громкость
                    mMediaPlayer.start();//Запускаем
                } catch (Exception e) {
                    Toast.makeText(context, "Невозможно произвести данную аудиозапись", Toast.LENGTH_SHORT).show();
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
                        //Если что-то другое включилось
                        case (AudioManager.AUDIOFOCUS_LOSS):
                            ComponentName component = new ComponentName(BackgroundService.this, BackgroundService.class);


                            if (am != null) {
                                am.unregisterMediaButtonEventReceiver(component);
                            }
                            mMediaPlayer.pause();

                            if(MiniMediaPlayerFragment.mPauseButton != null) {
                                t.checkPlayButtonState(context, MiniMediaPlayerFragment.mPauseButton, 1);
                            }
                            if(MediaPlayerFragment.mPauseButton != null) {
                                t.checkPlayButtonState(context, MediaPlayerFragment.mPauseButton, 0);
                            }

                            break;

                        //Если аудиоканал свободен(он занят, даже если другая музыка стоит на паузе)
                        case (AudioManager.AUDIOFOCUS_REQUEST_GRANTED):
                            break;


                    }
                }
            };



    public static void setSong(List<Song> mSongs, int pos,  Context context) {
        final Intent intent = new Intent(context, BackgroundService.class);
        intent.setAction(ACTION_SET_SONG);
        mSongUri = mSongs.get(pos).getSongUri();
        mSongName = mSongs.get(pos).getSongName();
        mAuthorName = mSongs.get(pos).getAuthorName();
        mResId = mSongs.get(pos).getSongPreview();
        context.startService(intent);
    }


    public static void changeSong(List<Song> mSongs, int pos, Context context) {
        final Intent intent = new Intent(context, BackgroundService.class);
        intent.setAction(ACTION__CHANGE_SONG);
        mSongUri = mSongs.get(pos).getSongUri();
        mSongName = mSongs.get(pos).getSongName();
        mAuthorName = mSongs.get(pos).getAuthorName();
        mResId = mSongs.get(pos).getSongPreview();
        context.startService(intent);
    }


    public static void resume(Context context) {
        final Intent intent = new Intent(context, BackgroundService.class);
        intent.setAction(ACTION_PLAY);
        context.startService(intent);
    }



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


    private static ChangeListener mChangeListener = null;
    public static void onChangeListener(ChangeListener listener) {
        mChangeListener = listener;
    }

    interface ChangeListener {

        void onChange(MediaPlayer mMediaPlayer);
    }
}
