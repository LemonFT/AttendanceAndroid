package com.example.certainlyhereiamfinal.activity;

import static com.example.certainlyhereiamfinal.Global.cutString;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.certainlyhereiamfinal.Global;
import com.example.certainlyhereiamfinal.R;
import com.example.certainlyhereiamfinal.adapter.ViewPagerAdapter;
import com.example.certainlyhereiamfinal.fragment.SessionsFragment;
import com.example.certainlyhereiamfinal.store.DataLocalManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SessionsActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private TextView title_class_name;
    private ImageView back_classses;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sessions_activity);
        mViewPager = findViewById(R.id.view_pager);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(viewPagerAdapter);

        mViewPager.setOffscreenPageLimit(3);

        title_class_name = findViewById(R.id.title_class_name);

        title_class_name.setText(cutString(DataLocalManager.getClassname(), 30));

        back_classses = findViewById(R.id.back_classses);
        back_classses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SessionsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.item_sessions).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.item_students).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.item_statistics).setChecked(true);
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
                if (itemSelected == R.id.item_sessions) {
                    mViewPager.setCurrentItem(0);
                }
                if (itemSelected == R.id.item_students) {
                    mViewPager.setCurrentItem(1);
                }
                if (itemSelected == R.id.item_statistics) {
                    mViewPager.setCurrentItem(2);
                }
                return true;
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(SessionsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }


}
