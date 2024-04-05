package com.example.certainlyhereiamfinal;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Global {
    public static void showAlert(String content, Context context){
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.toast_background_error);
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
