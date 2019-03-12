package com.zhxh.xhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.zhxh.xhttp.bean.BaseResponse;
import com.zhxh.xhttplib.IJsonDataListener;
import com.zhxh.xhttplib.Xhttp;
import com.zhxh.xnetlib.NetworkManager;
import com.zhxh.xnetlib.annotation.Network;
import com.zhxh.xnetlib.type.NetType;
import com.zhxh.xnetlib.utlis.Constants;

public class MainActivity extends AppCompatActivity {

    private TextView content;
    private TextView netState;

    private String url = "https://v.juhe.cn/historyWeather/citys?province_id=1";
    private String errorUrl = "https://xxx?province_id=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkManager.getDefault().registerObserver(this);


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


    @Network(netType = NetType.AUTO)
    public void network(NetType netType) {
        switch (netType) {
            case WIFI:
                Log.e(Constants.TAG, "MainActivity网络类型WiFi");
                break;
            case CMNET:
            case CMWAP:
                Log.e(Constants.TAG, "MainActivity网络连接成功，网络类型"+netType.name());
                break;
            case NONE:
                Log.e(Constants.TAG, "MainActivity网络无连接");
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册
        NetworkManager.getDefault().unRegisterObserver(this);
    }
}
