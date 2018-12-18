package com.example.tiget.musicplayer.ui.Library;



import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.tiget.musicplayer.R;
import com.example.tiget.musicplayer.ui.MainActivity;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class PlaylistFragment extends Fragment {


    public static PlaylistFragment newInstance(SongConstructor constructor) {
        PlaylistFragment fragnent = new PlaylistFragment();
        return fragnent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = getContext();
        //создаем главную вьюшку
        View view = inflater.inflate(R.layout.recycler_view_fragment_layout, container, false);


        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter((MainActivity) getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        //настраиваем



        //возвращаем
        return view;

    }



}



class RecyclerViewAdapter extends RecyclerView.Adapter<PlaylistViewHolder> {

    MainActivity activity;
    public static String SongUri;
    public static MediaPlayer SongLenghtMediaPlayer;
    public static int totalTime;
    Context context;
    ProgressBar progressBar;
    int currentPos = -1;//Текущая песня

    public static final String ACTION_PLAY  = "ACTION_PLAY";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";


    public RecyclerViewAdapter(MainActivity activity) {
        this.activity = activity;
    }


    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.song_item, parent, false );
        PlaylistViewHolder vh = new PlaylistViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(final PlaylistViewHolder holder, final int position) {
        context = this.context;

        final SongConstructor  constructor = PlaylistDatabase.Arr[position];
        holder.SongName.setText(constructor.SongName);
        holder.AuthorName.setText(constructor.AuthorName);
        holder.SongPreview.setImageResource(constructor.SongPreview);

        SongUri = constructor.SongUri;
        holder.previewSongController.setVisibility(View.GONE);



/*Из-за этого лагает, надо чем-нибудь заменить
        SongLenghtMediaPlayer = MediaPlayer.create(activity, Uri.parse(SongUri));//Создаем MediaPlayer для получения длины песни(позже им не пользуемся)
        totalTime = SongLenghtMediaPlayer.getDuration();//Получаем длину песни
        //Конвертируем милисекунды(длину песни) в минуты и секунды
        String timeLabel = "";
        int min = totalTime / 1000 / 60;
        int sec = totalTime / 1000 % 60;
        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";//Если секунд <10, то добавляем перед секундами 0(5 секнуд - 05 в записи)
        timeLabel += sec;
        holder.SongLenght.setText(String.valueOf(timeLabel));
        SongLenghtMediaPlayer = null;//Удаляем MediaPlayer для получения длины песни
*/


        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Если что-то уже играет
                if(BackgroundService.mMediaPlayer != null) {

                    if(SongUri != constructor.SongUri) {

                        BackgroundService.mMediaPlayer.reset();//Сбрасываем текущую песню
                        try {
                            BackgroundService.mMediaPlayer.setDataSource(activity, Uri.parse(constructor.SongUri));//Включаем ту, на которую нажали
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            BackgroundService.mMediaPlayer.prepare();//Что-то делаем, не знаю что, но надо
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        BackgroundService.mMediaPlayer.start();//Запускаем
                        showMiniMediaFragment(constructor);
                        SongUri = constructor.SongUri;
                    } else if(SongUri == constructor.SongUri) {
                        showMediaFragment(constructor);
                    }

                //При первом нажатии на песню
                } else  if(BackgroundService.mMediaPlayer == null){

                    holder.previewSongController.setVisibility(View.GONE);

                    SongUri = constructor.SongUri;
                    activity.startService(new Intent(activity, BackgroundService.class));
                    showMiniMediaFragment(constructor);

                }
            }

        });




/*
        //Нажатие на тело песни(без превьюшки)
        holder.mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BackgroundService.mMediaPlayer == null) {
                    ShowMiniMediaFragment(constructor);
                    currentPos = position;

                } else if(BackgroundService.mMediaPlayer != null) {

                    //Если другой элемент, то меняем песню и показываем минифрагмент
                    if(SongUri != constructor.SongUri) {

                        BackgroundService.mMediaPlayer.reset();//Сбрасываем текущую песню
                        try {
                            BackgroundService.mMediaPlayer.setDataSource(activity, Uri.parse(constructor.SongUri));//Включаем ту, на которую нажали
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            BackgroundService.mMediaPlayer.prepare();//Что-то делаем, не знаю что, но надо
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        BackgroundService.mMediaPlayer.start();//Запускаем
                        ShowMiniMediaFragment(constructor);//Показываем мини фрамент

                        currentPos = position;
                    }

                    //Если тот-же самый
                    else if(SongUri == constructor.SongUri) {
                        if (BackgroundService.mMediaPlayer.isPlaying()) {
                            showMediaFragment(constructor);
                        } else if (!BackgroundService.mMediaPlayer.isPlaying()) {
                            //Если во время клика стоит на паузе, то...
                            showMediaFragment(constructor);
                        }
                    }
                }
            }
        });
*/


        /**
         * BROKEN
         */

/*
//Нажатие на фотку песни
        holder.playPausePreviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BackgroundService.mMediaPlayer == null) {
                    //Если другой элемент - Смена песни
                } else if(BackgroundService.mMediaPlayer != null) {
                    //Если тот-же самый элемент - stop/start
                    BackgroundService.mMediaPlayer = MediaPlayer.create(context, Uri.parse(constructor.SongUri));
                    showFragment(constructor);


                    if (BackgroundService.mMediaPlayer.isPlaying()) {

                        //Если во время клика играет, то...
                        BackgroundService.mMediaPlayer.pause();//Ставим на паузу
                        MiniMediaPlayerFragment.playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);//Меняем значок на миниФрагменте
                        holder.previewSongController.setVisibility(View.VISIBLE);//Показываем прогресбар и значок на превьюшке
                        holder.previewStopPauseButton.setBackgroundResource(R.drawable.ic_play_arrow_blue_24dp);//Меняем значок на превьюшке
                        setProgressBarTime(holder.progressBar);//Вкоючаем работу прогресбара

                    } else if (!BackgroundService.mMediaPlayer.isPlaying()) {
                        //Если во время клика стоит на паузе, то...
                        BackgroundService.mMediaPlayer.start();
                        MiniMediaPlayerFragment.playBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);//Меняем значок на миниФрагменте
                        holder.previewSongController.setVisibility(View.VISIBLE);//Показываем прогресбар и значок на превьюшке
                        holder.previewStopPauseButton.setBackgroundResource(R.drawable.ic_pause_blue_24dp);//Меняем значок на превьюшке
                        setProgressBarTime(holder.progressBar);//Вкоючаем работу прогресбара

                    }
                }
            }
        });
        */
    }


    @Override
    public int getItemCount() {
        return PlaylistDatabase.Arr.length;

    }


    public void showMiniMediaFragment(SongConstructor  constructor) {
        //Создаем сервис для действий в фоновом режиме
        //activity.startService(new Intent(activity, BackgroundService.class));
        //открываем фрагмент с плеером внизу экрана
        MiniMediaPlayerFragment fragment = MiniMediaPlayerFragment.newInstance(constructor);

        activity.getSupportFragmentManager().beginTransaction().replace(R.id.miniFrameLayout, fragment).commit();
    }



    private void showMediaFragment(SongConstructor  constructor) {

        MediaPlayerFragment fragment = MediaPlayerFragment.newInstance(constructor);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, fragment).commit();
    }


    //Код для работы progressBar
    public void setProgressBarTime(final ProgressBar progressBar) {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                progressBar.setMax(BackgroundService.mMediaPlayer.getDuration() / 1000);//Макс значение - длина песни(в секундах)
                int currentPosition =  BackgroundService.mMediaPlayer.getCurrentPosition() / 1000;//Текущая позиция(в секундах)
                progressBar.setProgress(currentPosition);

            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 0, 1);
    }






}







class PlaylistViewHolder extends RecyclerView.ViewHolder {

    View itemView;
    Context context;
    TextView AuthorName;
    TextView SongName;
    TextView SongLenght;
    ImageView SongPreview;
    ProgressBar progressBar;
    ImageView previewStopPauseButton;
    RelativeLayout previewSongController;


    LinearLayout mainContainer;
    RelativeLayout playPausePreviewLayout;//Макет с превью, кнопкой паузы и прогресс баром, появл при нажатии


    public PlaylistViewHolder(View itemView) {
        super(itemView);
        this.context = context;


        this.itemView = itemView;
        AuthorName = itemView.findViewById(R.id.AuthorName);
        SongLenght = itemView.findViewById(R.id.SongsLenght);
        SongName = itemView.findViewById(R.id.SongName);
        SongPreview = itemView.findViewById(R.id.preview);
        progressBar = itemView.findViewById(R.id.progressBar);
        previewStopPauseButton = itemView.findViewById(R.id.preview_stop_play_button);
        previewSongController = itemView.findViewById(R.id.preview_song_controller);

        playPausePreviewLayout = itemView.findViewById(R.id.play_pause_preview_layout);
        mainContainer = itemView.findViewById(R.id.main_container);




    }
}

