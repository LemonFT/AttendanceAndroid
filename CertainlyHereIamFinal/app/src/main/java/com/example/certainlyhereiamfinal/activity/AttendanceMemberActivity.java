package com.example.certainlyhereiamfinal.activity;

import static com.example.certainlyhereiamfinal.Global.isValidEmail;
import static com.example.certainlyhereiamfinal.Global.permissionLocationGranted;
import static com.example.certainlyhereiamfinal.Global.requestPermissionLocation;
import static com.example.certainlyhereiamfinal.Global.showAlert;
import static com.example.certainlyhereiamfinal.Global.showAlertSuccess;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.certainlyhereiamfinal.R;
import com.example.certainlyhereiamfinal.model.Attendance;
import com.example.certainlyhereiamfinal.model.Classroom;
import com.example.certainlyhereiamfinal.model.Session;
import com.example.certainlyhereiamfinal.model.User;
import com.example.certainlyhereiamfinal.store.DataLocalManager;
import com.example.certainlyhereiamfinal.viewmodel.AttendanceViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.Date;

public class AttendanceMemberActivity extends AppCompatActivity {

    private FloatingActionButton attendanceBtn;
    private BottomSheetBehavior bottomSheetBehavior;
    private RelativeLayout layout;
    private TextView checked, check, clock;
    private AttendanceViewModel attendanceViewModel;
    private EditText email_member, identifier_member;
    private Button btnSaveAttendance;
    private double latitude, longitude;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback locationCallback;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private Location location;
    private Context context;
    private Session session;
    private ImageView back_session_member;
    private boolean isRotated = false;
    private Handler handler;
    private ShapeAppearanceModel originalShapeAppearanceModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_member_activity);
        attendanceViewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);
        Intent intent = getIntent();
        String sessionData = intent.getStringExtra("session");
        session = new Gson().fromJson(sessionData, Session.class);
        checked = findViewById(R.id.checked);
        check = findViewById(R.id.check);
        clock = findViewById(R.id.clock);
        email_member = findViewById(R.id.email_member);
        identifier_member = findViewById(R.id.identifier_member);
        attendanceBtn = findViewById(R.id.attendance_btn);
        originalShapeAppearanceModel = attendanceBtn.getShapeAppearanceModel();
        btnSaveAttendance = findViewById(R.id.btnSaveAttendance);
        handler = new Handler(Looper.getMainLooper());
        context = this;
        loadData(session);
        layout = findViewById(R.id.attendance_form);
        bottomSheetBehavior = BottomSheetBehavior.from(layout);
        back_session_member = findViewById(R.id.back_session_member);
        back_session_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AttendanceMemberActivity.this, SessionsMemberActivity.class);
                startActivity(intent1);
            }
        });
        attendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    onFloatButtonClick();
                    if(!permissionLocationGranted(context)){
                        requestPermissionLocation(AttendanceMemberActivity.this);
                    }else {
                        init();
                    }
                }else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });


        btnSaveAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email_member.getText().toString();
                String identifierStr = identifier_member.getText().toString();
                if(emailStr.equals("") || identifierStr.equals("")){
                    showAlert("Check information and try again!", AttendanceMemberActivity.this);
                    return;
                }
                if((!emailStr.equals("") || !emailStr.equals(" ")) && !isValidEmail(emailStr)){
                    showAlert("Email invalid", AttendanceMemberActivity.this);
                    return;
                }
                User user = new User(emailStr, identifierStr, "");
                Classroom classroom = new Classroom(session.getClassroom().getId());
                attendanceViewModel.
                        insertAttendanceMember(new Attendance(user, classroom, session.getQr(), new Date()), latitude, longitude)
                        .observe(AttendanceMemberActivity.this, data -> {
                            if(data.equals("Attendance succcessfully")){
                                runOnUiThread(() -> {
                                        showAlertSuccess("Attendance succcessfully", AttendanceMemberActivity.this);
                                        loadData(session);
                                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                });
                            }else{
                                showAlert(data, AttendanceMemberActivity.this);
                            }
                    });
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

    public void onFloatButtonClick() {
        isRotated = true;
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel.Builder()
                .setAllCorners(CornerFamily.ROUNDED, 100)
                .build();
        attendanceBtn.setShapeAppearanceModel(shapeAppearanceModel);
        attendanceBtn.setImageResource(R.drawable.ic_loadright);
        handler.post(() -> {
            if (isRotated) {
                rotateFloatButton();
            }
        });
    }

    private void rotateFloatButton() {
        final RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if(!isRotated){
                    attendanceBtn.clearAnimation();
                    attendanceBtn.setShapeAppearanceModel(originalShapeAppearanceModel);
                    attendanceBtn.setImageResource(R.drawable.ic_plus);
                }
            }
        });

        attendanceBtn.startAnimation(rotateAnimation);
    }
    @SuppressLint("MissingPermission")
    private void startLocationUpdates(){
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(locationSettingsResponse -> {
                    Log.d("Location", "location setting ok");
                    mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                })
                .addOnFailureListener(e -> {
                    isRotated = false;
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                    Log.d("err", e.toString());
                });
    }

    private void stopLocationUpdates(){
        if(mFusedLocationProviderClient != null){
            mFusedLocationProviderClient.removeLocationUpdates(locationCallback)
                    .addOnCompleteListener(task -> {
                        Log.d("task", task.toString());
                    });
        }
    }

    private void receiveLocation(LocationResult locationResult){
        location = locationResult.getLastLocation();
        if(location != null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.e("location", latitude + "; " +longitude);
            isRotated = false;
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            startClock(session.getTimeEnd());
            stopLocationUpdates();
        }
    }

    public void init(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        settingsClient = LocationServices.getSettingsClient(context);
        locationCallback= new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                receiveLocation(locationResult);
            }

            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };
        locationRequest = LocationRequest.create()
                .setInterval(5000)
                .setFastestInterval(500)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(100);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addAllLocationRequests(Collections.singleton(locationRequest));
        locationSettingsRequest= builder.build();
        startLocationUpdates();
    }

    private void startClock(Date timeEnd) {
        long endTime = timeEnd.getTime();
        long currentTime = new Date().getTime();
        long countDown = endTime - currentTime;

        Log.e("" + endTime, currentTime+"");
        new CountDownTimer(countDown, 1000) {
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long hours = seconds / 3600;
                seconds %= 3600;
                long minutes = seconds / 60;
                seconds %= 60;
                String timeRemaining = String.format("Time remaining: %02dH : %02dM : %02dS", hours, minutes, seconds);
                clock.setText(timeRemaining);
            }

            public void onFinish() {
                clock.setText("Time remaining: 00M : 00S");
            }
        }.start();
    }

    private void loadData(Session session){
        attendanceViewModel
                .checkAttendanceUser(new Attendance(new User(DataLocalManager.getUserId()), session.getClassroom(), session.getQr()))
                .observe(this, data -> {
                    if (data.equals("checked")) {
                        runOnUiThread(() -> {
                            checked.setVisibility(View.VISIBLE);
                            check.setVisibility(View.GONE);
                        });
                    } else {
                        runOnUiThread(() -> {
                            checked.setVisibility(View.GONE);
                            check.setVisibility(View.VISIBLE);
                        });
                    }
                });
    }
}
