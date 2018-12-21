package com.example.tiget.musicplayer.ui.Library;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.tiget.musicplayer.ui.BackgroundService;
import com.example.tiget.musicplayer.ui.UserLibrary.MediaPlayerFragment;
import com.example.tiget.musicplayer.ui.UserLibrary.MiniMediaPlayerFragment;
import com.example.tiget.musicplayer.ui.UserLibrary.PlaylistDatabase;
import com.example.tiget.musicplayer.ui.UserLibrary.PlaylistFragment;
import com.example.tiget.musicplayer.ui.UserLibrary.UserLibSong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class LibraryFragment extends Fragment {

    PlaylistDatabase mDatabase;
    LibraryAdapter adapter;

    public static LibraryFragment  newInstance(UserLibSong constructor) {
        LibraryFragment  fragnent = new LibraryFragment();
        return fragnent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = getContext();
        //создаем главную вьюшку
        View view = inflater.inflate(R.layout.lib_fragment, container, false);


        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new LibraryAdapter((MainActivity) getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        //настраиваем



        mDatabase = new PlaylistDatabase(context);
        mDatabase.load();


        ImageView backButton = view.findViewById(R.id.backToUserLibButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new PlaylistFragment()).commit();
            }
        });

        //возвращаем
        return view;

    }
}


