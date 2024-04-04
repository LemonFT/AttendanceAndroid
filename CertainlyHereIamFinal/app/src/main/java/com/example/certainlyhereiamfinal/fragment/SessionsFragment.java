package com.example.certainlyhereiamfinal.fragment;

import static androidx.camera.core.impl.utils.ContextUtil.getApplicationContext;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.example.certainlyhereiamfinal.model.User;
import com.example.certainlyhereiamfinal.store.DataLocalManager;
import com.example.certainlyhereiamfinal.viewmodel.MemberViewModel;
import com.example.certainlyhereiamfinal.viewmodel.SessionViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SessionsFragment extends Fragment implements SessionItemListenner {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sessions_fragment, container, false);
        sessionAdapter = new SessionAdapter(root.getContext(), true, this);
        recyclerView = root.findViewById(R.id.sessions_recyclerview);
        sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);
        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        loadSessions(root.getContext());

        create_session_form = root.findViewById(R.id.create_session_form);

        bottomSheetBehavior = BottomSheetBehavior.from(create_session_form);

        update_session_form = root.findViewById(R.id.update_session_form);

        bottomSheetBehavior_update = BottomSheetBehavior.from(update_session_form);

        btn_open_create_session_form = root.findViewById(R.id.btn_open_create_session_form);

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

        btn_open_create_session_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else {
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
                Session session = new Session(roomStr, locationStr, new Date(), timeEnd, new Classroom(classId));
                sessionViewModel.insertSession(session).observe((LifecycleOwner) root.getContext(), data -> {
                    if(data != null){
                        loadSessions(root.getContext());
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }else{
                        showAlert("Please, check internet and try again!");
                    }
                });
            }
        });

        return root;
    }

    public void setTimeEndToEdt(View root, EditText edt){
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                root.getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = hourOfDay + ":" + minute + ":00" +" "+ dayOfMonth + "/" + (month + 1) + "/" + year;
                        edt.setText(selectedTime);
                    }
                },
                hourOfDay,
                minute,
                true
        );

        timePickerDialog.show();
    }
    public void loadSessions(Context context){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        sessionViewModel.findAllSessionClass(DataLocalManager.getClassId()).observe((LifecycleOwner) requireContext(), data -> {
            if(data != null){
                sessionAdapter.setSessions(data);
                total_session.setText("Total sessions: "+data.size());
                recyclerView.setAdapter(sessionAdapter);
            }
        });
        memberViewModel.getNumberMember(DataLocalManager.getClassId()).observe((LifecycleOwner) context, data -> {
            number_member.setText("Members: "+data);
        });
    }

    public void showAlert(String content){
        Toast toast = Toast.makeText(SessionsFragment.this.getContext(), content, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundColor(Color.RED);
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public Date convertStringToDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        try {
            Date date = dateFormat.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String convertDateToString(Date date){
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

        User user = new Gson().fromJson(DataLocalManager.getUser(), User.class);

        inflater.inflate(R.menu.menu_seemore_session, popupMenu.getMenu());

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.update){
                    if(bottomSheetBehavior_update.getState() != BottomSheetBehavior.STATE_EXPANDED){
                        bottomSheetBehavior_update.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }else {
                        bottomSheetBehavior_update.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                    edt_update_room.setText(session.getRoom());
                    edt_update_location.setText(session.getLocation());
                    edt_update_time_end .setText(convertDateToString(session.getTimeEnd()));
                }else if(item.getItemId() == R.id.delete){
                    sessionViewModel.deleteSession(session.getId()).observe((LifecycleOwner) view.getContext(), data -> {
                        if(data.equals("200")){
                            loadSessions(view.getContext());
                        }else {
                            showAlert("Delete failed, check internet and try again!");
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
                Session sessionUpdate = new Session(session.getId(), roomStr, locationStr, new Date(), timeEnd, new Classroom(classId), session.getQr());
                sessionViewModel.updateSession(sessionUpdate).observe((LifecycleOwner) view.getContext(), data -> {
                    if(data != null){
                        loadSessions(view.getContext());
                        bottomSheetBehavior_update.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }else{
                        showAlert("Please, check internet and try again!");
                    }
                });
            }
        });
    }
}
