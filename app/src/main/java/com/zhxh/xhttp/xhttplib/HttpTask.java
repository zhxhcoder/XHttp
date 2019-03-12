package com.zhxh.xhttp.xhttplib;

import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;

public class HttpTask<T> implements Runnable {

    IHttpRequest httpRequest;

    public HttpTask(String url, T requestData, IHttpRequest httpRequest, CallbackListener listener) {

        this.httpRequest = httpRequest;

        httpRequest.setUrl(url);
        httpRequest.setListener(listener);

        String content = JSON.toJSONString(requestData);
        try {
            httpRequest.setData(content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        httpRequest.execute();
    }
}
