package com.example.tiget.musicplayer.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.tiget.musicplayer.R;
import com.example.tiget.musicplayer.ui.UserLibrary.UserLibAdapter;
import com.example.tiget.musicplayer.ui.UserLibrary.UserLibSong;


public class MediaPlayerFragment extends Fragment {
    Context context;

    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    TextView SongNameTextView;
    TextView AuthorNameTextView;
    ImageView mSongPreview;


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


    public static MediaPlayerFragment newInstance(String SongUri, String AuthorName, String SongName, int ResId) {
        MediaPlayerFragment fragment = new MediaPlayerFragment();

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.media_player_fragment, container, false);
        //final UserLibSong song = (UserLibSong) getArguments().getSerializable("Constructor");

        mBackButton = view.findViewById(R.id.backBtn);
        mPauseButton = view.findViewById(R.id.pauseBtn);
        mRewindButton = view.findViewById(R.id.rewindBtn);
        mForwardButton = view.findViewById(R.id.forwardBtn);

        mTimeline = view.findViewById(R.id.positionBar);
        mVolumeBar = view.findViewById(R.id.volumeBar);

        elapsedTimeLabel = view.findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = view.findViewById(R.id.remainingTimeLabel);

        mSongPreview = view.findViewById(R.id.SongImage);
        AuthorNameTextView = view.findViewById(R.id.AuthorNameTextView);
        SongNameTextView = view.findViewById(R.id.SongNameTextView);



        SongNameTextView.setText(mSongName);
        AuthorNameTextView.setText(mAuthorName);
        mSongPreview.setImageResource(mResId);



        mTimeline.setMax(UserLibAdapter.mTotalTime);
        mTimeline.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    BackgroundService.mMediaPlayer.seekTo(i);
                    BackgroundService.mMediaPlayer.start();
                    mTimeline.setProgress(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
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
                BackgroundService.changeSong(UserLibAdapter.nextSong.getSongUri(), UserLibAdapter.nextSong.getAuthorName(),UserLibAdapter.nextSong.getSongName(), UserLibAdapter.nextSong.getSongPreview(),  context);
            }
        });


        mRewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundService.changeSong(UserLibAdapter.previousSong.getSongUri(), UserLibAdapter.previousSong.getAuthorName(),UserLibAdapter.previousSong.getSongName(), UserLibAdapter.previousSong.getSongPreview(),  context);
            }
        });


        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new MusicFragment()).commit();
                t.showMiniMediaFragment(mSongUri, mAuthorName, mSongName, mResId, getActivity());
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
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        t.checkPlayButtonState(context, mPauseButton, 0);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    //Устанавливаем текущее и оставшееся время
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            //Updating Position Bar
            mTimeline.setProgress(currentPosition);

            //Update Labels
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(UserLibAdapter.mTotalTime - currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };


    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;
        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }
}

