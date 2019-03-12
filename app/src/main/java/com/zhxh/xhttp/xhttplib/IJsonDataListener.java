package com.zhxh.xhttp.xhttplib;

import java.io.InputStream;

public interface IJsonDataListener<T> {

    void onSuccess(T t);
    void onFailure();

}

