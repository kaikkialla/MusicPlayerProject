package com.example.tiget.musicplayer.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tiget.musicplayer.R;
import com.example.tiget.musicplayer.ui.Library.LibraryFragment;

public class voids extends Activity {


    public static void showFragment(Fragment fragment, FragmentActivity activity) {
        activity
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.FrameLayout, new LibraryFragment())
                .commit();
    }


    public static void checkPlayButtonPressedState(Context context, ImageView imageView) {
        if(BackgroundService.mMediaPlayer != null) {
            if (BackgroundService.mMediaPlayer.isPlaying()) {
                BackgroundService.pause(context);
                imageView.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
            } else if (!BackgroundService.mMediaPlayer.isPlaying()) {
                BackgroundService.resume(context);
                imageView.setBackgroundResource(R.drawable.ic_pause_black_24dp);
            }
        }

    }

    public static void checkPlayButtonState(Context context, ImageView imageView) {
        Toast.makeText(context, "Resume", Toast.LENGTH_SHORT).show();
        if(BackgroundService.mMediaPlayer != null) {
            if (!BackgroundService.mMediaPlayer.isPlaying()) {
                //BackgroundService.pause(context);
                imageView.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
            } else if (BackgroundService.mMediaPlayer.isPlaying()) {
                //BackgroundService.resume(context);
                imageView.setBackgroundResource(R.drawable.ic_pause_black_24dp);
            }
        }

    }
}
