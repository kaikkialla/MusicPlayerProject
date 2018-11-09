package com.example.tiget.musicplayer;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.tiget.musicplayer.MainActivity;

import java.io.IOException;


public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    MainActivity activity;
    public static String SongUri;
    public static MediaPlayer mMediaPlayer;
    public static int totalTime;
    Context context;

    public RecyclerViewAdapter(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.row, parent, false );
        ViewHolder vh = new ViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        context = this.context;

        final Constructor constructor = DataBase.Arr[position];
        holder.SongName.setText(constructor.SongName);
        holder.AuthorName.setText(constructor.AuthorName);
        holder.SongPreview.setImageResource(constructor.SongPreview);


        //SongUri = "http://dl.mp3party.net/download/8677221";
        SongUri = constructor.SongUri;


        mMediaPlayer = MediaPlayer.create(activity, Uri.parse(SongUri));
        totalTime = mMediaPlayer.getDuration();

        String timeLabel = "";
        int min = totalTime / 1000 / 60;
        int sec = totalTime / 1000 % 60;
        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        holder.SongLenght.setText(String.valueOf(timeLabel));

        //заполняем поля макета
                //при нажатии на макет откроется фрагмент с информацией о определенной валюте
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMiniMediaPlayerFragment(constructor);

            }
        });
    }

    @Override
    public int getItemCount() {
        return DataBase.Arr.length;

    }


    //метод, рисующий фрагмент с иформацией о валюте
    private void showMiniMediaPlayerFragment(Constructor constructor) {
        activity.startService(new Intent(activity, BackgroundService.class)); //Для игры музыки на заднем плане

        //MediaPlayerFragment fragment = MediaPlayerFragment.newInstance(constructor);
        //activity.getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, fragment).commit();


        MiniMediaPlayerFragment fragment = MiniMediaPlayerFragment.newInstance(constructor);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.miniFrameLayout, fragment).commit();




    }

}
