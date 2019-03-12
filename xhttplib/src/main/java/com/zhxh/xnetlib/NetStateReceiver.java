package com.zhxh.xnetlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zhxh.xnetlib.annotation.Network;
import com.zhxh.xnetlib.bean.MethodManager;
import com.zhxh.xnetlib.type.NetType;
import com.zhxh.xnetlib.utlis.Constants;
import com.zhxh.xnetlib.utlis.NetworkUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetStateReceiver extends BroadcastReceiver {

    private NetType netType;


    private Map<Object, List<MethodManager>> networkList = new HashMap<>();

    public NetStateReceiver() {
        this.netType = NetType.NONE;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null || null == intent.getAction()) {
            Log.e(Constants.TAG, "异常");
            return;
        }

        if (intent.getAction().equalsIgnoreCase(Constants.ANDROID_NET_CHANGE_ACTION)) {
            Log.e(Constants.TAG, "网络改变");

            netType = NetworkUtils.getNetType();

            if (NetworkUtils.isNetworkAvailable()) {
                Log.e(Constants.TAG, "网络连接成功");
            } else {
                Log.e(Constants.TAG, "么有网络连接");
            }

            //将所有订阅网络的Activity中的所有方法分发，结果在networkList

            post(netType);

        }
    }

    private void post(final NetType netType) {
        Set<Object> set = networkList.keySet();

        for (final Object getter : set) {
            List<MethodManager> methodList = networkList.get(getter);
            if (methodList != null) {
                for (final MethodManager method :
                        methodList) {

                    if (method.getType().isAssignableFrom(netType.getClass())) {//参数匹配
                        switch (method.getNetType()) {
                            case AUTO:
                                invoke(method, getter, netType);
                                break;
                            case WIFI:

                                if (netType == NetType.WIFI || netType == NetType.NONE) {
                                    invoke(method, getter, netType);

                                }
                                break;
                            case CMWAP:
                                if (netType == NetType.CMWAP || netType == NetType.NONE) {
                                    invoke(method, getter, netType);

                                }
                                break;
                            case CMNET:
                                if (netType == NetType.CMNET || netType == NetType.NONE) {
                                    invoke(method, getter, netType);

                                }
                                break;
                        }
                    }

                }
            }

        }
    }

    private void invoke(MethodManager methodManager, Object getter, NetType netType) {

        Method method = methodManager.getMethod();
        try {
            method.invoke(getter, netType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public void unRegisterObserver(Object obj) {

        if (!networkList.isEmpty()) {
            networkList.remove(obj);
        }
        Log.e(Constants.TAG, obj.getClass().getName() + "注销成功");

    }

    public void unRegisterAllObserver() {
        if (!networkList.isEmpty()) {
            networkList.clear();
        }

        NetworkManager.getDefault().getApplication().unregisterReceiver(this);

        networkList = null;
        Log.e(Constants.TAG, "全部注销成功");

    }

    public void registerObserver(Object obj) {

        //获取对象所有注解方法
        List<MethodManager> methodList = networkList.get(obj);
        if (methodList == null) {
            //通过反射获取方法
            methodList = findAnnotationMethod(obj);
            networkList.put(obj, methodList);
        }

    }

    private List<MethodManager> findAnnotationMethod(Object obj) {
        List<MethodManager> methodList = new ArrayList<>();

        Class<?> claz = obj.getClass();
        Method[] methods = claz.getMethods();

        for (Method method : methods) {
            Network network = method.getAnnotation(Network.class);

            if (network==null) {//过滤
                continue;
            }

            Type returnType = method.getGenericReturnType();
            if (!"void".equals(returnType.toString())) {
                throw new RuntimeException("返回值必须为void");
            }

            Class<?>[] parameterTypes = method.getParameterTypes();

            if (parameterTypes.length != 1) {
                throw new RuntimeException("参数个数必须为1");
            }

            MethodManager manager = new MethodManager(parameterTypes[0], network.netType(), method);

            methodList.add(manager);
        }

        //优化：反射不停的找父类
        return methodList;

    }
}