# XHttp
自己封装的网络请求库，支持并发，缓存，重试等机制；

也可通过注解的方式监听网络


注册：

`NetworkManager.getDefault().registerObserver(this);`

使用：

```java
Xhttp.sendJsonRequest(url, null, BaseResponse.class, new IJsonDataListener<BaseResponse>() {
    @Override
    public void onSuccess(BaseResponse o) {
        content.setText(o.toString());
    }

    @Override
    public void onFailure() {

    }
});
```

解除注册：

`NetworkManager.getDefault().unRegisterObserver(this);`

网络监听：

```java
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
```