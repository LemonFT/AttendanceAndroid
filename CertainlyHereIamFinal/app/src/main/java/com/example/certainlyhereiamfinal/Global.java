package com.example.certainlyhereiamfinal;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.certainlyhereiamfinal.activity.AttendanceActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Global {
    public static final int REQUEST_CODE_PERMISSIONS = 123;
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public static final int[] COLOR_CUSTOM = {
            rgb("3dcd1b"), rgb("#cd1b25"), rgb("#e74c3c"), rgb("#3498db")
    };

    public static String[] REQUIRED_PERMISSIONS = {
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static String[] REQUIRED_PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static String[] REQUIRED_PERMISSIONS_CAMERA = {
            android.Manifest.permission.CAMERA
    };

    public static String[] REQUIRED_PERMISSIONS_LOCATION = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static boolean allPermissionsGranted(Context context) {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean permissionCameraGranted(Context context){
        for (String permission : REQUIRED_PERMISSIONS_CAMERA) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean permissionLocationGranted(Context context) {
        for (String permission : REQUIRED_PERMISSIONS_LOCATION) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    public static boolean permissionStorageGranted(Context context){
        for (String permission : REQUIRED_PERMISSIONS_STORAGE) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    public static void requestPermissionCamera(Activity activity){
        if (!permissionCameraGranted(activity)) {
            ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS_CAMERA, REQUEST_CODE_PERMISSIONS);
        }
    }
    public static void requestPermissionLocation(Activity activity){
        if (!permissionLocationGranted(activity)) {
            ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS_LOCATION, REQUEST_CODE_PERMISSIONS);
        }
    }

    public static void requestPermissionStorage(Activity activity){
        if (!permissionStorageGranted(activity)) {
            Log.e("abc", "abc");
            ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS_STORAGE, REQUEST_CODE_PERMISSIONS);
        }
    }

    public static void showAlert(String content, Context context){
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.toast_background_error);
        TextView text = view.findViewById(android.R.id.message);
        text.setMinWidth(500);
        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        text.setTextColor(Color.WHITE);
        toast.setGravity(Gravity.CENTER, 0, -300);
        toast.show();
    }

    public static void showAlertSuccess(String content, Context context){
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.toast_background_success);
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);
        text.setMinWidth(500);
        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        toast.setGravity(Gravity.CENTER, 0, -300);
        toast.show();
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String cutString(String string, int maxLength) {
        if (string.length() > maxLength) {
            return string.substring(0, maxLength - 3) + "...";
        } else {
            return string;
        }
    }


}
