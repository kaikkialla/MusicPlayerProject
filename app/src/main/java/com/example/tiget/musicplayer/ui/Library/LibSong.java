package com.example.tiget.musicplayer.ui.Library;

import java.io.Serializable;

public class LibSong implements Serializable {
    public int id;
    public String type;
    public String AuthorName;
    public String SongName;
    public String SongUri;
    public int SongPreview;
    boolean explicit;

    public LibSong(int id, String type, String AuthorName, String SongName, String SongUri, int SongPreview, boolean explicit){
        this.id = id;
        this.type = type;
        this.AuthorName = AuthorName;
        this.SongName = SongName;
        this.SongUri = SongUri;
        this.SongPreview = SongPreview;
        this.explicit = explicit;

    }


    public int getId(){
        return id;
    }

    public String getType(){
        return type;
    }

    public String getAuthorName(){
        return AuthorName;
    }

    public String getSongName(){
        return SongName;
    }

    public String getSongUri(){
        return SongUri;
    }

    public int getSongPreview(){
        return SongPreview;
    }

    public boolean getExplicit(){
        return explicit;
    }


}