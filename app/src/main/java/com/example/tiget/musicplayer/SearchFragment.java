package com.example.tiget.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = getContext();
        //создаем главную вьюшку
        View view = inflater.inflate(R.layout.search_fragment_layout, container, false);


        final RecyclerView recyclerView = view.findViewById(R.id.SearchRecyclerView);
        SearchAdapter adapter = new SearchAdapter((MainActivity) getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        return view;
    }
}








class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    MainActivity activity;
    Context context;

    public SearchAdapter (MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.search_recycler_view_row, parent, false );
        SearchViewHolder vh = new SearchViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(final SearchViewHolder holder, final int position) {
        context = this.context;
        final CategoryConstructor categoryConstructor = CategoryDatabase.Categories[position];
        String GenreName = categoryConstructor.CategoryName;
        int GenreBackground = categoryConstructor.CategoryBackground;

        holder.GenreName.setText(GenreName);
        holder.GenreBackground.setBackgroundColor(GenreBackground);


    }


    @Override
    public int getItemCount() {
        return CategoryDatabase.Categories.length;

    }




    class SearchViewHolder extends RecyclerView.ViewHolder {
        View v;
        Context context;
        LinearLayout GenreBackground;
        TextView GenreName;


        public SearchViewHolder(View v) {
            super(v);
            this.context = context;
            this.v = v;
            GenreName = v.findViewById(R.id.genre);
            GenreBackground = v.findViewById(R.id.background);


        }
    }
}
