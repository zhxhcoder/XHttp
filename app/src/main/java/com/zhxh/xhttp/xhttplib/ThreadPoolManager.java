package com.zhxh.xhttp.xhttplib;

import java.util.concurrent.ArrayBlockingQueue;
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

}
