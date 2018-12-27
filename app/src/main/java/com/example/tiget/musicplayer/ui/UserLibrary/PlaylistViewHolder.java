package com.example.tiget.musicplayer.ui.UserLibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tiget.musicplayer.R;

public class PlaylistViewHolder extends RecyclerView.ViewHolder {

    View itemView;
    Context context;
    TextView AuthorName;
    TextView SongName;
    TextView SongLength;
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
        SongLength = itemView.findViewById(R.id.SongInfo);
        SongName = itemView.findViewById(R.id.SongName);
        SongPreview = itemView.findViewById(R.id.preview);
        progressBar = itemView.findViewById(R.id.progressBar);
        previewStopPauseButton = itemView.findViewById(R.id.preview_stop_play_button);
        previewSongController = itemView.findViewById(R.id.preview_song_controller);

        playPausePreviewLayout = itemView.findViewById(R.id.play_pause_preview_layout);
        mainContainer = itemView.findViewById(R.id.main_container);




    }
}
