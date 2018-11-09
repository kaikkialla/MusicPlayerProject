package com.example.tiget.musicplayer;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MiniMediaPlayerFragment extends Fragment {

    Button playBtn;
    TextView SongNameTextView;
    ImageView SongImage;
    LinearLayout mainContainer;


    public static MiniMediaPlayerFragment newInstance(Constructor constructor) {
        MiniMediaPlayerFragment fragnent = new MiniMediaPlayerFragment();
        Bundle argument = new Bundle();
        argument.putSerializable("Constructor", constructor);
        fragnent.setArguments(argument);
        return fragnent;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context context = getContext();
        View view = inflater.inflate(R.layout.mini_media_player_fragment, container, false);

        playBtn=view.findViewById(R.id.mini_play_btn);
        SongImage=view.findViewById(R.id.mini_preview);
        SongNameTextView=view.findViewById(R.id.mini_song_name);
        mainContainer = view.findViewById(R.id.mini_song_info_container);


        final Constructor constructor = (Constructor) getArguments().getSerializable("Constructor");
        String SongName = constructor.SongName;
        int SongImageResourseId = constructor.SongPreview;

        SongNameTextView.setText(SongName);
        SongImage.setImageResource(SongImageResourseId);




        //Кнопка паузы
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RecyclerViewAdapter.mMediaPlayer.isPlaying()) {
                    RecyclerViewAdapter.mMediaPlayer.pause();
                    playBtn.setBackgroundResource(R.drawable.play);
                } else if (!RecyclerViewAdapter.mMediaPlayer.isPlaying()) {
                    RecyclerViewAdapter.mMediaPlayer.start();
                    playBtn.setBackgroundResource(R.drawable.stop);
                }

            }
        });

        mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayerFragment fragment = MediaPlayerFragment.newInstance(constructor);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, fragment).commit();
            }
        });

        return view;

    }





}

