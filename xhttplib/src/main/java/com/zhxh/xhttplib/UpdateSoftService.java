package com.zhxh.xhttplib;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zhxh on 2018/7/2
 */
public class UpdateSoftService extends Service {

    public final static String ACTION_UPDATE_SOFT_UI = "ACTION_UPDATE_SOFT_UI";

    private final int DOWNLOAD_SUCCESS = 1;
    private final int DOWNLOAD_COMPLETE = 2;
    private final int DOWNLOAD_FALL = 3;
    private final int DOWNLOAD_CANCEL = 4;

    private File updateFile;

    private Intent updateIntent;

    public static String downUrl = "";
    public static boolean stopDownload;

    private final IBinder localBinder = new UpdateSoftService.LocalBinder();

    public class LocalBinder extends Binder {
        public UpdateSoftService getService() {

            return UpdateSoftService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {

        return localBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {

            if (XTools.isSDCardMounted() && !TextUtils.isEmpty(downUrl)) {

                updateFile = XTools.isStockInSDK("niuguwang.apk");

                if (updateFile.exists()) {

                    try {

                        updateFile.delete();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                updateIntent = new Intent(this, UpdateSoftDialogActivity.class);

                // 开启线程进行下载
                new Thread(new UpdateSoftService.UpdateThread()).start();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    class UpdateThread implements Runnable {

        Message msg = handler.obtainMessage();

        @Override
        public void run() {

            try {

                long downSize = downloadFile(downUrl, updateFile);

                if (downSize > 0) {
                    // 下载成功！
                    msg.what = DOWNLOAD_SUCCESS;
                    handler.sendMessage(msg);
                }
                if (downSize == -4) {
                    // 下载取消！
                    msg.what = DOWNLOAD_CANCEL;
                    handler.sendMessage(msg);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                // 下载失败
                msg.what = DOWNLOAD_FALL;
                handler.sendMessage(msg);
            }
        }
    }

    public long downloadFile(String downloadUrl, File saveFile)
            throws Exception {

        int downloadCount = 0;
        int currentSize = 0;
        long totalSize = 0;
        int updateTotalSize = 0;
        HttpURLConnection httpConnection = null;
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            URL url = new URL(downloadUrl);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");

            if (currentSize > 0) {
                httpConnection.setRequestProperty("RANGE", "bytes="
                        + currentSize + "-");
            }

            httpConnection.setConnectTimeout(10000);
            httpConnection.setReadTimeout(20000);
            //总大小
            updateTotalSize = httpConnection.getContentLength();

            if (httpConnection.getResponseCode() == 404) {
                throw new Exception("conection net 404！");
            }

            is = httpConnection.getInputStream();
            fos = new FileOutputStream(saveFile);
            byte[] buf = new byte[1024];
            int readSize = -1;

            while ((readSize = is.read(buf)) != -1) {

                if (stopDownload) {
                    return -4;
                }

                fos.write(buf, 0, readSize);
                // 通知更新进度
                totalSize += readSize;
                int tmp = (int) (totalSize * 100 / updateTotalSize);

                // 为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
                if (downloadCount == 0 || tmp - 1 > downloadCount) {
                    downloadCount += 1;
                    Message msg = handler.obtainMessage();
                    msg.what = DOWNLOAD_COMPLETE;
                    msg.arg1 = downloadCount;
                    handler.sendMessage(msg);
                }
            }

        } catch (Exception ex) {

            throw new Exception();

        } finally {

            if (httpConnection != null) {
                httpConnection.disconnect();
            }

            if (is != null) {
                is.close();
            }

            if (fos != null) {
                fos.close();
            }
        }
        return totalSize;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent();

            switch (msg.what) {
                case DOWNLOAD_CANCEL:

                    intent.setAction(ACTION_UPDATE_SOFT_UI);
                    intent.putExtra("progress", -4);
                    sendBroadcast(intent);

                    stopService(updateIntent);// 停止service
                    break;
                case DOWNLOAD_SUCCESS:

                    installApk(UpdateSoftService.this, updateFile);

                    intent.setAction(ACTION_UPDATE_SOFT_UI);
                    intent.putExtra("progress", 100);
                    sendBroadcast(intent);

                    stopService(updateIntent);// 停止service
                    break;
                case DOWNLOAD_COMPLETE:// 下载中状态

                    intent.setAction(ACTION_UPDATE_SOFT_UI);
                    intent.putExtra("progress", msg.arg1);
                    sendBroadcast(intent);

                    break;
                case DOWNLOAD_FALL:// 失败状态
                    Log.d("UpdateSoftService-", "DOWNLOAD_FALL " + msg.arg1);
                    // 停止service
                    stopService(updateIntent);

                    break;
                default:

                    stopService(updateIntent);
            }
        }

    };

    public static void installApk(Context context, File file) {
        if (stopDownload) {
            return;
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
