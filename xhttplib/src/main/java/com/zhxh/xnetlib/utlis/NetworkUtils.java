package com.zhxh.xnetlib.utlis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zhxh.xnetlib.NetworkManager;
import com.zhxh.xnetlib.type.NetType;

public class NetworkUtils {

    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectManager = (ConnectivityManager) NetworkManager.getDefault().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectManager == null) {
            return false;
        }
        NetworkInfo[] info = connectManager.getAllNetworkInfo();

        if (info != null) {
            for (NetworkInfo aInfo : info) {
                if (aInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }


    @SuppressLint("MissingPermission")
    public static NetType getNetType() {
        ConnectivityManager connectManager = (ConnectivityManager) NetworkManager.getDefault().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectManager == null) {
            return NetType.NONE;
        }
        NetworkInfo info = connectManager.getActiveNetworkInfo();

        if (info == null) {
            return NetType.NONE;
        }

        int type = info.getType();

        if (type == ConnectivityManager.TYPE_MOBILE) {

            if (info.getExtraInfo().equalsIgnoreCase("cmnet")) {
                return NetType.CMNET;
            } else {
                return NetType.CMWAP;
            }

        } else if (type == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI;
        }
        return NetType.NONE;
    }

}
