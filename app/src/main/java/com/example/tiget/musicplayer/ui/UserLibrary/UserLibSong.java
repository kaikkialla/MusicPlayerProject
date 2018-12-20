package com.example.tiget.musicplayer.ui.UserLibrary;

import java.io.Serializable;

public class UserLibSong implements Serializable {
    public long id;
    public String type;
    public String AuthorName;
    public String SongName;
    public String SongUri;
    public int SongPreview;
    boolean explicit;

    public UserLibSong(long id, /*String type, */String AuthorName, String SongName, String SongUri, int SongPreview/*, boolean explicit*/){
        this.id = id;
        //this.type = type;
        this.AuthorName = AuthorName;
        this.SongName = SongName;
        this.SongUri = SongUri;
        this.SongPreview = SongPreview;
        //this.explicit = explicit;

    }


    public long getId(){
        return id;
    }

    /*public String getType(){
        return type;
    }*/

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
/*
    public boolean getExplicit(){
        return explicit;
    }
*/

}