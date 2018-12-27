package com.example.tiget.musicplayer.ui.UserLibrary;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.example.tiget.musicplayer.ui.BackgroundService;
import com.example.tiget.musicplayer.ui.Library.LibraryFragment;
import com.example.tiget.musicplayer.ui.MainActivity;
import com.example.tiget.musicplayer.ui.voids;

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
                voids.showFragment(new LibraryFragment(), getActivity());
                /*
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.FrameLayout, new LibraryFragment())
                        .commit();
                        */
            }
        });
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


