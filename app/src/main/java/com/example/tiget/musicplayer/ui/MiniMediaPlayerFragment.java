package com.example.tiget.musicplayer.ui;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tiget.musicplayer.R;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


import static com.example.tiget.musicplayer.ui.BackgroundService.pos;
import static com.example.tiget.musicplayer.ui.BackgroundService.songs;
import static com.example.tiget.musicplayer.ui.MainActivity.DEFAULT_ALPHA;
import static com.example.tiget.musicplayer.ui.MainActivity.PRESSED_ALPHA;


public class MiniMediaPlayerFragment extends Fragment {
    Context context;
    FragmentActivity activity;

    LinearLayout mainContainer;

    static RoundedImageView SongImage;
    static TextView SongName;
    public static ImageView mPauseButton;
    ImageView mForwardButton;





    private static String mSongUri;
    private static String mAuthorName;
    private static String mSongName;
    private static int mResId;

    //public static int mPos;
    //private static List<Song> mSongs;


    public static MiniMediaPlayerFragment newInstance(List<Song> a, int b) {
        MiniMediaPlayerFragment fragment = new MiniMediaPlayerFragment();
        songs = a;
        pos = b;
        return fragment;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mini_media_player_fragment, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();


        mPauseButton = view.findViewById(R.id.mini_play_btn);
        mForwardButton = view.findViewById(R.id.mini_forward);
        SongImage = view.findViewById(R.id.mini_preview);
        SongName = view.findViewById(R.id.mini_song_name);
        mainContainer = view.findViewById(R.id.mini_song_info_container);



        mSongUri = songs.get(pos).getSongUri();
        mAuthorName = songs.get(pos).getAuthorName();
        mSongName = songs.get(pos).getSongName();
        mResId = songs.get(pos).getSongPreview();

        if(pos == songs.size() - 1) {
            mForwardButton.setClickable(false);
            mForwardButton.setAlpha(PRESSED_ALPHA);
            //mForwardButton.setImageDrawable(context.getResources().getDrawable(R.drawable.));
        } else if(pos != songs.size() - 1){
            mForwardButton.setClickable(true);
            mForwardButton.setAlpha(DEFAULT_ALPHA);
            //mForwardButton.setImageDrawable(context.getResources().getDrawable(R.drawable.next_song_gray));
        }

        changeInfo();
        t.checkPlayButtonState(activity, mPauseButton, 1);

/*
        BackgroundService.setChangeListener(new BackgroundService.ChangeListener() {
            @Override
            public void onChange(boolean isPlaying) {
                Log.e("fkjs0igfsw", "changed");
                t.checkPlayButtonState(context, mPauseButton, 1);
            }
        });
*/

        //Кнопка паузы
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.checkPlayButtonPressedState(activity, mPauseButton, 1);
            }
        });


        //По нажатию на тело фрагмента, открываем большой MediaPlayer
        mainContainer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                t.showMediaFragment(songs, pos, activity, t.mMiniMediaPlayerFragmentTag);
            }
        });


        mForwardButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                BackgroundService.setNextSong(songs, pos, activity);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onSongChangeEvent(BackgroundService.SongChangedEvent event) {
        if(pos == songs.size() - 1) {
            mForwardButton.setClickable(false);
            mForwardButton.setAlpha(PRESSED_ALPHA);
            //mForwardButton.setImageDrawable(context.getResources().getDrawable(R.drawable.));
        } else if(pos != songs.size() - 1){
            mForwardButton.setClickable(true);
            mForwardButton.setAlpha(DEFAULT_ALPHA);
            //mForwardButton.setImageDrawable(context.getResources().getDrawable(R.drawable.next_song_gray));
        }


        changeInfo();
    }


/*
    @Override
    public void onResume() {
        super.onResume();
        t.checkPlayButtonState(context, mPauseButton, 1 );
    }
*/



    void changeInfo() {
        SongName.setText(songs.get(pos).getSongName());
        SongImage.setImageResource(songs.get(pos).getSongPreview());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


}

