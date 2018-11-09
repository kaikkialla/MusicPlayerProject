package com.example.tiget.musicplayer;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


public class MediaPlayerFragment extends Fragment {

    Button playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;

    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    TextView SongNameTextView;
    TextView AuthorNameTextView;
    ImageView SongImage;


    //int totalTime;

    ImageView backBtn;

    public static MediaPlayerFragment newInstance(Constructor constructor) {
        MediaPlayerFragment fragnent = new MediaPlayerFragment();
        Bundle argument = new Bundle();
        argument.putSerializable("Constructor", constructor);
        fragnent.setArguments(argument);
        return fragnent;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context context = getContext();
        //создаем главную вьюшку
        View view = inflater.inflate(R.layout.media_player_fragment, container, false);
        //настраиваем
        final Constructor constructor = (Constructor) getArguments().getSerializable("Constructor");

        backBtn = view.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //RecyclerViewAdapter.mMediaPlayer.stop();

                RecyclerViewFragment recyclerViewFragment = RecyclerViewFragment.newInstance(constructor);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, recyclerViewFragment).commit();

                MiniMediaPlayerFragment miniMediaPlayer = MiniMediaPlayerFragment.newInstance(constructor);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.miniFrameLayout, miniMediaPlayer).commit();

            }
        });


        playBtn = view.findViewById(R.id.playBtnClick);
        positionBar = view.findViewById(R.id.positionBar);
        volumeBar = view.findViewById(R.id.volumeBar);
        elapsedTimeLabel = view.findViewById(R.id.elapsedTimaLable);
        remainingTimeLabel = view.findViewById(R.id.remainingTimeLabel);
        AuthorNameTextView = view.findViewById(R.id.AuthorNameTextView);
        SongNameTextView = view.findViewById(R.id.SongNameTextView);
        SongImage = view.findViewById(R.id.SongImage);


        String SongName = constructor.SongName;
        String AuthorName = constructor.AuthorName;
        int SongImageResourseId = constructor.SongPreview;

        SongNameTextView.setText(SongName);
        AuthorNameTextView.setText(AuthorName);
        SongImage.setImageResource(SongImageResourseId);


        //Настраиваем Position Bar
        positionBar.setMax(RecyclerViewAdapter.totalTime);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    RecyclerViewAdapter.mMediaPlayer.seekTo(i);
                    RecyclerViewAdapter.mMediaPlayer.start();
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
                RecyclerViewAdapter.mMediaPlayer.setVolume(volumeNum, volumeNum);
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
                if (RecyclerViewAdapter.mMediaPlayer.isPlaying()) {
                    Log.e("a", "IsPlaying");
                    RecyclerViewAdapter.mMediaPlayer.pause();
                    playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                } else if (!RecyclerViewAdapter.mMediaPlayer.isPlaying()) {
                    Log.e("a", "IsPlaying");
                    RecyclerViewAdapter.mMediaPlayer.start();
                    playBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                }

            }
        });


        //Постоянно получаем текущее время трека, кладем в Message и отпрявляем в Handler
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (RecyclerViewAdapter.mMediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = RecyclerViewAdapter.mMediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();




        return view;

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

            String remainingTime = createTimeLabel(RecyclerViewAdapter.totalTime - currentPosition);
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

