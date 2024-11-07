package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class bnv_adapter extends FragmentStateAdapter {

    bnv_fragment1_recipes bnv_fragment1_recipes = new bnv_fragment1_recipes();
    bnv_fragment2_favourite bnv_fragment2_favourite = new bnv_fragment2_favourite();
    bnv_fragment3_profile bnv_fragment3_profile = new bnv_fragment3_profile();

    public bnv_adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return bnv_fragment1_recipes;
            case 1:
                return bnv_fragment2_favourite;
            default:
                return bnv_fragment3_profile;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
