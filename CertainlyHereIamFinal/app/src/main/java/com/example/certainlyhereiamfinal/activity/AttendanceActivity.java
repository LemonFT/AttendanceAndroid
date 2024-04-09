package com.example.certainlyhereiamfinal.activity;

import static com.example.certainlyhereiamfinal.Global.isValidEmail;
import static com.example.certainlyhereiamfinal.Global.showAlert;
import static com.example.certainlyhereiamfinal.Global.showAlertSuccess;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.certainlyhereiamfinal.R;
import com.example.certainlyhereiamfinal.adapter.AttendanceAdapter;
import com.example.certainlyhereiamfinal.model.Attendance;
import com.example.certainlyhereiamfinal.model.Classroom;
import com.example.certainlyhereiamfinal.model.User;
import com.example.certainlyhereiamfinal.viewmodel.AttendanceViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttendanceActivity extends AppCompatActivity {

    private FloatingActionButton btnShowQr, btnHelpAtt;
    private RelativeLayout qr_show_layout;
    private RelativeLayout help_attendance_show_layout;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetBehavior bottomSheetBehaviorHelp;
    private RecyclerView recyclerView;
    private AttendanceAdapter attendanceAdapter;
    private TextView yesAtt, noAtt;
    private ImageView img_qr;
    private AttendanceViewModel attendanceViewModel;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private EditText edtEmail, edtIdentifier;
    private Button btnSaveAttHelp;
    private ImageView back_session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_activity);
        attendanceViewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);
        img_qr = findViewById(R.id.img_qr);
        qr_show_layout = findViewById(R.id.show_qr_form);
        bottomSheetBehavior = BottomSheetBehavior.from(qr_show_layout);
        btnShowQr = findViewById(R.id.seen_qr);
        btnHelpAtt = findViewById(R.id.attendance_help);
        help_attendance_show_layout = findViewById(R.id.attendance_help_form);
        bottomSheetBehaviorHelp = BottomSheetBehavior.from(help_attendance_show_layout);
        btnSaveAttHelp = findViewById(R.id.attendance_help_btn);
        edtEmail = findViewById(R.id.edt_email_attendanceHelp);
        edtIdentifier = findViewById(R.id.edt_identifier_attendanceHelp);
        attendanceAdapter = new AttendanceAdapter(AttendanceActivity.this);
        yesAtt = findViewById(R.id.yesAtt);
        noAtt = findViewById(R.id.noAtt);
        back_session = findViewById(R.id.back_session);

        Intent intent = getIntent();
        String qr = intent.getStringExtra("qrCode");
        Long classId = intent.getLongExtra("classId", -1);

        firstLoading(classId, qr);
        back_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AttendanceActivity.this, SessionsActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        yesAtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadStudentsAttendanced(AttendanceActivity.this, classId, qr);
                yesAtt.setTextColor(Color.BLUE);
                noAtt.setTextColor(Color.BLACK);
            }
        });

        noAtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadStudentsNoAttendanceYet(AttendanceActivity.this, classId, qr);
                yesAtt.setTextColor(Color.BLACK);
                noAtt.setTextColor(Color.BLUE);
            }
        });

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

        btnSaveAttHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = edtEmail.getText().toString();
                String identifierStr = edtIdentifier.getText().toString();
                if(emailStr.equals("") || identifierStr.equals("")){
                    showAlert("Check information and try again!", AttendanceActivity.this);
                    return;
                }
                if((!emailStr.equals("") || !emailStr.equals(" ")) && !isValidEmail(emailStr)){
                    showAlert("Email invalid", AttendanceActivity.this);
                    return;
                }
                User user = new User(emailStr, identifierStr, "");
                Classroom classroom = new Classroom(classId);
                Attendance attendance = new Attendance(user, classroom, qr, new Date());
                attendanceViewModel.insertAttendance(attendance).observe(AttendanceActivity.this, data -> {
                    if(data.equals("Attendance succcessfully")){
                        runOnUiThread(() -> {
                            firstLoading(classId, qr);
                            showAlertSuccess("Attendance succcessfully", AttendanceActivity.this);
                            bottomSheetBehaviorHelp.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        });
                    }else {
                        firstLoading(classId, qr);
                        showAlert(data, AttendanceActivity.this);
                        bottomSheetBehaviorHelp.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                });
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent1 = new Intent(AttendanceActivity.this, SessionsActivity.class);
                startActivity(intent1);
                finish();
            }
        };

        this.getOnBackPressedDispatcher().addCallback(this, callback);

    }

    private void firstLoading(Long classId, String qr){
        loadStudentsAttendanced(this, classId, qr);
        yesAtt.setTextColor(Color.BLUE);
        noAtt.setTextColor(Color.BLACK);
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

    private void loadStudentsAttendanced(Context context, Long classId, String qr){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.attendance_recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        attendanceViewModel.findUsersAttendanced(classId, qr).observe((LifecycleOwner) context, data -> {
            attendanceAdapter.setStudents(data);
            recyclerView.setAdapter(attendanceAdapter);
        });
    }

    private void loadStudentsNoAttendanceYet(Context context, Long classId, String qr){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.attendance_recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        attendanceViewModel.findUsersNoAttendanceYet(classId, qr).observe((LifecycleOwner) context, data -> {
            attendanceAdapter.setStudents(data);
            recyclerView.setAdapter(attendanceAdapter);
        });
    }



}
