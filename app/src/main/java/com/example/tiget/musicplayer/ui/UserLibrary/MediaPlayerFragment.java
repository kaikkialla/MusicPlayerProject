package com.example.tiget.musicplayer.ui.UserLibrary;


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
import com.example.tiget.musicplayer.ui.BackgroundService;
import com.example.tiget.musicplayer.ui.MusicFragment;
import com.example.tiget.musicplayer.ui.t;


public class MediaPlayerFragment extends Fragment {
    Context context;
    ImageView playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;

    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    TextView SongNameTextView;
    TextView AuthorNameTextView;
    ImageView SongPreview;


    String mSongName;
    String mAuthorName;
    int mPreviewImageResId;

    ImageView backBtn;


    public static MediaPlayerFragment newInstance(UserLibSong constructor) {
        MediaPlayerFragment fragnent = new MediaPlayerFragment();
        Bundle argument = new Bundle();
        argument.putSerializable("Constructor", constructor);
        fragnent.setArguments(argument);
        return fragnent;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        //создаем главную вьюшку
        View view = inflater.inflate(R.layout.media_player_fragment, container, false);
        //настраиваем
        final UserLibSong song = (UserLibSong) getArguments().getSerializable("Constructor");

        backBtn = view.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //PlaylistFragment recyclerViewFragment = PlaylistFragment.newInstance(song);
                //MediaPlayerFragment mediaPlayerFragment = MediaPlayerFragment.newInstance(song);
                //MiniMediaPlayerFragment miniMediaPlayer = MiniMediaPlayerFragment.newInstance(song);



                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new MusicFragment()).commit();
                t.showMiniMediaFragment(song, getActivity());
            }
        });



        playBtn = view.findViewById(R.id.playBtnClick);
        positionBar = view.findViewById(R.id.positionBar);
        volumeBar = view.findViewById(R.id.volumeBar);
        elapsedTimeLabel = view.findViewById(R.id.elapsedTimaLable);
        remainingTimeLabel = view.findViewById(R.id.remainingTimeLabel);
        AuthorNameTextView = view.findViewById(R.id.AuthorNameTextView);
        SongNameTextView = view.findViewById(R.id.SongNameTextView);
        SongPreview = view.findViewById(R.id.SongImage);


        mSongName = song.SongName;
        mAuthorName = song.AuthorName;
        mPreviewImageResId = song.SongPreview;

        SongNameTextView.setText(mSongName);
        AuthorNameTextView.setText(mAuthorName);
        SongPreview.setImageResource(mPreviewImageResId);


        //Настраиваем Position Bar
        positionBar.setMax(UserLibAdapter.totalTime);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    BackgroundService.mMediaPlayer.seekTo(i);
                    BackgroundService.mMediaPlayer.start();
                    positionBar.setProgress(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        //Настраиваем Volume Bar
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        //Кнопка паузы
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.checkPlayButtonPressedState(context, playBtn);

            }
        });


        //Постоянно получаем текущее время трека, кладем в Message и отпрявляем в Handler
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
        t.checkPlayButtonState(context, playBtn);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    //Устанавливаем текущее и оставшееся время
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            //Updating Position Bar
            positionBar.setProgress(currentPosition);

            //Update Labels
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(UserLibAdapter.totalTime - currentPosition);
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

