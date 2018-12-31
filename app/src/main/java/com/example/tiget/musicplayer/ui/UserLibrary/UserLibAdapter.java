package com.example.tiget.musicplayer.ui.UserLibrary;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tiget.musicplayer.R;
import com.example.tiget.musicplayer.ui.BackgroundService;
import com.example.tiget.musicplayer.ui.MainActivity;
import com.example.tiget.musicplayer.ui.MediaPlayerFragment;
import com.example.tiget.musicplayer.ui.SongInfoFragment;
import com.example.tiget.musicplayer.ui.t;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UserLibAdapter extends RecyclerView.Adapter<ViewHolder> {

    MainActivity activity;

    Context context;

    public static long mCurrentSongId;
    public static UserLibSong nextSong;
    public static UserLibSong previousSong;

    public static List<UserLibSong> mSongs = new ArrayList<>();

    public static String mSongUri;
    public static String mAuthorName;
    public static String mSongName;
    public static int mResId;
    public static int mTotalTime;


    public UserLibAdapter(MainActivity activity) {
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.user_lib_song_item, parent, false );
        ViewHolder vh = new ViewHolder(v);

        return vh;

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        context = this.context;

        final UserLibSong song = mSongs.get(position);
        mSongUri = song.getSongUri();
        mAuthorName = song.getAuthorName();
        mSongName = song.getSongName();
        mResId = song.getSongPreview();

        Log.e("gagaga", "USERLIB: " + mSongName);

        holder.SongName.setText(mSongName);
        holder.AuthorName.setText(mAuthorName);
        holder.SongPreview.setImageResource(mResId);

        if(position > 0) {
            previousSong = mSongs.get(position - 1);
        }

        if(position < mSongs.size() - 1) {
            nextSong = mSongs.get(position + 1);
        }

/**
 *  При открытии MediaPlayer'а, закрытии и нажатии на ту же самую песню
 */

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("gagaga", "USERLIB: " + mSongName);
                //Если что-то уже играет
                if(BackgroundService.mMediaPlayer != null) {

                    if(mSongUri != song.SongUri) {
                        BackgroundService.changeSong(song.getSongUri(), song.getAuthorName(), song.getSongName(),song.getSongPreview(), activity);
                        t.showMiniMediaFragment(song.getSongUri(), song.getAuthorName(), song.getSongName(),song.getSongPreview(), activity);
                        mSongUri = song.SongUri;
                    } else if(mSongUri == song.SongUri) {
                        t.showMediaFragment(song.getSongUri(), song.getAuthorName(), song.getSongName(),song.getSongPreview(), activity);
                    }

                    //При первом нажатии на песню
                } else  if(BackgroundService.mMediaPlayer == null){

                    BackgroundService.setSong(song.getSongUri(), song.getAuthorName(), song.getSongName(),song.getSongPreview(), activity);
                    mSongUri = song.SongUri;
                    //activity.startService(new Intent(activity, BackgroundService.class));
                    t.showMiniMediaFragment(song.getSongUri(), song.getAuthorName(), song.getSongName(),song.getSongPreview(), activity);

                }
            }

        });



        holder.SongInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.showSongInfoFragment(song.getSongUri(), song.getAuthorName(), song.getSongName(), song.getSongPreview(), activity);
            }
        });





    }


    public void swap(List<UserLibSong> songs) {
        if (songs != null) {
            mSongs.clear();
            mSongs.addAll(songs);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mSongs.size();

    }
}
