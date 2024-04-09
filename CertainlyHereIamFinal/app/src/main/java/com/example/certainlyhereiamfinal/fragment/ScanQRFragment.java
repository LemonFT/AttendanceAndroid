package com.example.certainlyhereiamfinal.fragment;


import static com.example.certainlyhereiamfinal.Global.permissionCameraGranted;
import static com.example.certainlyhereiamfinal.Global.permissionLocationGranted;
import static com.example.certainlyhereiamfinal.Global.requestPermissionCamera;
import static com.example.certainlyhereiamfinal.Global.requestPermissionLocation;
import static com.example.certainlyhereiamfinal.Global.showAlert;
import static com.example.certainlyhereiamfinal.Global.showAlertSuccess;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.certainlyhereiamfinal.R;
import com.example.certainlyhereiamfinal.activity.AttendanceMemberActivity;
import com.example.certainlyhereiamfinal.model.Attendance;
import com.example.certainlyhereiamfinal.model.Classroom;
import com.example.certainlyhereiamfinal.model.User;
import com.example.certainlyhereiamfinal.store.DataLocalManager;
import com.example.certainlyhereiamfinal.viewmodel.AttendanceViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScanQRFragment extends Fragment {

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback locationCallback;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private Location location;
    private CodeScannerView scannerView;
    private  CodeScanner mCodeScanner;
    private AttendanceViewModel attendanceViewModel;
    private Context context;
    private FloatingActionButton start_camera;
    private double latitude, longitude;
    private boolean isRotated = false;
    private boolean locationCheck = false;
    private Handler handler;
    private ShapeAppearanceModel originalShapeAppearanceModel;
    private int zoom = 1;
    private FloatingActionButton resetzoom;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.scan_qr_fragment, container, false);
        attendanceViewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);

        scannerView = rootView.findViewById(R.id.scanner_view);

        mCodeScanner = new CodeScanner(this.getContext(), scannerView);

        handler = new Handler(Looper.getMainLooper());

        context = this.getContext();

        start_camera = rootView.findViewById(R.id.start_camera);

        resetzoom = rootView.findViewById(R.id.zoom_reset);

        originalShapeAppearanceModel = start_camera.getShapeAppearanceModel();
        start_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!permissionLocationGranted(context)){
                    requestPermissionLocation(ScanQRFragment.this.getActivity());
                }else if(!permissionCameraGranted(context)){
                    requestPermissionCamera(ScanQRFragment.this.getActivity());
                }else{
                    onFloatButtonClick();
                    init();
                }

            }
        });

        resetzoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoom = 1;
                mCodeScanner.setZoom(1);
            }
        });
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        User user = new User(DataLocalManager.getUserId());
                        Classroom classroom = new Classroom(DataLocalManager.getClassId());
                        String qr = result.toString();

                        attendanceViewModel.
                                insertAttendanceMemberQr(new Attendance(user, classroom, qr, new Date()), latitude, longitude)
                                .observe((LifecycleOwner) ScanQRFragment.this.getContext(), data -> {
                                    if(data.equals("Attendance succcessfully")){
                                        showAlertSuccess("Attendance succcessfully", ScanQRFragment.this.getContext());

                                    }else{
                                        showAlert(data, ScanQRFragment.this.getContext());
                                    }
                                });
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
        return rootView;
    }

    public void onFloatButtonClick() {
        if(locationCheck) {
            mCodeScanner.setZoom(zoom++);
            return;
        }
        isRotated = true;
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel.Builder()
                .setAllCorners(CornerFamily.ROUNDED, 300)
                .build();
        start_camera.setShapeAppearanceModel(shapeAppearanceModel);
        start_camera.setImageResource(R.drawable.ic_loadright);
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
                    start_camera.clearAnimation();
                    start_camera.setShapeAppearanceModel(originalShapeAppearanceModel);
                    start_camera.setImageResource(R.drawable.ic_plus);
                }
            }
        });

        start_camera.startAnimation(rotateAnimation);
    }
    @SuppressLint("MissingPermission")
    private void startLocationUpdates(){
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(locationSettingsResponse -> {
                    Log.d("Location", "location setting ok");
                    mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                })
                .addOnFailureListener(e -> {
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
            isRotated = false;
            locationCheck = true;
            mCodeScanner.startPreview();
            onDestroy();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }


    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

}
