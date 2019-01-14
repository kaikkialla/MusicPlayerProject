package com.example.tiget.musicplayer.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.tiget.musicplayer.BuildConfig;
import com.example.tiget.musicplayer.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import static com.example.tiget.musicplayer.ui.MainActivity.MARGIN_LEFT_PX;
import static com.example.tiget.musicplayer.ui.MainActivity.MARGIN_RIGHT_PX;
import static com.example.tiget.musicplayer.ui.MainActivity.SCREEN_HEIGHT_PX;
import static com.example.tiget.musicplayer.ui.MainActivity.SCREEN_WIDTH_PX;


public class MediaPlayerFragment extends Fragment {
    Context context;
    View view;

    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    TextView SongName;
    TextView AuthorName;
    RoundedImageView SongImage;
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

    private static int mPos;
    private static List<Song> mSongs;

    public static MediaPlayerFragment newInstance(List<Song> songs, int pos) {
        MediaPlayerFragment fragment = new MediaPlayerFragment();
        mSongs = songs;
        mPos = pos;
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
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


        mSongUri = mSongs.get(mPos).getSongUri();
        mAuthorName = mSongs.get(mPos).getAuthorName();
        mSongName = mSongs.get(mPos).getSongName();
        mResId = mSongs.get(mPos).getSongPreview();




        SongName.setText(mSongName);
        AuthorName.setText(mAuthorName);

        if(mResId != 0) {
            SongImage.setImageResource(mResId);
        } else if(mResId == 0) {
            SongImage.setImageResource(R.drawable.no_image_loaded);
        }



        mTimeline.setMax(BackgroundService.mMediaPlayer.getDuration() / 1000);
        mTimeline.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mTimeline.setProgress(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                BackgroundService.seekTo(seekBar.getProgress(), context);
                Log.e("jsgs", String.valueOf(seekBar.getProgress()));
            }
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
                t.checkPlayButtonPressedState(context, mPauseButton, 0);

            }
        });


        mForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //BackgroundService.changeSong(mSongs, mPos + 1, context);
                BackgroundService.setNextSong(mSongs, mPos, context);
                mPos = mPos + 1;

                changeInfo();
            }
        });


        mRewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                BackgroundService.changeSong(mSongs, mPos - 1,  context);
                */
                BackgroundService.setPreviousSongSong(mSongs, mPos, context);
                mPos = mPos - 1;

                changeInfo();

            }
        });


        mBackButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.f, new MusicFragment()).commit();
                t.showMiniMediaFragment(mSongs, mPos, getActivity(), t.mMediaPlayerFragmentTag);
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (BackgroundService.mMediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = BackgroundService.mMediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }).start();
    }



    //Устанавливаем текущее и оставшееся время
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what / 1000;
            int duration = BackgroundService.mMediaPlayer.getDuration() / 1000;

        //Updating Position Bar


        Log.e("gsgmsgmsogus", duration + " / " + currentPosition + " / " + (duration - currentPosition));

        if(currentPosition != duration) {
            mTimeline.setProgress(currentPosition);

            //Update Labels
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(duration - currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);

        } else if(currentPosition == duration) {
            BackgroundService.setNextSong(mSongs, mPos, context);
            mPos = mPos + 1;



        }

        }
    };


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
            params.setMargins(MARGIN_LEFT_PX, MARGIN_LEFT_PX, MARGIN_RIGHT_PX,0);
            positionBarLayout.setLayoutParams(params);

        } else if(orientation == Configuration.ORIENTATION_LANDSCAPE) {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SCREEN_WIDTH_PX / 2 - MARGIN_LEFT_PX - MARGIN_RIGHT_PX, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(MARGIN_LEFT_PX,0,MARGIN_RIGHT_PX,0);
            positionBarLayout.setLayoutParams(params);

        }
    }



    @Override
    public void onResume() {
        super.onResume();
        t.checkPlayButtonState(context, mPauseButton, 0);
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
        SongName.setText(mSongs.get(mPos).getSongName());
        if(SongImage.getDrawable() != null) {
            SongImage.setImageResource(mSongs.get(mPos).getSongPreview());
        } else if(SongImage.getDrawable() == null) {
            SongImage.setImageResource(R.drawable.no_image_loaded);
        }
    }
}

