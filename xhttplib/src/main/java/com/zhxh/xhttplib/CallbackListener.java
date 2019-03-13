package com.zhxh.xhttplib;

import java.io.InputStream;

public interface CallbackListener {
    void onSuccess(InputStream inputStream);

    void onFailure();
}
