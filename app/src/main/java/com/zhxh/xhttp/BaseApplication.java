package com.zhxh.xhttp;

import android.app.Application;

import com.zhxh.xnetlib.NetworkManager;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkManager.getDefault().init(this);
    }
}
