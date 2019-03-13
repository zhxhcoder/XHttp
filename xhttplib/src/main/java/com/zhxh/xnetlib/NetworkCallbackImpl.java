package com.zhxh.xnetlib;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.zhxh.xnetlib.utlis.Constants;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {

    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);

        Log.e(Constants.TAG, "onAvailable");
    }

    @Override
    public void onLost(Network network) {
        super.onLost(network);

        Log.e(Constants.TAG, "onLost");
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);

        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.e(Constants.TAG, "onCapabilitiesChanged: wifi");
            } else {
                Log.e(Constants.TAG, "onCapabilitiesChanged: cellular");
            }
        }
    }
}
