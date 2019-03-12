package com.zhxh.xnetlib.listener;

import com.zhxh.xnetlib.type.NetType;

public interface NetworkListener {

    void onConnect(NetType netType);

    void onDisConnect();

}
