package com.example.tiget.musicplayer.ui.Library;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
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
import com.example.tiget.musicplayer.ui.UserLibrary.MediaPlayerFragment;
import com.example.tiget.musicplayer.ui.UserLibrary.MiniMediaPlayerFragment;
import com.example.tiget.musicplayer.ui.UserLibrary.PlaylistDatabase;
import com.example.tiget.musicplayer.ui.UserLibrary.RecyclerViewAdapter;
import com.example.tiget.musicplayer.ui.UserLibrary.UserLibSong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LibraryAdapter extends RecyclerView.Adapter<ViewHolder> {

    MainActivity activity;
    public static String SongUri;
    public static int totalTime;
    public List<LibSong> mSong = new ArrayList<>();
    PlaylistDatabase mDatabase;






    public LibraryAdapter(MainActivity activity) {
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.lib_song_item, parent, false );
        ViewHolder vh = new ViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final UserLibSong constructor = Database.Arr[position];
        holder.SongName.setText(constructor.SongName);
        holder.AuthorName.setText(constructor.AuthorName);
        holder.SongPreview.setImageResource(constructor.SongPreview);

        SongUri = constructor.SongUri;
        holder.previewSongController.setVisibility(View.GONE);


        mDatabase = new PlaylistDatabase(activity);

        holder.SongLenght.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("BHUFDUSUIGSA", "Current URL:  " + constructor.SongUri);
                //Log.e("BHUFDUSUIGSA", "Arr size:  " + RecyclerViewAdapter.mSongs.size());


                //Log.e("BHUFDUSUIGSA", "Playlist URI's:  " + RecyclerViewAdapter.mSongs.get(i).SongUri + "  Someshit " + constructor.SongUri.equals(RecyclerViewAdapter.mSongs.get(i).SongUri));

                if (RecyclerViewAdapter.mSongs.size() == 0) {
                    mDatabase.addSong(new UserLibSong(RecyclerViewAdapter.mSongs.size(), constructor.AuthorName, constructor.SongName, constructor.SongUri, constructor.SongPreview));
                } else if (RecyclerViewAdapter.mSongs.size() > 0) {
                    for(int i = 0; i <= RecyclerViewAdapter.mSongs.size() - 1; i++) {
                        //Log.e("BHUFDUSUIGSA", RecyclerViewAdapter.mSongs.get(i).getSongUri() + "   " + constructor.SongUri);
                        if (RecyclerViewAdapter.mSongs.get(i).getSongUri().equals(constructor.SongUri)) {
                            Toast.makeText(activity, "Данная песня уже у вас в плейлисте", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(activity, "Данной  песни еще нету у вас в плейлисте", Toast.LENGTH_SHORT).show();

                    }
                    //mDatabase.addSong(new UserLibSong(mSong.size(), constructor.AuthorName, constructor.SongName, constructor.SongUri, constructor.SongPreview));
                }
            }
        });


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



    @Override
    public int getItemCount() {
        return Database.Arr.length;

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
