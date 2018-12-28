package com.example.tiget.musicplayer.ui.UserLibrary;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tiget.musicplayer.R;
import com.example.tiget.musicplayer.ui.t;


public class MiniMediaPlayerFragment extends Fragment {

    public static ImageView playBtn;
    TextView SongNameTextView;
    ImageView SongImage;
    LinearLayout mainContainer;
    static Context context;

    public static final String ACTION_PLAY  = "ACTION_PLAY";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";



    public static MiniMediaPlayerFragment newInstance(UserLibSong constructor) {
        MiniMediaPlayerFragment fragnent = new MiniMediaPlayerFragment();
        Bundle argument = new Bundle();
        argument.putSerializable("Constructor", constructor);
        fragnent.setArguments(argument);
        return fragnent;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.mini_media_player_fragment, container, false);

        playBtn=view.findViewById(R.id.mini_play_btn);
        SongImage=view.findViewById(R.id.mini_preview);
        SongNameTextView=view.findViewById(R.id.mini_song_name);
        mainContainer = view.findViewById(R.id.mini_song_info_container);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final UserLibSong constructor = (UserLibSong) getArguments().getSerializable("Constructor");
        String SongName = constructor.SongName;
        int SongImageResourceId = constructor.SongPreview;


        SongNameTextView.setText(SongName);
        SongImage.setImageResource(SongImageResourceId);




        //Кнопка паузы
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.checkPlayButtonPressedState(context, playBtn, 1);

            }
        });


        //По нажатию на тело фрагмента, открываем большой MediaPlayer
        mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayerFragment fragment = MediaPlayerFragment.newInstance(constructor);
                t.showMediaFragment(constructor, getActivity());
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, fragment).commit();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        t.checkPlayButtonState(context, playBtn, 1 );
    }







}

