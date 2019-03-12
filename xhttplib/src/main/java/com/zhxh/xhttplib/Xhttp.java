package com.zhxh.xhttplib;

public class Xhttp {


    public static <T,R> void sendJsonRequest(String url,T requestData,Class<R> response,IJsonDataListener listener){
        IHttpRequest httpRequest=new JsonHttpRequest();
        CallbackListener callbackListener=new JsonCallbackListener<>(response,listener);
        HttpTask httpTask=new HttpTask(url,requestData,httpRequest,callbackListener);
        ThreadPoolManager.getInstance().addTask(httpTask);
    }
}
