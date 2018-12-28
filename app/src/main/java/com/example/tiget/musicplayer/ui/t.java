package com.example.tiget.musicplayer.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.example.tiget.musicplayer.R;
import com.example.tiget.musicplayer.ui.Library.LibraryFragment;
import com.example.tiget.musicplayer.ui.UserLibrary.UserLibSong;

public class t extends Activity {



    public static void showFragment(Fragment fragment, FragmentActivity activity) {
        activity
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.FrameLayout, new LibraryFragment())
                .commit();
    }


    public static void checkPlayButtonPressedState(Context context, ImageView imageView, int color) {
        if(color == 0) {
            if (BackgroundService.mMediaPlayer != null) {
                if (BackgroundService.mMediaPlayer.isPlaying()) {
                    BackgroundService.pause(context);
                    imageView.setBackgroundResource(R.drawable.play_black);
                } else if (!BackgroundService.mMediaPlayer.isPlaying()) {
                    BackgroundService.resume(context);
                    imageView.setBackgroundResource(R.drawable.pause_black);
                }
            }
        } else if(color == 1) {
            if (BackgroundService.mMediaPlayer != null) {
                if (BackgroundService.mMediaPlayer.isPlaying()) {
                    BackgroundService.pause(context);
                    imageView.setBackgroundResource(R.drawable.play_gray);
                } else if (!BackgroundService.mMediaPlayer.isPlaying()) {
                    BackgroundService.resume(context);
                    imageView.setBackgroundResource(R.drawable.pause_gray);
                }
            }
        }

    }

    public static void checkPlayButtonState(Context context, ImageView imageView, int color) {
        if(color == 0) {
            if(BackgroundService.mMediaPlayer != null) {
                if (!BackgroundService.mMediaPlayer.isPlaying()) {
                    imageView.setBackgroundResource(R.drawable.play_black);
                } else if (BackgroundService.mMediaPlayer.isPlaying()) {
                    imageView.setBackgroundResource(R.drawable.pause_black);
                }
            }
        } else if(color == 1) {
            if(BackgroundService.mMediaPlayer != null) {
                if (!BackgroundService.mMediaPlayer.isPlaying()) {
                    imageView.setBackgroundResource(R.drawable.play_gray);
                } else if (BackgroundService.mMediaPlayer.isPlaying()) {
                    imageView.setBackgroundResource(R.drawable.pause_gray);
                }
            }
        }

    }

    public static void showMiniMediaFragment(UserLibSong constructor, FragmentActivity activity) {
        MiniMediaPlayerFragment fragment = MiniMediaPlayerFragment.newInstance(constructor);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.miniMediaPlayerLayout, fragment).commit();
    }


    public static void showMediaFragment(UserLibSong constructor, FragmentActivity activity) {
        MediaPlayerFragment mediaPlayerFragment = MediaPlayerFragment.newInstance(constructor);
        MiniMediaPlayerFragment miniMediaPlayerFragment = new MiniMediaPlayerFragment();


       // activity.getSupportFragmentManager().beginTransaction().replace(R.id.miniMediaPlayerLayout, null).commit();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, mediaPlayerFragment).commit();

    }
}
