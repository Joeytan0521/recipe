package com.example.recipe;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    BottomNavigationView bottomNavigationView;
    bnv_adapter bnv_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navigation_bar_page);

        viewPager2 = findViewById(R.id.bnv_viewPager2);
        bottomNavigationView = findViewById(R.id.bnv_bottomNavigationBar);

        bnv_adapter = new bnv_adapter(this);
        viewPager2.setAdapter(bnv_adapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageSelected(int position){
                switch(position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.bnv_menu_recipes).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.bnv_menu_favourite).setChecked(true);
                        break;
                    default:
                        bottomNavigationView.getMenu().findItem(R.id.bnv_menu_profile).setChecked(true);
                        break;
                }
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.bnv_menu_recipes) {
                    viewPager2.setCurrentItem(0, true);
                } else if (item.getItemId() == R.id.bnv_menu_favourite) {
                    viewPager2.setCurrentItem(1, true);
                } else if (item.getItemId() == R.id.bnv_menu_profile) {
                    viewPager2.setCurrentItem(2, true);
                }
                return true;
            }
        });
    }
}
