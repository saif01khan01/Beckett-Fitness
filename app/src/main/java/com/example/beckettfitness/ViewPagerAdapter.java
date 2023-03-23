package com.example.beckettfitness;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
                return new daily_meal_frag();
            case 1:
                return new summary_frag();
            default:
                return new AddMealFragment();
        }
    }


    @Override
    public int getItemCount() {
        return 2;
    }
}
