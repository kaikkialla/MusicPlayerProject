package com.example.tiget.musicplayer.ui.Library;


import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tiget.musicplayer.R;
import com.example.tiget.musicplayer.ui.BackgroundService;
import com.example.tiget.musicplayer.ui.MainActivity;
import com.example.tiget.musicplayer.ui.UserLibrary.MediaPlayerFragment;
import com.example.tiget.musicplayer.ui.UserLibrary.MiniMediaPlayerFragment;
import com.example.tiget.musicplayer.ui.UserLibrary.UserLibDatabase;
import com.example.tiget.musicplayer.ui.UserLibrary.UserLibSong;
import com.example.tiget.musicplayer.ui.t;

import java.util.ArrayList;
import java.util.List;



public class LibraryAdapter extends RecyclerView.Adapter<ViewHolder> {

    MainActivity activity;
    public static String mSongUri;
    public List<LibSong> mSong = new ArrayList<>();
    UserLibDatabase mDatabase;


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
        final UserLibSong song = Database.Arr[position];

        holder.SongName.setText(song.SongName);
        holder.AuthorName.setText(song.AuthorName);
        holder.SongPreview.setImageResource(song.SongPreview);
        Log.v("SONGPREVIEWID", "LIB: " + song.SongName + " / " + song.SongPreview);


        mDatabase = new UserLibDatabase(activity);

        holder.SongInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDatabase.alreadyExists(song.getSongUri())) {
                    Snackbar.make(holder.itemView, "Данная песня уже у вас в плейлисте", Snackbar.LENGTH_SHORT).show();
                } else {
                    mDatabase.addSong(new UserLibSong(mSong.size(), song.getAuthorName(), song.getSongName(), song.getSongUri(), song.getSongPreview()));
                    //Snackbar.make(holder.itemView, "Песня добавлена в ваш плейлист", Snackbar.LENGTH_SHORT).show();
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Если что-то уже играет
                if(BackgroundService.mMediaPlayer != null) {
                    if(mSongUri != song.getSongUri()) {
                        BackgroundService.changeSong(activity, song.getSongUri());
                        t.showMiniMediaFragment(song, activity);
                        mSongUri = song.getSongUri();
                    } else if(mSongUri == song.getSongUri()) {
                        t.showMediaFragment(song, activity);
                    }

                //При первом нажатии на песню
                } else  if(BackgroundService.mMediaPlayer == null){
                    holder.previewSongController.setVisibility(View.GONE);
                    BackgroundService.setSong(activity, song.getSongUri());
                    mSongUri = song.getSongUri();
                    t.showMiniMediaFragment(song, activity);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return Database.Arr.length;

    }


}
