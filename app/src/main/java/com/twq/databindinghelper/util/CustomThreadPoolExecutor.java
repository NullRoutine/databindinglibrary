package com.twq.databindinghelper.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自定义线程池
 * Created by tang.wangqiang on 2018/4/28.
 */

public class CustomThreadPoolExecutor extends ThreadPoolExecutor {

    private boolean isPaused;
    private ReentrantLock pauseLock = new ReentrantLock();
    private Condition unpaused = pauseLock.newCondition();

    public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        pauseLock.lock();
        while (isPaused) {
            try {
                unpaused.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                pauseLock.unlock();
            }
        }
        LogUtil.e("线程" + t.getName() + "开始");
    }

    /**
     * 线程暂停
     */
    public void pause() {
        pauseLock.lock();
        try {
            isPaused = true;
        } finally {
            pauseLock.unlock();
        }
    }

    /**
     * 线程开始
     */
    public void resume() {
        pauseLock.lock();
        try {
            isPaused = false;
            unpaused.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        LogUtil.e("线程" + Thread.currentThread().getName() + "结束");
    }

    @Override
    protected void terminated() {
        super.terminated();
        LogUtil.e("线程结束");
    }
}
