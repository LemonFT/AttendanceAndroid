package com.example.certainlyhereiamfinal.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.certainlyhereiamfinal.R;
import com.example.certainlyhereiamfinal.model.FirstSign;
import com.example.certainlyhereiamfinal.store.DataLocalManager;
import com.example.certainlyhereiamfinal.viewmodel.SigninViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignInActivity extends AppCompatActivity {

    private Button open_register, btnSignIn, btnRegister;
    private RelativeLayout layout_register;
    private BottomSheetBehavior bottomSheetBehavior;
    EditText signinEdtEmail, signinEdtPwd, registerEdtEmail, registerEdtPwd;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private SigninViewModel signinViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_activity);
        open_register = findViewById(R.id.open_register);
        layout_register = findViewById(R.id.register_form);
        bottomSheetBehavior = BottomSheetBehavior.from(layout_register);
        signinEdtEmail = findViewById(R.id.signin_edtemail);
        signinEdtPwd = findViewById(R.id.signin_edtpwd);
        registerEdtEmail = findViewById(R.id.register_edtemail);
        registerEdtPwd = findViewById(R.id.register_edtpwd);
        btnSignIn = findViewById(R.id.btn_signin);
        btnRegister = findViewById(R.id.btn_register);

        signinViewModel = new ViewModelProvider(this).get(SigninViewModel.class);

        open_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signinEdtEmail.getText().toString();
                String password = signinEdtPwd.getText().toString();
                String resultValidate = validatingForm(email, password);
                if(resultValidate.equals("")){
                    signinViewModel.signIn(email, password).observe(SignInActivity.this, data -> {
                        if(data != null){
                            Gson gson = new Gson();
                            String tokens = gson.toJson(data.getTokens());
                            String user = gson.toJson(data.getUser());
                            DataLocalManager.setTokens(tokens);
                            DataLocalManager.setUser(user);
                            DataLocalManager.setUserId(data.getUser().getId());
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }else {
                    showAlert(resultValidate);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = registerEdtEmail.getText().toString();
                String password = registerEdtPwd.getText().toString();
                String resultValidate = validatingForm(email, password);
                if(resultValidate.equals("")){
                    signinViewModel.register(email, password).observe(SignInActivity.this, data -> {
                        if(data != null){
                            signinEdtEmail.setText(data.getEmail());
                            signinEdtPwd.setText(password);
                            registerEdtEmail.setText("");
                            registerEdtPwd.setText("");
                            Toast toast = Toast.makeText(getApplicationContext(), "Register is successfully", Toast.LENGTH_SHORT);
                            View view = toast.getView();
                            view.setBackgroundColor(Color.WHITE);
                            TextView text = view.findViewById(android.R.id.message);
                            text.setTextColor(Color.GREEN);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    });
                }else {
                    showAlert(resultValidate);
                }
            }
        });

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

    private String validatingForm(String email, String pwd){
        if(email.equals("") || pwd.equals(""))  return "Please, Check information again!";
        if(!isValidEmail(email))    return "Email invalid";
        if(pwd.length() <= 7) return "Password must be 8 characters long";
        return "";
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
