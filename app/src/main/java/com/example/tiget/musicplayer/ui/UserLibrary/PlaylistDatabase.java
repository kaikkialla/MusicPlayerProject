package com.example.tiget.musicplayer.ui.UserLibrary;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;


public class PlaylistDatabase {

    private Context mContext;
    private static List<UserLibSong> mSongs = new ArrayList<>();

    /**
     * Переменная хранит слушатель изменений в базе данных.
     */
    private static ChangeListener mChangeListener = null;

    public PlaylistDatabase(Context context) {
        mContext = context;
    }

    /**
     * Метод загружает из локального хранилища сохранённые ранее будильники.
     */
    public void load() {
        // получаем будильники в виде строки
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        final String s = preferences.getString("ALARMS", "");
        // десериализуем строку
        final Gson gson = new Gson();
        mSongs  = gson.fromJson(s, new TypeToken<List<UserLibSong>>(){}.getType());
        if (mSongs == null) {
            mSongs = new ArrayList<>();
        }
        if (mChangeListener != null) {
            mChangeListener.onChange(mSongs);
        }
    }

    /**
     * Метод сохраняет в локальное хранилище будильники из mAlarms.
     */
    private void save() {
        // открываем для редактирования SP
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        final SharedPreferences.Editor editor = preferences.edit();
        // превращаем в строку (сериализуем)
        final Gson gson = new Gson();
        final String s = gson.toJson(mSongs, new TypeToken<List<UserLibSong>>(){}.getType());
        // сохраняем по ключу ALARMS
        editor.putString("ALARMS", s);
        editor.apply();
    }

    /**
     * Метод добавляет будильник в базу данных (и сохраняет изменения в локальное хранилище).
     * @param song Будильник, который нужно добавить.
     */
    public void addSong(UserLibSong song) {
        mSongs.add(song);
        if (mChangeListener != null) {
            mChangeListener.onChange(mSongs);
        }
        save();
    }

    /**
     * Метод удаляет из базы данных будильник по id (и сохраняет изменения в локальное хранилище).
     * @param id Идентификатор будильника, который нужно удалить.
     */
    public void removeSong(long id) {
        boolean needsSave = false;
        for (int i = 0; i < mSongs.size(); ++i) {
            if (mSongs.get(i).getId() == id) {
                mSongs.remove(i);
                needsSave = true;
                break;
            }
        }

        if (needsSave) {
            if (mChangeListener != null) {
                mChangeListener.onChange(mSongs);
            }
            save();
        }
    }

    /**
     * Возвращает список будильников.
     */
    public static List<UserLibSong> getSongs() {
        return mSongs;
    }

    /**
     * Очищает будильники (только из оперативной памяти!).
     */
    public void clear() {
        mSongs.clear();
        if (mChangeListener != null) {
            mChangeListener.onChange(mSongs);
        }
    }

    public static void setChangeListener(ChangeListener listener) {
        mChangeListener = listener;
    }

    interface ChangeListener {
        /**
         * Будет вызываться после каждого изменения списка будильников.
         * @param songs Актуальный список будильников.
         */
        void onChange(List<UserLibSong> songs);
    }

}