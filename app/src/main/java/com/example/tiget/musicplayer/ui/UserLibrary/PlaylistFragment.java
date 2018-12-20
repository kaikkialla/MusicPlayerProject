package com.example.tiget.musicplayer.ui.UserLibrary;



import android.content.Context;
import android.content.Intent;
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
import com.example.tiget.musicplayer.ui.Library.LibraryFragment;
import com.example.tiget.musicplayer.ui.MainActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class PlaylistFragment extends Fragment {

    PlaylistDatabase mDatabase;
    RecyclerViewAdapter adapter;

    public static PlaylistFragment newInstance(UserLibSong constructor) {
        PlaylistFragment fragnent = new PlaylistFragment();
        return fragnent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = getContext();
        //создаем главную вьюшку
        View view = inflater.inflate(R.layout.user_lib_fragment, container, false);


        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter((MainActivity) getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        //настраиваем


        mDatabase = new PlaylistDatabase(context);
        mDatabase.load();


        ImageView addSongBtn = view.findViewById(R.id.addSong);
        addSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new LibraryFragment()).commit();
            }
        });

        //возвращаем
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        mDatabase.setChangeListener(new PlaylistDatabase.ChangeListener() {
            @Override
            public void onChange(List<UserLibSong> alarms) {
                adapter.swap(alarms);
            }
        });
        adapter.swap(mDatabase.getSongs());
    }
}



class RecyclerViewAdapter extends RecyclerView.Adapter<PlaylistViewHolder> {

    MainActivity activity;
    public static String SongUri;
    public static int totalTime;
    Context context;

    private List<UserLibSong> mSongs = new ArrayList<>();




    public RecyclerViewAdapter(MainActivity activity) {
        this.activity = activity;
    }


    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.user_lib_song_item, parent, false );
        PlaylistViewHolder vh = new PlaylistViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(final PlaylistViewHolder holder, final int position) {
        context = this.context;

        final UserLibSong constructor = mSongs.get(position);
        holder.SongName.setText(constructor.SongName);
        holder.AuthorName.setText(constructor.AuthorName);
        holder.SongPreview.setImageResource(constructor.SongPreview);

        SongUri = constructor.SongUri;
        holder.previewSongController.setVisibility(View.GONE);





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
    }


    public void swap(List<UserLibSong> songs) {
        if (songs != null) {
            mSongs.clear();
            mSongs.addAll(songs);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mSongs.size();

    }


    public void showMiniMediaFragment(UserLibSong constructor) {
        //Создаем сервис для действий в фоновом режиме
        //activity.startService(new Intent(activity, BackgroundService.class));
        //открываем фрагмент с плеером внизу экрана
        MiniMediaPlayerFragment fragment = MiniMediaPlayerFragment.newInstance(constructor);

        activity.getSupportFragmentManager().beginTransaction().replace(R.id.miniFrameLayout, fragment).commit();
    }



    private void showMediaFragment(UserLibSong constructor) {

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

