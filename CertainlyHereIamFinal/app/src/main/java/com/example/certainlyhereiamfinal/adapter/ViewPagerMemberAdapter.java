package com.example.certainlyhereiamfinal.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.certainlyhereiamfinal.fragment.ScanQRFragment;
import com.example.certainlyhereiamfinal.fragment.SessionsFragment;
import com.example.certainlyhereiamfinal.fragment.SessionsMemberFragment;
import com.example.certainlyhereiamfinal.fragment.StatisticsFragment;
import com.example.certainlyhereiamfinal.fragment.StudentsFragment;
import com.example.certainlyhereiamfinal.fragment.StudentsMemberFragment;

public class ViewPagerMemberAdapter extends FragmentStatePagerAdapter {

    public ViewPagerMemberAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new SessionsMemberFragment();
            case 1:
                return new ScanQRFragment();
            case 2:
                return new StudentsMemberFragment();
            default:
                return new SessionsMemberFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}

