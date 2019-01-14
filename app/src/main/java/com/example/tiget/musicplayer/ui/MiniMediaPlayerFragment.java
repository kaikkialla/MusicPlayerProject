package com.example.tiget.musicplayer.ui;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tiget.musicplayer.R;
import com.example.tiget.musicplayer.ui.UserLibrary.UserLibAdapter;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;


public class MiniMediaPlayerFragment extends Fragment {
    Context context;

    LinearLayout mainContainer;
    RoundedImageView SongImage;
    TextView SongName;
    public static ImageView mPauseButton;
    ImageView mForwardButton;



    private static String mSongUri;
    private static String mAuthorName;
    private static String mSongName;
    private static int mResId;

    private static int mPos;
    private static List<Song> mSongs;


    public static MiniMediaPlayerFragment newInstance(List<Song> songs, int pos) {
        MiniMediaPlayerFragment fragment = new MiniMediaPlayerFragment();

        mSongs = songs;
        mPos = pos;

        return fragment;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.mini_media_player_fragment, container, false);

        mPauseButton = view.findViewById(R.id.mini_play_btn);
        mForwardButton = view.findViewById(R.id.mini_forward);
        SongImage = view.findViewById(R.id.mini_preview);
        SongName = view.findViewById(R.id.mini_song_name);
        mainContainer = view.findViewById(R.id.mini_song_info_container);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mSongUri = mSongs.get(mPos).getSongUri();
        mAuthorName = mSongs.get(mPos).getAuthorName();
        mSongName = mSongs.get(mPos).getSongName();
        mResId = mSongs.get(mPos).getSongPreview();

        SongName.setText(mSongName);
        if(mResId != 0) {
            SongImage.setImageResource(mResId);
        } else if(mResId == 0) {
            SongImage.setImageResource(R.drawable.no_image_loaded);
        }





        //Кнопка паузы
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.checkPlayButtonPressedState(context, mPauseButton, 1);
            }
        });


        //По нажатию на тело фрагмента, открываем большой MediaPlayer
        mainContainer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                //MediaPlayerFragment fragment = MediaPlayerFragment.newInstance(mSongUri, mAuthorName, mSongName, mResId);
                t.showMediaFragment(mSongs, mPos, getActivity(), t.mMiniMediaPlayerFragmentTag);
            }
        });


        mForwardButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                //BackgroundService.changeSong(mSongs, mPos + 1,  context);
                BackgroundService.setNextSong(mSongs, mPos, context);
                mPos = mPos + 1;

                SongName.setText(mSongs.get(mPos).getSongName());
                if(SongImage.getDrawable() != null) {
                    SongImage.setImageResource(mSongs.get(mPos).getSongPreview());
                } else if(SongImage.getDrawable() == null) {
                    SongImage.setImageResource(R.drawable.no_image_loaded);
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        t.checkPlayButtonState(context, mPauseButton, 1 );
    }
}

