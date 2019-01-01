package com.example.tiget.musicplayer.ui.Library;


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
import com.example.tiget.musicplayer.ui.Song;
import com.example.tiget.musicplayer.ui.UserLibrary.UserLibDatabase;


public class LibraryFragment extends Fragment {

    UserLibDatabase mDatabase;
    LibraryAdapter adapter;

    public static LibraryFragment  newInstance(Song constructor) {
        LibraryFragment  fragnent = new LibraryFragment();
        return fragnent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = getContext();
        View view = inflater.inflate(R.layout.lib_fragment, container, false);


        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new LibraryAdapter((MainActivity) getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));


        mDatabase = new UserLibDatabase(context);
        mDatabase.load();

        return view;

    }
}


