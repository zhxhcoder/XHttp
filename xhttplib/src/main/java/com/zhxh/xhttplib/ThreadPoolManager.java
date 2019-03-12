package com.zhxh.xhttplib;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhxh on 2018/7/2
 */
public class ThreadPoolManager {
    private static volatile ThreadPoolManager instance;

    public static ThreadPoolManager getInstance() {

        if (instance == null) {
            synchronized (ThreadPoolManager.class) {
                if (instance == null) {
                    instance = new ThreadPoolManager();
                }
            }
        }
        return instance;
    }

    //FIFO 新元素插入尾部
    private LinkedBlockingDeque<Runnable> linkedBlockingDeque = new LinkedBlockingDeque<>();

    //新任务入列
    public void addTask(Runnable runnable) {

        if (runnable != null) {
            try {
                linkedBlockingDeque.add(runnable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //线程池
    private ThreadPoolExecutor poolExecutor;

    private ThreadPoolManager() {
        //初始化时
        poolExecutor = new ThreadPoolExecutor(3, 10, 15, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {

                addTask(runnable);
            }
        });

        poolExecutor.execute(daemonTask);
        poolExecutor.execute(delayTask);
    }

    //创建延迟队列

    private DelayQueue<HttpTask> mDelayQueue = new DelayQueue<>();


    public void addDelayTask(HttpTask httpTask) {
        if (httpTask != null) {
            httpTask.setDelayTime(5000);
            mDelayQueue.offer(httpTask);
        }
    }


    //创建守护进程 将队列与线程池关联,获取队列中的任务

    public Runnable daemonTask = new Runnable() {
        Runnable item;

        @Override
        public void run() {
            while (true) {
                if (linkedBlockingDeque != null && !linkedBlockingDeque.isEmpty()) {
                    try {
                        item = linkedBlockingDeque.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    poolExecutor.execute(item);
                }
            }
        }
    };

    //创建重试进程，不停获取延迟队列


    public Runnable delayTask = new Runnable() {
        @Override
        public void run() {
            HttpTask httpTask = null;
            while (true) {
                if (mDelayQueue != null && !mDelayQueue.isEmpty()) {
                    try {

                        httpTask = mDelayQueue.take();
                        if (httpTask.getRetryCount() < 3) {
                            poolExecutor.execute(httpTask);
                            httpTask.setRetryCount(httpTask.getRetryCount() + 1);

                            Log.e("xhttp-", httpTask.getRetryCount() + "次" + System.currentTimeMillis());
                        } else {
                            Log.e("xhttp-", "重试仍失败，放弃重试");
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //poolExecutor.execute(httpTask);
                }
            }
        }
    };
}
