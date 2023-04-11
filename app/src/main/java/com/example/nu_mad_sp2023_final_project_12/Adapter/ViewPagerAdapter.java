package com.example.nu_mad_sp2023_final_project_12.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;


public class ViewPagerAdapter extends FragmentStateAdapter {

    private List<Fragment> fragments;

    public ViewPagerAdapter(FragmentManager fm, Lifecycle lifecycle, List<Fragment> fragments) {
        super(fm, lifecycle);
        this.fragments = fragments;
    }
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }


}