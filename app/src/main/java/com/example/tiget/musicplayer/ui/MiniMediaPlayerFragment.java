package com.example.tiget.musicplayer.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tiget.musicplayer.R;
import com.example.tiget.musicplayer.ui.UserLibrary.UserLibAdapter;
import com.example.tiget.musicplayer.ui.UserLibrary.UserLibSong;


public class MiniMediaPlayerFragment extends Fragment {

    public static ImageView mPauseButton;
    TextView SongNameTextView;
    ImageView SongImage;
    LinearLayout mainContainer;
    static Context context;
    ImageView mForwardButton;



    private static String mSongUri;
    private static String mAuthorName;
    private static String mSongName;
    private static int mResId;




    public static MiniMediaPlayerFragment newInstance(String SongUri, String AuthorName, String SongName, int ResId) {
        MiniMediaPlayerFragment fragment = new MiniMediaPlayerFragment();
        mSongUri = SongUri;
        mAuthorName = AuthorName;
        mSongName = SongName;
        mResId = ResId;

        //Bundle argument = new Bundle();
        //argument.putSerializable("Constructor", constructor);
        //fragment.setArguments(argument);
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
        SongNameTextView = view.findViewById(R.id.mini_song_name);
        mainContainer = view.findViewById(R.id.mini_song_info_container);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SongNameTextView.setText(mSongName);
        SongImage.setImageResource(mResId);




        //Кнопка паузы
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.checkPlayButtonPressedState(context, mPauseButton, 1);

            }
        });


        //По нажатию на тело фрагмента, открываем большой MediaPlayer
        mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayerFragment fragment = MediaPlayerFragment.newInstance(mSongUri, mAuthorName, mSongName, mResId);
                t.showMediaFragment(mSongUri, mAuthorName, mSongName, mResId, getActivity());
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, fragment).commit();
            }
        });

        mForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundService.changeSong(UserLibAdapter.nextSong.getSongUri(), context);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        t.checkPlayButtonState(context, mPauseButton, 1 );
    }







}

