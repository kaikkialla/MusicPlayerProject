package com.example.tiget.musicplayer.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.transition.Fade;
import android.widget.ImageView;

import com.example.tiget.musicplayer.R;

import java.util.List;

public class t extends Activity {

    private static final long MOVE_DEFAULT_TIME = 200;
    private static final long FADE_DEFAULT_TIME = 200;

    public static String mLibraryFragmentTag = "LibraryFragment";
    public static String mUserLibraryFragmentTag = "UserLibraryFragment";
    public static String mMediaPlayerFragmentTag = "MediaPlayerFragment";
    public static String mMiniMediaPlayerFragmentTag = "MiniMediaPlayerFragment";
    public static String mSongInfoFragmentTag = "SongInfoFragment";



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








    public static void showFragment(Fragment fragment, FragmentActivity activity,  String tag) {
        activity
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_right, R.anim.exit_to_bottom, R.anim.exit_to_left)
                .replace(R.id.f, fragment, tag)
                .commit();
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void showMiniMediaFragment(List<Song> mSongs, int pos, FragmentActivity activity, String tag) {
        MiniMediaPlayerFragment fragment = MiniMediaPlayerFragment.newInstance(mSongs, pos);

        Fade fade = new Fade();
        fade.setDuration(1000);
        fragment.setEnterTransition(fade);

        activity
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.miniMediaPlayerLayout, fragment, tag)
                .commit();
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void showMediaFragment(List<Song> mSongs, int pos, FragmentActivity activity, String tag) {
        MediaPlayerFragment fragment = MediaPlayerFragment.newInstance(mSongs, pos);

        activity
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_bottom, R.anim.enter_from_bottom)
                .add(R.id.f, fragment, tag)
                .commit();
    }



    public static void showSongInfoFragment(String SongUri, String AuthorName, String SongName, int ResId, FragmentActivity activity) {
        final SongInfoFragment fragment = SongInfoFragment.newInstance(SongUri, AuthorName, SongName, ResId);
        fragment.show(activity.getSupportFragmentManager(), "BottomSheet");
        //Fad fade = new Fade();
        //Fade fade = new Fade();
        //fade.setDuration(1000);
        //fragment.setEnterTransition(fade);

        //Slide slide = new Slide();
        //slide.setDuration(1000);
        //fragment.setReturnTransition(slide);
        //activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.enter_from_left).add(R.id.FrameLayout, fragment).commit();
    }

}






/*
SongInfoFragment fragment = SongInfoFragment.newInstance(song.getSongUri(), song.getAuthorName(), song.getSongName(), song.getSongPreview());
 */
