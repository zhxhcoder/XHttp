package com.zhxh.xhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zhxh.xhttp.bean.BaseResponse;
import com.zhxh.xhttplib.IJsonDataListener;
import com.zhxh.xhttplib.Xhttp;
import com.zhxh.xnetlib.NetworkManager;
import com.zhxh.xnetlib.listener.NetworkListener;
import com.zhxh.xnetlib.type.NetType;

public class MainActivity extends AppCompatActivity implements NetworkListener {

    private TextView content;
    private TextView netState;

    private String url = "https://v.juhe.cn/historyWeather/citys?province_id=1";
    private String errorUrl = "https://xxx?province_id=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        NetworkManager.getDefault().init(getApplication());
        NetworkManager.getDefault().setListener(this);

        content = findViewById(R.id.content);
        netState = findViewById(R.id.netState);

        request();
    }

    private void request() {
        Xhttp.sendJsonRequest(url, null, BaseResponse.class, new IJsonDataListener<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse o) {
                content.setText(o.toString());
            }

            @Override
            public void onFailure() {

            }
        });
    }


    @Override
    public void onConnect(NetType netType) {
        netState.append("连接成功 " + netType.name() + "\n");
    }

    @Override
    public void onDisConnect() {
        netState.append("连接失败\n");
    }
}
