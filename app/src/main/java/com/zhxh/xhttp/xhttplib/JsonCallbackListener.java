package com.zhxh.xhttp.xhttplib;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonCallbackListener<T> implements CallbackListener {

    private Class<T> responseClass;
    private IJsonDataListener iJsonDataListener;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public JsonCallbackListener(Class<T> responseClass, IJsonDataListener iJsonDataListener) {
        this.responseClass = responseClass;
        this.iJsonDataListener = iJsonDataListener;
    }

    @Override
    public void onSuccess(InputStream inputStream) {

        String reponse = getContent(inputStream);
        final T claz = JSON.parseObject(reponse, responseClass);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                iJsonDataListener.onSuccess(claz);
            }
        });

    }

    private String getContent(InputStream inputStream) {
        String content = null;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {

                System.out.println("错误信息--" + e.toString());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("错误信息--" + e.toString());
                }
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }

    @Override
    public void onFailure() {

    }
}
