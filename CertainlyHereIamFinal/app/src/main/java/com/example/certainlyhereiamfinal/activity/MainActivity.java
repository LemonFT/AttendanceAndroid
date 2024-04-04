package com.example.certainlyhereiamfinal.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.certainlyhereiamfinal.R;
import com.example.certainlyhereiamfinal.adapter.ClassroomAdapter;
import com.example.certainlyhereiamfinal.adapter.ClassroomItemListenner;
import com.example.certainlyhereiamfinal.model.Classroom;
import com.example.certainlyhereiamfinal.model.User;
import com.example.certainlyhereiamfinal.store.DataLocalManager;
import com.example.certainlyhereiamfinal.viewmodel.ClassroomViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ClassroomItemListenner {

    private DrawerLayout drawerLayout;
    private ImageView button_open_navigationview, join_class;
    private NavigationView navigationView;

    private FloatingActionButton open_create_form_classroom;

    private ClassroomAdapter classroomAdapter;
    private RecyclerView recyclerView;
    private ClassroomViewModel classroomViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        button_open_navigationview = findViewById(R.id.open_navigationview);

        navigationView = findViewById(R.id.navigation_view);

        open_create_form_classroom = findViewById(R.id.open_create_form_classroom);

        join_class = findViewById(R.id.join_class);

        recyclerView = findViewById(R.id.recycler_classrooms);

        classroomAdapter = new ClassroomAdapter(this, this);

        classroomViewModel = new ViewModelProvider(this).get(ClassroomViewModel.class);

        loadingdata();

        button_open_navigationview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(navigationView);
            }
        });

        open_create_form_classroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateForm();
            }
        });

        join_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openJoinForm();
            }
        });
    }

    public void loadingdata(){
        List<Classroom> classrooms = new ArrayList<>();
        classroomViewModel.findAllClassroomByUserId(DataLocalManager.getUserId()).observe(MainActivity.this, data -> {
            if(data != null) {
                classrooms.addAll(data);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                classroomAdapter.setClassrooms(classrooms);
                recyclerView.setAdapter(classroomAdapter);
            }
        });
    }

    //create class
    public void openCreateForm(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.create_classroom_popup);

        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        dialog.setCancelable(true);
        EditText editText = dialog.findViewById(R.id.edt_class_name);
        Button cancelBtn = dialog.findViewById(R.id.cancel_btn);
        Button createBtn = dialog.findViewById(R.id.create_btn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        }) ;
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String className = editText.getText().toString();
                if(className.equals("") || className.equals(" ")){
                    showAlert("Please, check your information again!");
                }else {
                    User user = new User(DataLocalManager.getUserId());
                    classroomViewModel.insertClassroom(new Classroom(className, user)).observe(MainActivity.this, data -> {
                        if (data != null){
                            loadingdata();
                            dialog.dismiss();
                        }else {
                            showAlert("Class name already exist");
                        }
                    });
                }
            }
        });
        dialog.show();
    }

    public void openJoinForm(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.join_class_bottomsheet);

        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        dialog.setCancelable(true);
        EditText editText = dialog.findViewById(R.id.edt_class_name);
        Button cancelBtn = dialog.findViewById(R.id.cancel_btn);
        Button joinBtn = dialog.findViewById(R.id.join_btn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        }) ;
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int class_id = getIdClassFromUrl(editText.getText().toString());
                if(class_id == -1){
                    Toast.makeText(v.getContext(), "Url invalid!", Toast.LENGTH_SHORT).show();
                }
//                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private int getIdClassFromUrl(String url){
        int startIndex = url.indexOf("certainlyhereiam?join_classid=");
        if (startIndex != -1) {
            String classIdString = url.substring(startIndex + "certainlyhereiam?join_classid=".length());
            try {
                return Integer.parseInt(classIdString);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    public static void copyText(Context context, String textToCopy) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            // Create a ClipData object to store the copied text
            ClipData clip = ClipData.newPlainText("Copied Text", textToCopy);
            // Set the data to clipboard
            clipboard.setPrimaryClip(clip);
            // Show a toast message to indicate successful copy
            Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
        } else {
            // Show a toast message if clipboard is not available
            Toast.makeText(context, "Clipboard not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSeeMoreClicked(View view, Classroom classroom) {
        PopupMenu popupMenu = new PopupMenu(this, view);

        MenuInflater inflater = popupMenu.getMenuInflater();

        User user = new Gson().fromJson(DataLocalManager.getUser(), User.class);

        if(user != null && user.getId() == classroom.getUser().getId()){
            inflater.inflate(R.menu.menu_seemore, popupMenu.getMenu());
        }else{
            inflater.inflate(R.menu.menu_seemore_member, popupMenu.getMenu());
        }
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.edit){
                    openUpdateForm(classroom.getId());
                }else if(item.getItemId() == R.id.copy){
                    copyText(view.getContext(), "certainlyhereiam?join_classid="+classroom.getId());
                }
                return true;
            }
        });
    }

    public void openUpdateForm(Long classId){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_classroom_popup);

        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        dialog.setCancelable(true);
        EditText editText = dialog.findViewById(R.id.edt_class_name);
        Button cancelBtn = dialog.findViewById(R.id.cancel_btn);
        Button updateBtn = dialog.findViewById(R.id.update_btn);


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        }) ;
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String className = editText.getText().toString();
                if(className.equals("") || className.equals(" ")){
                    showAlert("Please, check your information again!");
                }else {
                    User user = new User(DataLocalManager.getUserId());
                    classroomViewModel.updateClassroom(new Classroom(classId, className, user)).observe(MainActivity.this, data -> {
                        if (data != null){
                            loadingdata();
                            dialog.dismiss();
                        }else {
                            showAlert("Class name already exist");
                        }
                    });
                }
            }
        });

        dialog.show();
    }

    public void showAlert(String content){
        Toast toast = Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundColor(Color.RED);
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }



}