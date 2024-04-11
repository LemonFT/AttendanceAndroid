package com.example.certainlyhereiamfinal.activity;

import static com.example.certainlyhereiamfinal.Global.showAlert;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.certainlyhereiamfinal.Global;
import com.example.certainlyhereiamfinal.R;
import com.example.certainlyhereiamfinal.model.Classroom;
import com.example.certainlyhereiamfinal.model.User;
import com.example.certainlyhereiamfinal.store.DataLocalManager;
import com.example.certainlyhereiamfinal.viewmodel.SigninViewModel;

public class ForgotPwd extends AppCompatActivity {
    private EditText edt_email, edt_pwd, edt_pwdCf, edt_verifiCode;
    private Button resetPasswordButton;
    private SigninViewModel signinViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_activity);
        signinViewModel = new ViewModelProvider(this).get(SigninViewModel.class);
        edt_email = findViewById(R.id.emailEditText);
        edt_pwd = findViewById(R.id.newPasswordEditText);
        edt_pwdCf = findViewById(R.id.confirmPasswordEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = edt_email.getText().toString();
                String pwdStr = edt_pwd.getText().toString();
                String pwdCfStr = edt_pwdCf.getText().toString();
                if(emailStr.equals("") || pwdStr.equals("") || pwdCfStr.equals("")){
                    Global.showAlert("Check information and check again!", ForgotPwd.this);
                }else if(!Global.isValidEmail(emailStr)){
                    Global.showAlert("Email invalid", ForgotPwd.this);
                }else if(pwdStr.length() <= 7){
                    Global.showAlert("Password must be 8 characters long", ForgotPwd.this);
                }else if(!pwdStr.equals(pwdCfStr)){
                    Global.showAlert("Password confirm invalid", ForgotPwd.this);
                }else{
                    updateFocus(false);
                    signinViewModel.verifiEmail(new User(emailStr)).observe((LifecycleOwner) ForgotPwd.this, data -> {
                        if(data.equals("200")){
                            openCreateForm();
                        }else {
                            Global.showAlert("Email doesn't not exist", ForgotPwd.this);
                            updateFocus(true);
                        }
                    });
                }
            }
        });
    }

    public void updateFocus(boolean regime){
        edt_email.setFocusable(regime);
        edt_email.setFocusableInTouchMode(regime);
        edt_pwd.setFocusable(regime);
        edt_pwd.setFocusableInTouchMode(regime);
        edt_pwdCf.setFocusable(regime);
        edt_pwdCf.setFocusableInTouchMode(regime);
    }

    public void openCreateForm(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.verification_email_form);

        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        EditText editText = dialog.findViewById(R.id.edt_verificode);
        Button createBtn = dialog.findViewById(R.id.sendcode);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editText.getText().toString();
                if(!code.equals("")){
                    signinViewModel.updatePwd(new User(edt_email.getText().toString(), edt_pwd.getText().toString()), code)
                            .observe((LifecycleOwner) ForgotPwd.this, data -> {
                                if(data != null){
                                    Global.showAlertSuccess("Update password successful", ForgotPwd.this);
                                    updateFocus(true);
                                    dialog.dismiss();
                                    Intent intent = new Intent(ForgotPwd.this, SignInActivity.class);
                                    startActivity(intent);
                                }else{
                                    updateFocus(true);
                                    Global.showAlert("Verification code invalid!", ForgotPwd.this);
                                    dialog.dismiss();
                                }
                            });
                }
            }
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
