package com.zhxh.xhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zhxh.xhttp.bean.BaseResponse;
import com.zhxh.xhttp.xhttplib.IJsonDataListener;
import com.zhxh.xhttp.xhttplib.Xhttp;

public class MainActivity extends AppCompatActivity {

    private TextView content;

    private String url = "https://v.juhe.cn/historyWeather/citys?province_id=1";
    private String errorUrl = "https://xxx?province_id=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content = findViewById(R.id.content);

        request();
    }

    private void request() {

        Xhttp.sendJsonRequest(errorUrl, null, BaseResponse.class, new IJsonDataListener<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse o) {
                content.setText(o.toString());
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
