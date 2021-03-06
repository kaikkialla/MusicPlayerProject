package com.example.tiget.musicplayer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tiget.musicplayer.R;
import com.example.tiget.musicplayer.ui.UserLibrary.UserLibDatabase;
import com.makeramen.roundedimageview.RoundedImageView;

public class SongInfoFragment extends BottomSheetDialogFragment {

    private RoundedImageView SongPreviewIV;
    private TextView SongNameTV;
    private TextView AuthorNameTV;
    public static ImageView CloseArea;

    private static String mSongUri;
    private static String mAuthorName;
    private static String mSongName;
    private static int mResId;

    private RelativeLayout mLikeSong;
    private RelativeLayout mHideSong;
    private RelativeLayout mAddSong;
    private RelativeLayout mRemoveSong;

    private UserLibDatabase mDatabase;


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
        //CloseArea = view.findViewById(R.id.closeArea);

        mLikeSong = view.findViewById(R.id.likeSong);
        mHideSong = view.findViewById(R.id.hideSong);
        mAddSong = view.findViewById(R.id.addSong);
        mRemoveSong = view.findViewById(R.id.removeSong);

        if(mResId != 0) {
            SongPreviewIV.setImageResource(mResId);
        } else if(mResId == 0) {
            SongPreviewIV.setImageResource(R.drawable.no_image_loaded);
        }

        SongNameTV.setText(mSongName);
        AuthorNameTV.setText(mAuthorName);

        mDatabase = new UserLibDatabase(getActivity());




        mAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDatabase.alreadyExists(mSongUri)) {
                    Snackbar.make(mAddSong, "Данная песня уже у вас в плейлисте", Snackbar.LENGTH_SHORT).show();
                } else {
                    mDatabase.addSong(new Song(0, mAuthorName, mSongName, mSongUri, mResId));
                    Snackbar.make(mAddSong, "Песня добавлена в ваш плейлист", Snackbar.LENGTH_SHORT).show();
                }
            }
        });


    }
}