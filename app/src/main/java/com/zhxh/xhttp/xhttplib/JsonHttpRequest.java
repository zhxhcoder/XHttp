package com.zhxh.xhttp.xhttplib;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonHttpRequest implements IHttpRequest {

    private String url;
    private byte[] data;
    private CallbackListener listener;
    private HttpURLConnection connection;
    private InputStream is;

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void setListener(CallbackListener listener) {
        this.listener = listener;
    }

    @Override
    public void execute() {
        URL url = null;
        try {
            url = new URL(this.url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "applicaton/json;chartset=UTF-8");//设置消息类型
            connection.connect();


            //使用字节流发送数据
            OutputStream ou = connection.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(ou);
            bos.write(data);
            bos.flush();
            ou.close();
            bos.close();

            //字符流写入数据

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream io = connection.getInputStream();
                listener.onSuccess(io);
            } else {
                throw new RuntimeException("请求失败");
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }


    }
}
