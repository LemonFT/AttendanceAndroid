package com.example.certainlyhereiamfinal.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.example.certainlyhereiamfinal.fragment.SessionsFragment;
import com.example.certainlyhereiamfinal.fragment.StatisticsFragment;
import com.example.certainlyhereiamfinal.fragment.StudentsFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return new StudentsFragment();
            case 2:
                return new StatisticsFragment();
            default:
                return new SessionsFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}

