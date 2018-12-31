package com.example.tiget.musicplayer.ui.Library;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tiget.musicplayer.R;
import com.example.tiget.musicplayer.ui.BackgroundService;
import com.example.tiget.musicplayer.ui.MainActivity;
import com.example.tiget.musicplayer.ui.SongInfoFragment;
import com.example.tiget.musicplayer.ui.UserLibrary.UserLibDatabase;
import com.example.tiget.musicplayer.ui.UserLibrary.UserLibSong;
import com.example.tiget.musicplayer.ui.t;

import java.util.ArrayList;
import java.util.List;



public class LibraryAdapter extends RecyclerView.Adapter<ViewHolder> {

    MainActivity activity;
    UserLibDatabase mDatabase;

    private static String mSongUri;
    private static String mAuthorName;
    private static String mSongName;
    private static int mResId;

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
        final LibSong song = Database.Array[position];

        mSongUri = song.SongUri;
        mAuthorName = song.AuthorName;
        mSongName = song.SongName;
        mResId = song.SongPreview;

        Log.e("gagaga", "LIB: " + song.SongName + song.SongUri);

        holder.SongName.setText(song.SongName);
        holder.AuthorName.setText(song.AuthorName);

        if(mResId != 0) {
            holder.SongPreview.setImageResource(mResId);
        } else if(mResId == 0) {
            holder.SongPreview.setImageResource(R.drawable.no_image_loaded);
        }



        mDatabase = new UserLibDatabase(activity);



        holder.SongInfo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                t.showSongInfoFragment(song.getSongUri(), song.getAuthorName(), song.getSongName(), song.getSongPreview(), activity);
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                //Если что-то уже играет
                if(BackgroundService.mMediaPlayer != null) {
                    if(mSongUri != song.getSongUri()) {
                        BackgroundService.changeSong(song.getSongUri(), song.getAuthorName(), song.getSongName(), song.getSongPreview(), activity);
                        t.showMiniMediaFragment(song.getSongUri(), song.getAuthorName(), song.getSongName(),song.getSongPreview(), activity);
                        mSongUri = song.getSongUri();
                    } else if(mSongUri == song.getSongUri()) {
                        t.showMediaFragment(song.getSongUri(), song.getAuthorName(), song.getSongName(),song.getSongPreview(), activity);
                    }

                //При первом нажатии на песню
                } else  if(BackgroundService.mMediaPlayer == null){
                    BackgroundService.setSong(song.getSongUri(), song.getAuthorName(), song.getSongName(), song.getSongPreview(), activity);
                    mSongUri = song.getSongUri();
                    t.showMiniMediaFragment(song.getSongUri(), song.getAuthorName(), song.getSongName(),song.getSongPreview(), activity);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return Database.Array.length;

    }



}


/*

    if(mDatabase.alreadyExists(song.getSongUri())) {
                    Snackbar.make(holder.itemView, "Данная песня уже у вас в плейлисте", Snackbar.LENGTH_SHORT).show();
                } else {
                    mDatabase.addSong(new UserLibSong(0, song.getAuthorName(), song.getSongName(), song.getSongUri(), song.getSongPreview()));
                    //Snackbar.make(holder.itemView, "Песня добавлена в ваш плейлист", Snackbar.LENGTH_SHORT).show();
                }

 */