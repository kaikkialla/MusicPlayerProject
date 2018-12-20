package com.example.tiget.musicplayer.ui.Library;

import com.example.tiget.musicplayer.R;
import com.example.tiget.musicplayer.ui.UserLibrary.UserLibSong;

public class Database {

    final private static String MarioUri = "http://66.90.93.122/ost/super-mario-bros.-1-3-anthology/gczrgwrx/1%2001%20Main%20Theme%20Overworld.mp3";
    final private static String WiiUri = "http://66.90.93.122/ost/mii-channel/blnabqdr/002%20-%20Kazumi%20Totaka%20-%20Mii%20Plaza.mp3";
    final private static String UndertaleUri = "http://trendmusic.media/mp3/e89934763ff38b5030b501313321833d24eda3bea7c31da139612e47c57f9d981936231339c51a60eaea3b224712af885cd5769138f21b81956d08158bec151d2574e782e3100ce9eb7b60ed7e337a0738ef8b1460680caed0dae387ab7a904e88c8ed37158ccf3fe58d51301e79f40208af6fcea703b02c245546bcf3167ac477f48f93d08a4c45d63ef9de716375e4ee2e4f0ce3669e8cea489f7992b2cd85/Undertale+OST+%96+Megalovania.mp3";

    public static final UserLibSong[] Arr = {



            new UserLibSong(0,"Mario", "Mario Theme", MarioUri, R.drawable.a)
            ,new UserLibSong(0,"Wii", "Wii theme", WiiUri, R.drawable.b)
            ,new UserLibSong(0,"Undertale", "Megalovania", UndertaleUri, R.drawable.c)

    };




}