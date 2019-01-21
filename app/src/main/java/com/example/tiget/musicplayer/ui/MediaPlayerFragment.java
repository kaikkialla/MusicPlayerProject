package com.example.tiget.musicplayer.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.tiget.musicplayer.R;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


import static com.example.tiget.musicplayer.ui.BackgroundService.songs;
import static com.example.tiget.musicplayer.ui.BackgroundService.pos;

import static com.example.tiget.musicplayer.ui.MainActivity.MARGIN_LEFT_PX;
import static com.example.tiget.musicplayer.ui.MainActivity.MARGIN_RIGHT_PX;
import static com.example.tiget.musicplayer.ui.MainActivity.MARGIN_TOP_PX;


import static com.example.tiget.musicplayer.ui.MainActivity.SCREEN_WIDTH_PX;

import static com.example.tiget.musicplayer.ui.MainActivity.PRESSED_ALPHA;
import static com.example.tiget.musicplayer.ui.MainActivity.DEFAULT_ALPHA;


public class MediaPlayerFragment extends Fragment {
    FragmentActivity activity;
    View view;

    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    TextView SongName;
    TextView AuthorName;
    static RoundedImageView SongImage;
    LinearLayout positionBarLayout;

    ImageView mBackButton;
    public static ImageView mPauseButton;
    ImageView mForwardButton;
    ImageView mRewindButton;

    SeekBar mTimeline;
    SeekBar mVolumeBar;
    private static String mSongUri;
    private static String mAuthorName;
    private static String mSongName;
    private static int mResId;

    //public static int mPos;
    //public static List<Song> mSongs;

    int time;
    int duration;

    public static MediaPlayerFragment newInstance(List<Song> a, int b) {
        MediaPlayerFragment fragment = new MediaPlayerFragment();
        songs = a;
        pos = b;
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            view = inflater.inflate(R.layout.media_player_landscape, container, false);

        } else if (orientation == Configuration.ORIENTATION_PORTRAIT){
            view = inflater.inflate(R.layout.media_player_portrait, container, false);
        }
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();


        mBackButton = view.findViewById(R.id.backBtn);
        mPauseButton = view.findViewById(R.id.pauseBtn);
        mRewindButton = view.findViewById(R.id.rewindBtn);
        mForwardButton = view.findViewById(R.id.forwardBtn);

        mTimeline = view.findViewById(R.id.positionBar);
        mVolumeBar = view.findViewById(R.id.volumeBar);

        positionBarLayout = view.findViewById(R.id.positionBarLayout);
        elapsedTimeLabel = view.findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = view.findViewById(R.id.remainingTimeLabel);

        SongImage = view.findViewById(R.id.SongImage);
        AuthorName = view.findViewById(R.id.AuthorNameTextView);
        SongName = view.findViewById(R.id.SongNameTextView);



        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            setImageSize(Configuration.ORIENTATION_LANDSCAPE);
            setTimelineSize(Configuration.ORIENTATION_LANDSCAPE);

        } else if (orientation == Configuration.ORIENTATION_PORTRAIT){

            setImageSize(Configuration.ORIENTATION_PORTRAIT);
            setTimelineSize(Configuration.ORIENTATION_PORTRAIT);

    }



        mSongUri = songs.get(pos).getSongUri();
        mAuthorName = songs.get(pos).getAuthorName();
        mSongName = songs.get(pos).getSongName();
        mResId = songs.get(pos).getSongPreview();


        if(pos == songs.size() - 1) {
            mForwardButton.setClickable(false);
            mForwardButton.setAlpha(PRESSED_ALPHA);
        } else if(pos != songs.size() - 1){
            mForwardButton.setClickable(true);
            mForwardButton.setAlpha(DEFAULT_ALPHA);
        }

        if(pos == 0) {
            mRewindButton.setClickable(false);
            mRewindButton.setAlpha(PRESSED_ALPHA);
        } else if(pos != 0){
            mRewindButton.setClickable(true);
            mRewindButton.setAlpha(DEFAULT_ALPHA);
        }


        changeInfo();
        t.checkPlayButtonState(activity, mPauseButton, 0);

        mTimeline.setMax(BackgroundService.mMediaPlayer.getDuration() / 1000);


        mTimeline.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mTimeline.setProgress(i);
                    BackgroundService.seekTo(i * 1000, activity);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });


        mVolumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float volumeNum = i / 100f;
                BackgroundService.mMediaPlayer.setVolume(volumeNum, volumeNum);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.checkPlayButtonPressedState(activity, mPauseButton, 0);
            }
        });


        mForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundService.setNextSong(songs, pos, activity);
                //changeInfo();
            }
        });


        mRewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundService.setPreviousSongSong(songs, pos, activity);
                //changeInfo();

            }
        });


        mBackButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                t.showFragment(new MusicFragment(), getActivity(), "TAG");
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.f, new MusicFragment()).commit();
                t.showMiniMediaFragment(songs, pos, getActivity(), t.mMediaPlayerFragmentTag);
            }
        });

/*
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (BackgroundService.mMediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = time;
                        mHandler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }).start();
        */
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onTimeChangeEvent(BackgroundService.TimeChangedEvent event) {
        time = event.time;
        duration = event.duration;

        mTimeline.setProgress(time);

        //Update Labels
        String elapsedTime = createTimeLabel(time);
        elapsedTimeLabel.setText(elapsedTime);

        String remainingTime = createTimeLabel(duration - time);
        remainingTimeLabel.setText("- " + remainingTime);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onSongChangeEvent(BackgroundService.SongChangedEvent event) {
        if(pos == songs.size() - 1) {
            mForwardButton.setClickable(false);
            mForwardButton.setAlpha(PRESSED_ALPHA);
        } else if(pos != songs.size() - 1){
            mForwardButton.setClickable(true);
            mForwardButton.setAlpha(DEFAULT_ALPHA);
        }

        if(pos == 0) {
            mRewindButton.setClickable(false);
            mRewindButton.setAlpha(PRESSED_ALPHA);
        } else if(pos != 0){
            mRewindButton.setClickable(true);
            mRewindButton.setAlpha(DEFAULT_ALPHA);
        }

        changeInfo();
    }


/*
    //Устанавливаем текущее и оставшееся время
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {




            if(msg.what != duration) {
                mTimeline.setProgress(msg.what);

                //Update Labels
                String elapsedTime = createTimeLabel(msg.what);
                elapsedTimeLabel.setText(elapsedTime);

                String remainingTime = createTimeLabel(duration - msg.what);
                remainingTimeLabel.setText("- " + remainingTime);
            }
        }
    };
*/

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 60;
        int sec = time % 60;
        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }


    public void setImageSize(int orientation) {
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SCREEN_WIDTH_PX - MARGIN_LEFT_PX - MARGIN_RIGHT_PX, SCREEN_WIDTH_PX - MARGIN_LEFT_PX - MARGIN_RIGHT_PX);
            params.setMargins(MARGIN_LEFT_PX,0,MARGIN_RIGHT_PX,0);
            SongImage.setLayoutParams(params);

        } else if(orientation == Configuration.ORIENTATION_LANDSCAPE) {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SCREEN_WIDTH_PX / 2 - MARGIN_LEFT_PX - MARGIN_RIGHT_PX, SCREEN_WIDTH_PX / 2 - MARGIN_LEFT_PX - MARGIN_RIGHT_PX);
            params.setMargins(MARGIN_LEFT_PX,0,MARGIN_RIGHT_PX,0);
            SongImage.setLayoutParams(params);

        }
    }



    public void setTimelineSize(int orientation) {
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SCREEN_WIDTH_PX - MARGIN_LEFT_PX - MARGIN_RIGHT_PX, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(MARGIN_LEFT_PX, MARGIN_TOP_PX, MARGIN_RIGHT_PX,0);
            positionBarLayout.setLayoutParams(params);

        } else if(orientation == Configuration.ORIENTATION_LANDSCAPE) {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SCREEN_WIDTH_PX / 2 - MARGIN_LEFT_PX - MARGIN_RIGHT_PX, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(MARGIN_LEFT_PX,0,MARGIN_RIGHT_PX,0);
            positionBarLayout.setLayoutParams(params);

        }
    }


//При смене ориентации меняем расположение элементов
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation=newConfig.orientation;

        switch(orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
//to do something
                getActivity().setContentView(R.layout.media_player_landscape);

                break;
            case Configuration.ORIENTATION_PORTRAIT:
//to do something
                getActivity().setContentView(R.layout.media_player_portrait);

                break;

        }
    }


    public void changeInfo() {
        SongName.setText(songs.get(pos).getSongName());
        AuthorName.setText(songs.get(pos).getAuthorName());
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


    /*
    @Override
    public void onResume() {
        super.onResume();
        t.checkPlayButtonState(context, mPauseButton, 0);
    }
    */
}

