package com.example.tiget.musicplayer;



import java.io.Serializable;




public class Constructor implements Serializable{
    public String type;
    public String AuthorName;
    public String SongName;
    public String SongUri;
    public int SongPreview;

    public Constructor(String type, String AuthorName, String SongName, String SongUri, int SongPreview){
        this.type = type;
        this.AuthorName = AuthorName;
        this.SongName = SongName;
        this.SongUri = SongUri;
        this.SongPreview = SongPreview;

    }

}
