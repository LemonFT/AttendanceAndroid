package com.example.certainlyhereiamfinal.fragment;

import static com.example.certainlyhereiamfinal.Global.permissionLocationGranted;
import static com.example.certainlyhereiamfinal.Global.requestPermissionLocation;
import static com.example.certainlyhereiamfinal.Global.showAlert;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.certainlyhereiamfinal.R;
import com.example.certainlyhereiamfinal.adapter.SessionAdapter;
import com.example.certainlyhereiamfinal.adapter.SessionItemListenner;
import com.example.certainlyhereiamfinal.model.Classroom;
import com.example.certainlyhereiamfinal.model.Session;
import com.example.certainlyhereiamfinal.store.DataLocalManager;
import com.example.certainlyhereiamfinal.viewmodel.MemberViewModel;
import com.example.certainlyhereiamfinal.viewmodel.SessionViewModel;
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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SessionsFragment extends Fragment implements SessionItemListenner {

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback locationCallback;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private Location location;
    private RecyclerView recyclerView;
    private SessionAdapter sessionAdapter;
    private SessionViewModel sessionViewModel;
    private MemberViewModel memberViewModel;
    private RelativeLayout create_session_form, update_session_form;
    private BottomSheetBehavior bottomSheetBehavior, bottomSheetBehavior_update;
    private FloatingActionButton btn_open_create_session_form;
    private EditText edt_time_end, edt_room, edt_location;
    private EditText edt_update_time_end, edt_update_room, edt_update_location;
    private Button btn_create_session, btn_update_session;
    private TextView total_session, number_member;
    private double latitude, longitude;
    private Context context;
    private Activity activity;
    private boolean isRotated = false;
    private Handler handler;

    private ShapeAppearanceModel originalShapeAppearanceModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sessions_fragment, container, false);
        sessionAdapter = new SessionAdapter(root.getContext(), true, this);
        recyclerView = root.findViewById(R.id.sessions_recyclerview);
        sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);
        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        loadSessions(root.getContext());

        context = this.getContext();
        activity = this.getActivity();

        create_session_form = root.findViewById(R.id.create_session_form);

        bottomSheetBehavior = BottomSheetBehavior.from(create_session_form);

        update_session_form = root.findViewById(R.id.update_session_form);

        bottomSheetBehavior_update = BottomSheetBehavior.from(update_session_form);

        btn_open_create_session_form = root.findViewById(R.id.btn_open_create_session_form);

        originalShapeAppearanceModel = btn_open_create_session_form.getShapeAppearanceModel();

        edt_room = root.findViewById(R.id.edt_room_session);

        edt_location = root.findViewById(R.id.edt_location_session);

        edt_time_end = root.findViewById(R.id.edt_time_end);

        edt_update_room = root.findViewById(R.id.edt_update_room_session);

        edt_update_location = root.findViewById(R.id.edt_update_location_session);

        edt_update_time_end = root.findViewById(R.id.edt_update_time_end);

        btn_create_session = root.findViewById(R.id.btn_create_session);

        btn_update_session = root.findViewById(R.id.btn_update_session);

        total_session = root.findViewById(R.id.total_session);

        number_member = root.findViewById(R.id.number_member);

        handler = new Handler(Looper.getMainLooper());

        btn_open_create_session_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    onFloatButtonClick();
                    if (!permissionLocationGranted(context)) {
                        requestPermissionLocation(activity);
                        init();
                    } else {
                        init();
                    }
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        edt_time_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeEndToEdt(v, edt_time_end);
            }
        });

        edt_update_time_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeEndToEdt(v, edt_update_time_end);
            }
        });

        btn_create_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date timeEnd = convertStringToDate(edt_time_end.getText().toString());
                String roomStr = edt_room.getText().toString();
                String locationStr = edt_location.getText().toString();
                Long classId = DataLocalManager.getClassId();
                Session session = new Session(roomStr, new Date(), timeEnd, new Classroom(classId), latitude, longitude);
                if (roomStr.length() == 0 || locationStr.length() == 0 || edt_time_end.getText().length() == 0) {
                    showAlert("Check information and try again!", SessionsFragment.this.getContext());
                    return;
                }
                sessionViewModel.insertSession(session).observe((LifecycleOwner) root.getContext(), data -> {
                    if (data != null) {
                        loadSessions(root.getContext());
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    } else {
                        showAlert("Please, check internet and try again!", SessionsFragment.this.getContext());
                    }
                });
            }
        });

        return root;
    }

    public void onFloatButtonClick() {
        isRotated = true;
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel.Builder()
                .setAllCorners(CornerFamily.ROUNDED, 100)
                .build();
        btn_open_create_session_form.setShapeAppearanceModel(shapeAppearanceModel);
        btn_open_create_session_form.setImageResource(R.drawable.ic_loadright);
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
                    btn_open_create_session_form.clearAnimation();
                    btn_open_create_session_form.setShapeAppearanceModel(originalShapeAppearanceModel);
                    btn_open_create_session_form.setImageResource(R.drawable.ic_plus);
                }
            }
        });

        btn_open_create_session_form.startAnimation(rotateAnimation);
    }



    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(locationSettingsResponse -> {
                    Log.d("Location", "location setting ok");
                    mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                })
                .addOnFailureListener(e -> {
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                    Log.d("err", e.toString());
                });
    }

    private void stopLocationUpdates() {
        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient.removeLocationUpdates(locationCallback)
                    .addOnCompleteListener(task -> {
                        Log.d("task", task.toString());
                    });
        }
    }

    private void receiveLocation(LocationResult locationResult) {
        location = locationResult.getLastLocation();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        onDestroy();
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            edt_location.setText(addressList.get(0).getAddressLine(0));
            if (!edt_location.getText().equals("")) {
                Calendar calendar = Calendar.getInstance();
                String selectedTime = (calendar.get(Calendar.HOUR_OF_DAY) + 1) + ":" + calendar.get(Calendar.MINUTE) + ":00" + " " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
                edt_time_end.setText(selectedTime);
                isRotated = false;
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void init() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        settingsClient = LocationServices.getSettingsClient(context);
        locationCallback = new LocationCallback() {
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
        locationSettingsRequest = builder.build();
        startLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }


    public void setTimeEndToEdt(View root, EditText edt) {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String selectedTime = (hourOfDay + 1) + ":" + minute + ":00" + " " + dayOfMonth + "/" + (month + 1) + "/" + year;
        edt.setText(selectedTime);
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                root.getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = hourOfDay + ":" + minute + ":00" + " " + dayOfMonth + "/" + (month + 1) + "/" + year;
                        edt.setText(selectedTime);
                    }
                },
                hourOfDay,
                minute,
                true
        );

        timePickerDialog.show();
    }

    public void loadSessions(Context context) {
        memberViewModel.getNumberMember(DataLocalManager.getClassId()).observe((LifecycleOwner) context, data -> {
            number_member.setText("Members: " + data);
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        sessionViewModel.findAllSessionClass(DataLocalManager.getClassId()).observe((LifecycleOwner) requireContext(), data -> {
            if (data != null) {
                sessionAdapter.setSessions(data);
                total_session.setText("Total sessions: " + data.size());
                recyclerView.setAdapter(sessionAdapter);
            }
        });
    }


    public Date convertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        try {
            Date date = dateFormat.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String convertDateToString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return hourOfDay + ":" + minute + ":00" + " " + dayOfMonth + "/" + month + "/" + year;
    }

    @Override
    public void onSeeMoreClicked(View view, Session session) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);

        MenuInflater inflater = popupMenu.getMenuInflater();

        inflater.inflate(R.menu.menu_seemore_session, popupMenu.getMenu());

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.update) {
                    if (bottomSheetBehavior_update.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheetBehavior_update.setState(BottomSheetBehavior.STATE_EXPANDED);
                    } else {
                        bottomSheetBehavior_update.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                    edt_update_room.setText(session.getRoom());
                    edt_update_location.setText("[" + session.getLatitude() + "; " + session.getLongitude() + "]");
                    edt_update_time_end.setText(convertDateToString(session.getTimeEnd()));
                } else if (item.getItemId() == R.id.delete) {
                    sessionViewModel.deleteSession(session.getId()).observe((LifecycleOwner) view.getContext(), data -> {
                        if (data.equals("200")) {
                            loadSessions(view.getContext());
                        } else {
                            showAlert("Delete failed, check internet and try again!", SessionsFragment.this.getContext());
                        }
                    });
                }
                return true;
            }
        });

        btn_update_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date timeEnd = convertStringToDate(edt_update_time_end.getText().toString());
                String roomStr = edt_update_room.getText().toString();
                String locationStr = edt_update_location.getText().toString();
                Long classId = DataLocalManager.getClassId();
                Session sessionUpdate = new Session(session.getId(), roomStr, new Date(), timeEnd, new Classroom(classId), session.getQr(), session.getLatitude(), session.getLongitude());
                if (roomStr.length() == 0 || locationStr.length() == 0 || edt_update_time_end.getText().length() == 0) {
                    showAlert("Check information and try again!", SessionsFragment.this.getContext());
                    return;
                }
                sessionViewModel.updateSession(sessionUpdate).observe((LifecycleOwner) view.getContext(), data -> {
                    if (data != null) {
                        loadSessions(view.getContext());
                        bottomSheetBehavior_update.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    } else {
                        showAlert("Please, check internet and try again!", SessionsFragment.this.getContext());
                    }
                });
            }
        });
    }

}
