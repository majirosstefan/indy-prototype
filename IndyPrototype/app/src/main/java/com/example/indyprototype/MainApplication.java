package com.example.indyprototype;

import android.app.Application;
import android.system.ErrnoException;
import android.system.Os;
import android.util.Log;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
       // SoLoader.init(this, /* native exopackage */ false);
        try {
            //required for Indy SDK
            Os.setenv("EXTERNAL_STORAGE", this.getApplicationContext().getExternalFilesDir(null).getAbsolutePath(), true);
            System.loadLibrary("indy");
        } catch (ErrnoException e) {
            Log.e("indy_sdk","Unable to initialize Indy SDK");
        }
    }
}
