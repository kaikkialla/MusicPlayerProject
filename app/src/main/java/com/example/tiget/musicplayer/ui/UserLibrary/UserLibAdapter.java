package com.example.tiget.musicplayer.ui.UserLibrary;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.tiget.musicplayer.R;
import com.example.tiget.musicplayer.ui.BackgroundService;
import com.example.tiget.musicplayer.ui.MainActivity;
import com.example.tiget.musicplayer.ui.t;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UserLibAdapter extends RecyclerView.Adapter<ViewHolder> {

    MainActivity activity;
    public static String SongUri;
    public static int totalTime;
    Context context;

    public static long mCurrentSongId;
    public static UserLibSong nextSong;
    public static UserLibSong previousSong;

    public static List<UserLibSong> mSongs = new ArrayList<>();




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
        holder.SongName.setText(song.SongName);
        holder.AuthorName.setText(song.AuthorName);
        holder.SongPreview.setImageResource(song.SongPreview);

        if(position > 1) {
            previousSong = mSongs.get(position -1);
        }

        if(position < mSongs.size() -1) {
            nextSong = mSongs.get(position + 1);
        }

        mCurrentSongId = song.id;

        SongUri = song.SongUri;





        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Если что-то уже играет
                if(BackgroundService.mMediaPlayer != null) {

                    if(SongUri != song.SongUri) {

                        BackgroundService.changeSong(song.SongUri, activity);
                        t.showMiniMediaFragment(song, activity);
                        SongUri = song.SongUri;
                    } else if(SongUri == song.SongUri) {
                        t.showMediaFragment(song, activity);
                    }

                    //При первом нажатии на песню
                } else  if(BackgroundService.mMediaPlayer == null){

                    BackgroundService.setSong(song.SongUri, activity);
                    SongUri = song.SongUri;
                    //activity.startService(new Intent(activity, BackgroundService.class));
                    t.showMiniMediaFragment(song, activity);

                }
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





    //Код для работы progressBar
    public void setProgressBarTime(final ProgressBar progressBar) {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                progressBar.setMax(BackgroundService.mMediaPlayer.getDuration() / 1000);//Макс значение - длина песни(в секундах)
                int currentPosition =  BackgroundService.mMediaPlayer.getCurrentPosition() / 1000;//Текущая позиция(в секундах)
                progressBar.setProgress(currentPosition);

            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 0, 1);
    }






}
