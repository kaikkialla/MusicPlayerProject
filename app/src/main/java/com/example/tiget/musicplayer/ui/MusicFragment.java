package com.example.tiget.musicplayer.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.example.tiget.musicplayer.R;
import com.example.tiget.musicplayer.ui.Library.LibraryFragment;
import com.example.tiget.musicplayer.ui.UserLibrary.PlaylistFragment;

import static com.example.tiget.musicplayer.ui.MainActivity.SCREEN_WIDTH_PX;

public class MusicFragment extends Fragment {

    ViewPager mViewPager;
    ViewPagerAdapter mViewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.music_fragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = view.findViewById(R.id.ViewPager);
        mViewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);



// Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.pagerTabStrip);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        mViewPager.setPageMargin(pageMargin);

        //mViewPager.setCurrentItem(1);

        //changeColor(ContextCompat.getColor(getActivity().getBaseContext(), Color.GREEN));
        tabs.setViewPager(mViewPager);



    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        public String[] titles = {"My music", "Recommended"};

        @Override
        public Fragment getItem(int i) {


            switch (i) {
                case 0:
                    return new PlaylistFragment();
                case 1:
                    return new LibraryFragment();
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }
}
