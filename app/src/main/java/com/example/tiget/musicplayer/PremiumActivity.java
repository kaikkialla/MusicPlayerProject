package com.example.tiget.musicplayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

public class PremiumActivity  extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context context = getContext();
        //создаем главную вьюшку
        View view = inflater.inflate(R.layout.premium_fragment_layout, container, false);
        return view;
    }

    public static class SearchActivity extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final Context context = getContext();
            //создаем главную вьюшку
            View view = inflater.inflate(R.layout.search_fragment_layout, container, false);
            return view;
        }
    }

    /**Сервис, играющий музыку на заднем фоне(даже когда приложение сверунто/выключено)*/



}
