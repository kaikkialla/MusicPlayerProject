package com.example.tiget.musicplayer.ui.UserLibrary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.tiget.musicplayer.R;
import com.example.tiget.musicplayer.ui.BackgroundService;
import com.example.tiget.musicplayer.ui.MainActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RecyclerViewAdapter extends RecyclerView.Adapter<PlaylistViewHolder> {

    MainActivity activity;
    public static String SongUri;
    public static int totalTime;
    Context context;

    public static List<UserLibSong> mSongs = new ArrayList<>();




    public RecyclerViewAdapter(MainActivity activity) {
        this.activity = activity;
    }


    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.user_lib_song_item, parent, false );
        PlaylistViewHolder vh = new PlaylistViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(final PlaylistViewHolder holder, final int position) {
        context = this.context;

        final UserLibSong constructor = mSongs.get(position);
        holder.SongName.setText(constructor.SongName);
        holder.AuthorName.setText(constructor.AuthorName);
        holder.SongPreview.setImageResource(constructor.SongPreview);

        SongUri = constructor.SongUri;
        holder.previewSongController.setVisibility(View.GONE);





        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Если что-то уже играет
                if(BackgroundService.mMediaPlayer != null) {

                    if(SongUri != constructor.SongUri) {

                        BackgroundService.changeSong(activity, constructor.SongUri);
                        showMiniMediaFragment(constructor);
                        SongUri = constructor.SongUri;
                    } else if(SongUri == constructor.SongUri) {
                        showMediaFragment(constructor);
                    }

                    //При первом нажатии на песню
                } else  if(BackgroundService.mMediaPlayer == null){

                    holder.previewSongController.setVisibility(View.GONE);
                    BackgroundService.setSong(activity, constructor.SongUri);
                    SongUri = constructor.SongUri;
                    //activity.startService(new Intent(activity, BackgroundService.class));
                    showMiniMediaFragment(constructor);

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


    public void showMiniMediaFragment(UserLibSong constructor) {
        //Создаем сервис для действий в фоновом режиме
        //activity.startService(new Intent(activity, BackgroundService.class));
        //открываем фрагмент с плеером внизу экрана
        MiniMediaPlayerFragment fragment = MiniMediaPlayerFragment.newInstance(constructor);

        activity.getSupportFragmentManager().beginTransaction().replace(R.id.miniFrameLayout, fragment).commit();
    }



    private void showMediaFragment(UserLibSong constructor) {

        MediaPlayerFragment fragment = MediaPlayerFragment.newInstance(constructor);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, fragment).commit();
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
