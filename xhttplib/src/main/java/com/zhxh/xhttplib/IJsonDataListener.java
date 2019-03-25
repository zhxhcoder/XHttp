package com.zhxh.xhttplib;

public interface IJsonDataListener<T> {

    void onSuccess(T t);

    void onFailure();
}

