package com.twq.databindinghelper.util;

import android.support.annotation.NonNull;

/**
 * Created by tang.wangqiang on 2018/4/28.
 */

public abstract class PriorityRunnable implements Runnable, Comparable<PriorityRunnable> {

    private int priority;

    public PriorityRunnable(int priority) {
        if (priority < 0)
            throw new IllegalArgumentException();
        this.priority = priority;
    }

    @Override
    public int compareTo(@NonNull PriorityRunnable o) {
        int my = this.getPriority();
        int other = o.getPriority();
        return my < other ? 1 : my > other ? -1 : 0;//注意这里是降序
    }

    @Override
    public void run() {
        doSth();
    }

    public abstract void doSth();

    public int getPriority() {
        return priority;
    }
}
