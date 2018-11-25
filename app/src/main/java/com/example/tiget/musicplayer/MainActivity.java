package com.example.tiget.musicplayer;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home:
                                getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new HomeActivity(), null).commit();
                                break;
                            case R.id.search:
                                getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new PremiumActivity.SearchActivity(), null).commit();
                                break;
                            case R.id.your_library:
                                getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new RecyclerViewFragment(), null).commit();
                                break;
                            case R.id.premium:
                                getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new PremiumActivity(), null).commit();
                                break;
                        }
                        return true;
                    }
                });





    }

}
