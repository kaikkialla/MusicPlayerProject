package com.example.tiget.musicplayer.ui.Library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tiget.musicplayer.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class ViewHolder extends RecyclerView.ViewHolder {

    View itemView;
    Context context;
    TextView AuthorName;
    TextView SongName;
    ImageView SongInfo;
    RoundedImageView SongImage;
    LinearLayout mainContainer;


    public ViewHolder(View itemView) {
        super(itemView);


        this.itemView = itemView;
        AuthorName = itemView.findViewById(R.id.AuthorName);
        SongInfo = itemView.findViewById(R.id.SongInfo);
        SongName = itemView.findViewById(R.id.SongName);
        SongImage = itemView.findViewById(R.id.preview);
        mainContainer = itemView.findViewById(R.id.main_container);




    }
}
