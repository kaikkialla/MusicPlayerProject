package com.example.tiget.musicplayer;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import java.util.Timer;
import java.util.TimerTask;


public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    MainActivity activity;
    public static String SongUri;
    public static MediaPlayer SongLenghtMediaPlayer;
    public static int totalTime;
    Context context;
    ProgressBar progressBar;


    public RecyclerViewAdapter(MainActivity activity) {
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.row, parent, false );
        ViewHolder vh = new ViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        context = this.context;

        final Constructor constructor = DataBase.Arr[position];
        holder.SongName.setText(constructor.SongName);
        holder.AuthorName.setText(constructor.AuthorName);
        holder.SongPreview.setImageResource(constructor.SongPreview);


        holder.previewSongController.setVisibility(View.GONE);



        SongUri = constructor.SongUri;

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




        //Нажатие на тело песни(без превьюшки)
        holder.mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Если тот-же самый элемент - showFragment
                //Если другой, то BackgroundService.mMediaPlayer.stop(); BackgroundService.mMediaPlayer.create(Новыя песня), BackgroundService.mMediaPlayer.start();
                showFragment(constructor);
            }
        });



//Нажатие на фотку песни
        holder.playPausePreviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BackgroundService.mMediaPlayer == null) {
                    //Если другой элемент - Смена песни
                } else if(BackgroundService.mMediaPlayer != null) {
                    //Если тот-же самый элемент - stop/start
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
    }

    @Override
    public int getItemCount() {
        return DataBase.Arr.length;

    }



    //метод, рисующий фрагмент с иформацией о валюте
    private void showFragment(Constructor constructor) {
        if(BackgroundService.mMediaPlayer == null) {

            //Создаем сервис для действий в фоновом режиме
            activity.startService(new Intent(activity, BackgroundService.class));
            //открываем фрагмент с плеером внизу экрана
            MiniMediaPlayerFragment fragment = MiniMediaPlayerFragment.newInstance(constructor);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.miniFrameLayout, fragment).commit();

        } else if(BackgroundService.mMediaPlayer != null) {

            MediaPlayerFragment fragment = MediaPlayerFragment.newInstance(constructor);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, fragment).commit();
        }
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


