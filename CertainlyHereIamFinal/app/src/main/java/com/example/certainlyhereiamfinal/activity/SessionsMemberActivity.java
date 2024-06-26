package com.example.certainlyhereiamfinal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.certainlyhereiamfinal.R;
import com.example.certainlyhereiamfinal.adapter.ViewPagerAdapter;
import com.example.certainlyhereiamfinal.adapter.ViewPagerMemberAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SessionsMemberActivity extends AppCompatActivity {


    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private ImageView back_classses;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_member_activity);

        mViewPager = findViewById(R.id.view_pager_member);
        mBottomNavigationView = findViewById(R.id.bottom_navigation_member);
        ViewPagerMemberAdapter viewPagerMemberAdapter = new ViewPagerMemberAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(viewPagerMemberAdapter);
        back_classses = findViewById(R.id.back_classses);

        back_classses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SessionsMemberActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mViewPager.setOffscreenPageLimit(1);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.item_sessions_member).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.item_scanqr).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.item_students_member).setChecked(true);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemSelected = menuItem.getItemId();
                if (itemSelected == R.id.item_sessions_member) {
                    mViewPager.setCurrentItem(0);
                }
                if (itemSelected == R.id.item_scanqr) {
                    mViewPager.setCurrentItem(1);
                }
                if (itemSelected == R.id.item_students_member) {
                    mViewPager.setCurrentItem(2);
                }
                return true;
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(SessionsMemberActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }
}
