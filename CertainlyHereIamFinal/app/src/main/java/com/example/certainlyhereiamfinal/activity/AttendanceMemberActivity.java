package com.example.certainlyhereiamfinal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.certainlyhereiamfinal.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AttendanceMemberActivity extends AppCompatActivity {

    private FloatingActionButton attendanceBtn;
    private BottomSheetBehavior bottomSheetBehavior;
    private RelativeLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_member_activity);
        attendanceBtn = findViewById(R.id.attendance_btn);
        layout = findViewById(R.id.attendance_form);
        bottomSheetBehavior = BottomSheetBehavior.from(layout);
        attendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent1 = new Intent(AttendanceMemberActivity.this, SessionsMemberActivity.class);
                startActivity(intent1);
                finish();
            }
        };

        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }
}
