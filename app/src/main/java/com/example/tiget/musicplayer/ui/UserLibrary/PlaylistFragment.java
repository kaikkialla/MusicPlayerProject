package com.example.tiget.musicplayer.ui.UserLibrary;



import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.tiget.musicplayer.R;
import com.example.tiget.musicplayer.ui.MainActivity;

import java.util.List;


public class PlaylistFragment extends Fragment {

    UserLibDatabase mDatabase;
    UserLibAdapter adapter;

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
        adapter = new UserLibAdapter((MainActivity) getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        //настраиваем

        mDatabase = new UserLibDatabase(context);
        mDatabase.load();


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mDatabase.setChangeListener(new UserLibDatabase.ChangeListener() {
            @Override
            public void onChange(List<UserLibSong> alarms) {
                adapter.swap(alarms);
            }
        });
        adapter.swap(mDatabase.getSongs());
    }
}


