package com.example.tiget.musicplayer.ui.Library;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tiget.musicplayer.R;
import com.example.tiget.musicplayer.ui.BackgroundService;
import com.example.tiget.musicplayer.ui.MainActivity;
import com.example.tiget.musicplayer.ui.Song;
import com.example.tiget.musicplayer.ui.UserLibrary.UserLibDatabase;
import com.example.tiget.musicplayer.ui.t;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.tiget.musicplayer.ui.Library.Database.Array;


public class LibraryAdapter extends RecyclerView.Adapter<ViewHolder> {

    MainActivity activity;
    UserLibDatabase mDatabase;
    public static List<Song> mLibSongs = new ArrayList<>();

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
        final Song song = Array[position];

        /*
        for(Song i : Array) {
            mLibSongs.add(i);
        }
        */

        mLibSongs.addAll(Arrays.asList(Array));

        mSongUri = song.SongUri;
        mAuthorName = song.AuthorName;
        mSongName = song.SongName;
        mResId = song.SongPreview;


        holder.SongName.setText(song.SongName);
        holder.AuthorName.setText(song.AuthorName);


        //if(holder.SongImage.getDrawable() != null) {
        holder.SongImage.setImageResource(mLibSongs.get(position).getSongPreview());
        //} else if(holder.SongImage.getDrawable() == null) {
        //holder.SongImage.setImageResource(R.drawable.no_image_loaded);
        //}


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
                        BackgroundService.changeSong(mLibSongs, position, activity);
                        t.showMiniMediaFragment(mLibSongs, position, activity, t.mLibraryFragmentTag);
                        mSongUri = song.getSongUri();
                    } else if(mSongUri == song.getSongUri()) {
                        t.showMediaFragment(mLibSongs, position, activity, t.mLibraryFragmentTag);
                    }

                    //При первом нажатии на песню
                } else  if(BackgroundService.mMediaPlayer == null){
                    BackgroundService.setSong(mLibSongs, position, activity);
                    mSongUri = song.getSongUri();
                    t.showMiniMediaFragment(mLibSongs, position, activity, t.mLibraryFragmentTag);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return Array.length;

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