package com.zhxh.xhttplib;

public interface IHttpRequest {
    void setUrl(String url);
    void setData(byte[] data);
    void setListener(CallbackListener listener);
    void execute();
}
