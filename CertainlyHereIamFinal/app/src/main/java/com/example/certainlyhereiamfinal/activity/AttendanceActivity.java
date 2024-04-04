package com.example.certainlyhereiamfinal.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.certainlyhereiamfinal.R;
import com.example.certainlyhereiamfinal.adapter.StudentAdapter;
import com.example.certainlyhereiamfinal.entityui.UserAttendance;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    private FloatingActionButton btnShowQr, btnHelpAtt;
    private RelativeLayout qr_show_layout;
    private RelativeLayout help_attendance_show_layout;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetBehavior bottomSheetBehaviorHelp;
    private RecyclerView recyclerView;
    private StudentAdapter studentAdapter;
    private TextView yesAtt, noAtt;
    private ImageView img_qr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_activity);

        Intent intent = getIntent();
        String qr = intent.getStringExtra("qrcode");
        int class_id = intent.getIntExtra("class_id", -1);

//        loadStudentsAttendanceed(this);
        yesAtt = findViewById(R.id.yesAtt);
        noAtt = findViewById(R.id.noAtt);
        yesAtt.setTextColor(Color.BLUE);

        yesAtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadStudentsAttendanceed(AttendanceActivity.this);
                yesAtt.setTextColor(Color.BLUE);
                noAtt.setTextColor(Color.BLACK);
            }
        });

        noAtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadStudentsNoAttendanceYet(AttendanceActivity.this);
                yesAtt.setTextColor(Color.BLACK);
                noAtt.setTextColor(Color.BLUE);
            }
        });


        img_qr = findViewById(R.id.img_qr);
        qr_show_layout = findViewById(R.id.show_qr_form);
        bottomSheetBehavior = BottomSheetBehavior.from(qr_show_layout);
        btnShowQr = findViewById(R.id.seen_qr);
        btnShowQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    try {
                        Bitmap bitmap = encodeAsBitmap(qr, 250, 250);
                        img_qr.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        btnHelpAtt = findViewById(R.id.attendance_help);
        help_attendance_show_layout = findViewById(R.id.attendance_help_form);
        bottomSheetBehaviorHelp = BottomSheetBehavior.from(help_attendance_show_layout);
        btnHelpAtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehaviorHelp.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehaviorHelp.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehaviorHelp.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
    }

    Bitmap encodeAsBitmap(String str, int width, int height) throws WriterException {
        BitMatrix result;
        Bitmap bitmap = null;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, width, height, null);

            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        } catch (Exception e) {
            Log.e("error: ", String.valueOf(e));
            return null;
        }
        return bitmap;
    }

    public void loadStudentsAttendanceed(Context context){
        studentAdapter = new StudentAdapter(context, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.attendance_recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<UserAttendance> userAttendances = new ArrayList<>();
        UserAttendance userAttendance = new UserAttendance(1, "lemonftdev@gmail.com", "3121410292", "Nguyễn Hoàng Diệu Thảo", "https://th.bing.com/th/id/R.8566db18ff6fdf6adf181fb61b025dfc?rik=CPybdUWp8Z3jLQ&pid=ImgRaw&r=0", new Date());
        userAttendances.add(userAttendance);
        userAttendances.add(userAttendance);
        userAttendances.add(userAttendance);
        userAttendances.add(userAttendance);
        userAttendances.add(userAttendance);
        userAttendances.add(userAttendance);
        userAttendances.add(userAttendance);
        userAttendances.add(userAttendance);
        userAttendances.add(userAttendance);
        userAttendances.add(userAttendance);
//        studentAdapter.setStudents(userAttendances);
        recyclerView.setAdapter(studentAdapter);
    }

    public void loadStudentsNoAttendanceYet(Context context){
        studentAdapter = new StudentAdapter(context, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.attendance_recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<UserAttendance> userAttendances = new ArrayList<>();
        UserAttendance userAttendance = new UserAttendance(1, "lemonftdev@gmail.com", "3121410292", "Nguyễn Hoàng Diệu Thảo", "https://th.bing.com/th/id/R.8566db18ff6fdf6adf181fb61b025dfc?rik=CPybdUWp8Z3jLQ&pid=ImgRaw&r=0", new Date());
        userAttendances.add(userAttendance);
        userAttendances.add(userAttendance);
        userAttendances.add(userAttendance);
        userAttendances.add(userAttendance);
        userAttendances.add(userAttendance);
        userAttendances.add(userAttendance);
        userAttendances.add(userAttendance);
        userAttendances.add(userAttendance);
        userAttendances.add(userAttendance);
        userAttendances.add(userAttendance);
//        studentAdapter.setStudents(userAttendances);
        recyclerView.setAdapter(studentAdapter);
    }

}
