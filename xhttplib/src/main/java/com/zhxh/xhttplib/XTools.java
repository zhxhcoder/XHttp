package com.zhxh.xhttplib;

import android.os.Environment;

import java.io.File;

/**
 * Created by zhxh on 2018/7/2
 */
public class XTools {

    /**
     * 判断SD卡是否可用
     *
     * @return 是否可用
     */
    public static boolean isSDCardMounted() {
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    public static File isStockInSDK(String apkName) {
        String dir = Environment.getExternalStorageDirectory().toString();
        File file = new File(dir, apkName);
        return file;
    }
}
