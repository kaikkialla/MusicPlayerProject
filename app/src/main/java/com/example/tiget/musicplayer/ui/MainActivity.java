package com.example.tiget.musicplayer.ui;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.FrameLayout;
import com.example.tiget.musicplayer.R;



public class MainActivity extends AppCompatActivity {

    public static int SCREEN_WIDTH_PX;
    public static int SCREEN_HEIGHT_PX;

    public static int MARGIN_LEFT_PX;
    public static int MARGIN_RIGHT_PX;
    public static int MARGIN_TOP_PX;
    public static int MARGIN_BOTTOM_PX;


    public static float SCREEN_WIDTH_DP;
    public static float SCREEN_HEIGHT_DP;

    public static int MARGIN_LEFT_DP = 32;
    public static int MARGIN_RIGHT_DP = 32;
    public static int MARGIN_TOP_DP = 32;
    public static int MARGIN_BOTTOM_DP = 32;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = getResources().getDisplayMetrics().density;


        SCREEN_WIDTH_DP = dm.widthPixels / density;
        SCREEN_HEIGHT_DP = dm.heightPixels / density;

        SCREEN_WIDTH_PX = dm.widthPixels;
        SCREEN_HEIGHT_PX = dm.heightPixels;



        MARGIN_LEFT_PX = (int) (MARGIN_LEFT_DP * density);
        MARGIN_RIGHT_PX = (int) (MARGIN_RIGHT_DP * density);

        MARGIN_TOP_PX = (int) (MARGIN_TOP_DP * density);
        MARGIN_BOTTOM_PX = (int) (MARGIN_BOTTOM_DP* density);


        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.f, new MusicFragment()).commit();
        }
    }


}


/**
 * TODO
 *
 * Перенести получение текущей позиции трека в Background Service, добавить смену песни по окончанию, seek bar
 */
