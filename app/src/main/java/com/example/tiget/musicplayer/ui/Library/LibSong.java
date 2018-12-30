package com.example.tiget.musicplayer.ui.Library;

import java.io.Serializable;

public class LibSong implements Serializable {
    public int id;
    public String AuthorName;
    public String SongName;
    public String SongUri;
    public int SongPreview;

    public LibSong(int id, String AuthorName, String SongName, String SongUri, int SongPreview){
        this.id = id;
        this.AuthorName = AuthorName;
        this.SongName = SongName;
        this.SongUri = SongUri;
        this.SongPreview = SongPreview;

    }


    public int getId(){
        return id;
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



}