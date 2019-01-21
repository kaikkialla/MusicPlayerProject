package com.example.tiget.musicplayer.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;


public class MediaButtonReceiver extends BroadcastReceiver {

    private long lastTime;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != null && System.currentTimeMillis() - lastTime >= 1000) {
            if(BackgroundService.mMediaPlayer != null) {
                if (BackgroundService.mMediaPlayer.isPlaying()) {
                    BackgroundService.pause(context);
                    lastTime = System.currentTimeMillis();
                    Log.e("HeadsetButtonClick", "is playing");

                } else if (!BackgroundService.mMediaPlayer.isPlaying()) {
                    BackgroundService.resume(context);
                    lastTime = System.currentTimeMillis();
                    Log.e("HeadsetButtonClick", "is NOT playing");
                }
//MiniMediaPlayerFragment.checkPlayButtonState(MiniMediaPlayerFragment.playBtn);
//MiniMediaPlayerFragment.checkPlayButtonState(MediaPlayerFragment.playBtn);
            }
        }
    }
}

/**
 * При нажатии на кнопку приходит 2 сигнала.
 * После первого сигнала надо ставить паузу на 0.5 секунд, во время которой сигнал не будет приниматься
 *
 * */

