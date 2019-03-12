package com.zhxh.xnetlib;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

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

    @SuppressWarnings("MissingPermission")
    public void init(Application application) {
        this.application = application;

        //第一种：注册广播的形式
        //IntentFilter filter = new IntentFilter();
        //filter.addAction(Constants.ANDROID_NET_CHANGE_ACTION);
        //application.registerReceiver(receiver, filter);

        //第二种方式监听，不通过广播

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager.NetworkCallback networkCallback = new NetworkCallbackImpl();

            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager connectivityManager = (ConnectivityManager) NetworkManager.getDefault().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                connectivityManager.registerNetworkCallback(request, networkCallback);
            }
        }
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
