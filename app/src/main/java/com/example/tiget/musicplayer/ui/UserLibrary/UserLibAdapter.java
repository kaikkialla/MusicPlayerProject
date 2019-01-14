package com.example.tiget.musicplayer.ui.UserLibrary;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tiget.musicplayer.R;
import com.example.tiget.musicplayer.ui.BackgroundService;
import com.example.tiget.musicplayer.ui.MainActivity;
import com.example.tiget.musicplayer.ui.Song;
import com.example.tiget.musicplayer.ui.t;
import java.util.ArrayList;
import java.util.List;


public class UserLibAdapter extends RecyclerView.Adapter<ViewHolder> {

    MainActivity activity;


    public static Song nextSong;
    public static Song previousSong;

    public static List<Song> mUserLibSongs = new ArrayList<>();

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

        final Song song = mUserLibSongs.get(position);
        mSongUri = song.getSongUri();
        mAuthorName = song.getAuthorName();
        mSongName = song.getSongName();
        mResId = song.getSongPreview();



        holder.SongName.setText(mSongName);
        holder.AuthorName.setText(mAuthorName);



        //if(holder.SongImage.getDrawable() != null) {
        holder.SongImage.setImageResource(mUserLibSongs.get(position).getSongPreview());
        //} else if(holder.SongImage.getDrawable() == null) {
        //holder.SongImage.setImageResource(R.drawable.no_image_loaded);
        //}



        if(position > 0) {
            previousSong = mUserLibSongs.get(position - 1);
        }


        if(position < mUserLibSongs.size() - 1) {
            nextSong = mUserLibSongs.get(position + 1);
        }




/**
 *  При открытии MediaPlayer'а, закрытии и нажатии на ту же самую песню
 */

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                //Если что-то уже играет
                if(BackgroundService.mMediaPlayer != null) {

                    if(mSongUri != song.SongUri) {
                        BackgroundService.changeSong(mUserLibSongs, position, activity);
                        t.showMiniMediaFragment(mUserLibSongs, position, activity, t.mUserLibraryFragmentTag);
                        mSongUri = song.SongUri;
                    } else if(mSongUri == song.SongUri) {
                        t.showMediaFragment(mUserLibSongs, position, activity, t.mUserLibraryFragmentTag);
                    }

                    //При первом нажатии на песню
                } else  if(BackgroundService.mMediaPlayer == null){

                    BackgroundService.setSong(mUserLibSongs, position, activity);
                    mSongUri = song.SongUri;
                    //activity.startService(new Intent(activity, BackgroundService.class));
                    t.showMiniMediaFragment(mUserLibSongs, position, activity, t.mUserLibraryFragmentTag);

                }
            }

        });



        holder.SongInfo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                t.showSongInfoFragment(song.getSongUri(), song.getAuthorName(), song.getSongName(), song.getSongPreview(), activity);
            }
        });





    }


    public void swap(List<Song> songs) {
        if (songs != null) {
            mUserLibSongs.clear();
            mUserLibSongs.addAll(songs);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mUserLibSongs.size();

    }
}
