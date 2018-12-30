package com.example.tiget.musicplayer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tiget.musicplayer.R;

public class SongInfoFragment extends Fragment {

    private ImageView SongPreviewIV;
    private TextView SongNameTV;
    private TextView AuthorNameTV;
    public static ImageView CloseArea;

    private static String mSongUri;
    private static String mAuthorName;
    private static String mSongName;
    private static int mResId;



    public static SongInfoFragment newInstance(String SongUri, String AuthorName, String SongName, int ResId) {
        SongInfoFragment fragment = new SongInfoFragment();

        mSongUri = SongUri;
        mAuthorName = AuthorName;
        mSongName = SongName;
        mResId = ResId;

        return fragment;
    }







    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.song_info_dialog, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SongPreviewIV = view.findViewById(R.id.photo);
        SongNameTV = view.findViewById(R.id.SongName);
        AuthorNameTV = view.findViewById(R.id.AuthorName);
        CloseArea = view.findViewById(R.id.closeArea);

        SongPreviewIV.setImageResource(mResId);
        SongNameTV.setText(mSongName);
        AuthorNameTV.setText(mAuthorName);


        CloseArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new MusicFragment()).commit();
                if(MiniMediaPlayerFragment.mPauseButton != null) {
                    t.showMiniMediaFragment(mSongUri, mAuthorName, mSongName, mResId, getActivity());
                }

            }
        });

    }
}


/*

    if(mDatabase.alreadyExists(song.getSongUri())) {
                    Snackbar.make(holder.itemView, "Данная песня уже у вас в плейлисте", Snackbar.LENGTH_SHORT).show();
                } else {
                    mDatabase.addSong(new UserLibSong(0, song.getAuthorName(), song.getSongName(), song.getSongUri(), song.getSongPreview()));
                    //Snackbar.make(holder.itemView, "Песня добавлена в ваш плейлист", Snackbar.LENGTH_SHORT).show();
                }

 */