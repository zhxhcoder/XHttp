package com.zhxh.xnetlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zhxh.xnetlib.listener.NetworkListener;
import com.zhxh.xnetlib.type.NetType;
import com.zhxh.xnetlib.utlis.Constants;
import com.zhxh.xnetlib.utlis.NetworkUtils;

public class NetStateReceiver extends BroadcastReceiver {

    private NetType netType;
    private NetworkListener listener;

    public NetStateReceiver() {
        this.netType = NetType.NONE;
    }


    public void setListener(NetworkListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null || null == intent.getAction()) {
            Log.e(Constants.TAG, "异常");
            return;
        }

        if (intent.getAction().equalsIgnoreCase(Constants.ANDROID_NET_CHANGE_ACTION)) {
            Log.e(Constants.TAG, "网络改变");

            netType = NetworkUtils.getNetType(context);

            if (NetworkUtils.isNetworkAvailable(context)) {

                listener.onConnect(netType);
            } else {

                listener.onDisConnect();
            }

        }
    }
}