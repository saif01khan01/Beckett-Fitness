package com.example.beckettfitness;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull AddMealFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new breakfast_frag();
            case 1:
                return new lunch_frag();
            case 2:
                return new dinner_frag();
            case 3:
                return new snacks_frag();
            default:
                return new home_fragment();
        }
    }


    @Override
    public int getItemCount() {
        return 4;
    }
}
