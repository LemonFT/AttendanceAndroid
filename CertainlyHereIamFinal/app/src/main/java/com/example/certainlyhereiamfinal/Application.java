package com.example.certainlyhereiamfinal;

import com.example.certainlyhereiamfinal.store.DataLocalManager;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataLocalManager.init(getApplicationContext());
    }
}
