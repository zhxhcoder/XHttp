package com.zhxh.xnetlib;

import android.app.Application;
import android.content.IntentFilter;

import com.zhxh.xnetlib.utlis.Constants;

public class NetworkManager {

    private static volatile NetworkManager instance;

    private Application application;
    private NetStateReceiver receiver;

    private NetworkManager() {

        receiver = new NetStateReceiver();
    }

    public static NetworkManager getDefault() {
        if (instance == null) {
            synchronized (NetworkManager.class) {
                if (instance == null) {
                    instance = new NetworkManager();
                }
            }
        }

        return instance;
    }


    //广播初始化

    public void init(Application application) {
        this.application = application;

        //注册
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ANDROID_NET_CHANGE_ACTION);
        application.registerReceiver(receiver, filter);

    }


    public Application getApplication() {

        if (application == null) {
            throw new RuntimeException("未初始化");
        }

        return application;
    }

    public void unRegisterObserver(Object obj) {
        receiver.unRegisterObserver(obj);

    }

    public void unRegisterAllObserver() {
        receiver.unRegisterAllObserver();
    }

    public void registerObserver(Object obj) {

        receiver.registerObserver(obj);
    }
}
