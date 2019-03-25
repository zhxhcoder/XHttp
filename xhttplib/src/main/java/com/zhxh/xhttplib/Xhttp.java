package com.zhxh.xhttplib;

public class Xhttp {

    public static <T, R> void sendJsonRequest(String url, T requestData, Class<R> response, IJsonDataListener listener) {
        //请求
        IHttpRequest httpRequest = new JsonHttpRequest();
        //数据返回
        CallbackListener callbackListener = new JsonCallbackListener<>(response, listener);
        //封装task
        HttpTask httpTask = new HttpTask(url, requestData, httpRequest, callbackListener);
        //加入队列
        ThreadPoolManager.getInstance().addTask(httpTask);
    }
}
