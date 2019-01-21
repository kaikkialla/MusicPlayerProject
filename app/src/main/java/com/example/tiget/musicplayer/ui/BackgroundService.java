package com.example.tiget.musicplayer.ui;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

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


    private static final String ACTION_PLAY  = "ACTION_PLAY";
    private static final String ACTION_PAUSE = "ACTION_PAUSE";
    private static final String ACTION_SET_SONG = "ACTION_SET_SONG";
    private static final String ACTION_CHANGE_SONG = "ACTION_CHANGE_SONG";
    private static final String ACTION_SET_NEXT_SONG = "ACTION_SET_NEXT_SONG";
    private static final String ACTION_SET_PREVIOUS_SONG = "ACTION_SET_PREVIOUS_SONG";
    private static final String ACTION_SEEK_TO = "ACTION_SEEK_TO";

    public static int mTime;
    public static int mDuration;


    public static List<Song> songs = new ArrayList<>();
    public static int pos;


    private static ChangeListener mChangeListener = null;
    //public static boolean isPlaying;




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





    /*
    Log.e("fisfsjufs", songs.size() + " / " + pos);
    */



    public int onStartCommand(Intent intent, int flags, int startId) {


        if (intent.getAction() != null) {
            if (intent.getAction().equals(ACTION_PLAY)) {

                requestFocus();

                mMediaPlayer.start();
                //isPlaying = true;



                if(MiniMediaPlayerFragment.mPauseButton != null) {
                    t.checkPlayButtonState(context, MiniMediaPlayerFragment.mPauseButton, 1);
                }
                if(MediaPlayerFragment.mPauseButton != null) {
                    t.checkPlayButtonState(context, MediaPlayerFragment.mPauseButton, 0);
                }


            } else if (intent.getAction().equals(ACTION_PAUSE)) {



                mMediaPlayer.pause();
                //isPlaying = false;



                if(MiniMediaPlayerFragment.mPauseButton != null) {
                    t.checkPlayButtonState(context, MiniMediaPlayerFragment.mPauseButton, 1);
                }
                if(MediaPlayerFragment.mPauseButton != null) {
                    t.checkPlayButtonState(context, MediaPlayerFragment.mPauseButton, 0);
                }


                /*
                 * SET SONG
                 */


            } else if(intent.getAction().equals(ACTION_SET_SONG)) {

                try{
                    mMediaPlayer = MediaPlayer.create(context, Uri.parse(mSongUri));//Создаем MediaPlayer
                    mMediaPlayer.setLooping(false);//Песня не будет повторяться
                    mMediaPlayer.seekTo(0);//Песня влючится на 0й секунде
                    mMediaPlayer.setVolume(0.3f, 0.7f);//Начальная громкость
                    mMediaPlayer.start();//Запускаем



                    //isPlaying = true;



                } catch (Exception e) {
                    Toast.makeText(context, "Невозможно произвести данную аудиозапись", Toast.LENGTH_SHORT).show();
                }

                EventBus.getDefault().post(new SongChangedEvent(songs, pos));


                /*
                 * CHANGE SONG
                 */


            } else if(intent.getAction().equals(ACTION_CHANGE_SONG)) {


                //isPlaying = true;

                changeSong();

                EventBus.getDefault().post(new SongChangedEvent(songs, pos));


                /*
                 * PREV SONG
                 */


            } else if(intent.getAction().equals(ACTION_SET_PREVIOUS_SONG)) {

                changeSong();
                //isPlaying = true;



                EventBus.getDefault().post(new SongChangedEvent(songs, pos));


                /*
                 * NEXT SONG
                 */


            } else if(intent.getAction().equals(ACTION_SET_NEXT_SONG)) {
                changeSong();

                mMediaPlayer.start();//Запускаем
                //isPlaying = true;

                EventBus.getDefault().post(new SongChangedEvent(songs, pos));

            } else if(intent.getAction().equals(ACTION_SEEK_TO)) {

                if(mTime != mDuration) {
                    mMediaPlayer.seekTo(mTime);
                }

            }
        }




        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mMediaPlayer.getCurrentPosition();
                        mHandler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }).start();


        return START_STICKY_COMPATIBILITY;
    }



    //Устанавливаем текущее и оставшееся время
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int time = msg.what / 1000;
            int duration = mMediaPlayer.getDuration() / 1000;

            if(time < duration) {
                EventBus.getDefault().post(new TimeChangedEvent(time, duration));
            } else if(time >= duration) {
                duration = 0;
                time = 0;
                setNextSong(songs, pos, context);
            }
        }
    };






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
                            //isPlaying = false;



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




    private void requestFocus() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = am.requestAudioFocus(focusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        // в result тебе может вернуться AUDIOFOCUS_REQUEST_FAILED, в этом случае звук не нужно проигрывать
        // см, например,
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


    public static void setSong(List<Song> mSongs, int i,  Context context) {
        final Intent intent = new Intent(context, BackgroundService.class);
        intent.setAction(ACTION_SET_SONG);

        songs = mSongs;
        pos = i;

        mSongUri = mSongs.get(i).getSongUri();
        mSongName = mSongs.get(i).getSongName();
        mAuthorName = mSongs.get(i).getAuthorName();
        mResId = mSongs.get(i).getSongPreview();

        context.startService(intent);
    }


    public static void changeSong(List<Song> mSongs, int i, Context context) {
        final Intent intent = new Intent(context, BackgroundService.class);
        intent.setAction(ACTION_CHANGE_SONG);

        songs = mSongs;
        pos = i;

        mSongUri = mSongs.get(i).getSongUri();
        mSongName = mSongs.get(i).getSongName();
        mAuthorName = mSongs.get(i).getAuthorName();
        mResId = mSongs.get(i).getSongPreview();

        context.startService(intent);
    }


    public static void setNextSong(List<Song> mSongs, int i, Context context) {
        final Intent intent = new Intent(context, BackgroundService.class);
        intent.setAction(ACTION_SET_NEXT_SONG);

        if(pos != mSongs.size() -1) {
            songs = mSongs;
            pos = i + 1;

            mSongUri = mSongs.get(pos).getSongUri();
            mSongName = mSongs.get(pos).getSongName();
            mAuthorName = mSongs.get(pos).getAuthorName();
            mResId = mSongs.get(pos).getSongPreview();
        }


        context.startService(intent);
    }


    public static void setPreviousSongSong(List<Song> mSongs, int i, Context context) {
        final Intent intent = new Intent(context, BackgroundService.class);
        intent.setAction(ACTION_SET_PREVIOUS_SONG);

        if(pos != 0) {
            songs = mSongs;
            pos = i - 1;

            mSongUri = mSongs.get(pos).getSongUri();
            mSongName = mSongs.get(pos).getSongName();
            mAuthorName = mSongs.get(pos).getAuthorName();
            mResId = mSongs.get(pos).getSongPreview();
        }


        context.startService(intent);
    }



    public static void seekTo(int time, Context context) {
        final Intent intent = new Intent(context, BackgroundService.class);
        intent.setAction(ACTION_SEEK_TO);

        mTime = time;

        context.startService(intent);
    }


    private void changeSong() {
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


    public static class TimeChangedEvent {
        public int time;
        public int duration;

        private TimeChangedEvent(int time, int duration) {
            this.time = time;
            this.duration = duration;
        }

        public int getTime() {
            return time;
        }

        public int getDuration() {
            return duration;
        }
    }




    public static class SongChangedEvent {
        List<Song> songs;
        int pos;

        private SongChangedEvent(List<Song> songs, int pos) {
            this.songs = songs;
            this.pos = pos;
        }

        public List<Song> getSongs() {
            return songs;
        }

        public int getPos() {
            return pos;
        }
    }





    /**
     *
     * Как-то переделать под mMediaPlayer.isPlaying
     */

    public static void setChangeListener(ChangeListener listener) {
        mChangeListener = listener;
    }

    interface ChangeListener {

        void onChange(boolean isPlaying);
    }

}


