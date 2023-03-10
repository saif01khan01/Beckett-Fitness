package com.example.beckettfitness;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;


public class AddMealFragment extends Fragment {
    TabLayout tl;
    ViewPager2 viewPager2;
    ViewPagerAdapter myViewPagerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_meal, container, false);

        tl = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.view_pager);
        myViewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);
        viewPager2.setSaveEnabled(false);

        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tl.getTabAt(position).select();
            }
        });


        return view;
    }
}