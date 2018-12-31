package com.example.tiget.musicplayer.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
                .replace(R.id.FrameLayout, fragment)
                .commit();
    }


    public static void checkPlayButtonPressedState(Context context, ImageView imageView, int color) {
        if(color == 0) {
            if (BackgroundService.mMediaPlayer != null) {
                if (BackgroundService.mMediaPlayer.isPlaying()) {
                    BackgroundService.pause(context);
                    //imageView.setBackgroundResource(R.drawable.play_black);
                } else if (!BackgroundService.mMediaPlayer.isPlaying()) {
                    BackgroundService.resume(context);
                    //imageView.setBackgroundResource(R.drawable.pause_black);
                }
            }
        } else if(color == 1) {
            if (BackgroundService.mMediaPlayer != null) {
                if (BackgroundService.mMediaPlayer.isPlaying()) {
                    BackgroundService.pause(context);
                    //imageView.setBackgroundResource(R.drawable.play_gray);
                } else if (!BackgroundService.mMediaPlayer.isPlaying()) {
                    BackgroundService.resume(context);
                    //imageView.setBackgroundResource(R.drawable.pause_gray);
                }
            }
        }

    }


    public static void checkPlayButtonState(Context context, ImageView imageView, int color) {
        if(color == 0) {
            if(BackgroundService.mMediaPlayer != null) {
                if (!BackgroundService.mMediaPlayer.isPlaying()) {
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.play_black));
                } else if (BackgroundService.mMediaPlayer.isPlaying()) {
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.pause_black));
                }
            }
        } else if(color == 1) {
            if(BackgroundService.mMediaPlayer != null) {
                if (!BackgroundService.mMediaPlayer.isPlaying()) {
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.play_gray));
                } else if (BackgroundService.mMediaPlayer.isPlaying()) {
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.pause_gray));
                }
            }
        }
    }


    public static void showMiniMediaFragment(String SongUri, String AuthorName, String SongName, int ResId, FragmentActivity activity) {
        MiniMediaPlayerFragment fragment = MiniMediaPlayerFragment.newInstance(SongUri, AuthorName, SongName, ResId);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.miniMediaPlayerLayout, fragment).commit();
    }



    public static void showMediaFragment(String SongUri, String AuthorName, String SongName, int ResId, FragmentActivity activity) {
        MediaPlayerFragment mediaPlayerFragment = MediaPlayerFragment.newInstance(SongUri, AuthorName, SongName, ResId);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, mediaPlayerFragment).commit();
    }

    public static void showSongInfoFragment(String SongUri, String AuthorName, String SongName, int ResId, FragmentActivity activity) {
        final SongInfoFragment fragment = SongInfoFragment.newInstance(SongUri, AuthorName, SongName, ResId);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, fragment).commit();
    }

}






/*
SongInfoFragment fragment = SongInfoFragment.newInstance(song.getSongUri(), song.getAuthorName(), song.getSongName(), song.getSongPreview());
 */
