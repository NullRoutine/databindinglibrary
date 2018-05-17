package com.twq.databindinghelper.module;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityTestThreadBinding;
import com.twq.databindinghelper.util.CustomThreadPoolExecutor;
import com.twq.databindinghelper.util.PriorityRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池测试
 * Created by tang.wangqiang on 2018/4/28.
 */

public class TestThreadActivity extends DataBindingActivity<ActivityTestThreadBinding> {

    private ScheduledExecutorService scheduledExecutorService;
    private CustomThreadPoolExecutor customThreadPoolExecutor;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_thread;
    }

    @Override
    public void create(Bundle savedInstanceState) {
        customThreadPoolExecutor = new CustomThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());
        getBinding().btnTestNewFixedThreadPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewFixedThreadPool();
            }
        });
        getBinding().btnNewCachedThreadPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewCachedThreadPool();
            }
        });
        getBinding().btnNewScheduledThreadPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] index = {0};
                scheduledExecutorService = Executors.newScheduledThreadPool(2);
                scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        index[0] = index[0] + 1;
                        String threadName = Thread.currentThread().getName();
                        Log.e("TAG", "线程" + threadName + "执行第" + index[0] + "任务");
                        if (index[0] == 6) {
                            if (scheduledExecutorService != null) {
                                scheduledExecutorService.shutdown();
                                scheduledExecutorService = null;
                            }
                        }
                    }
                }, 1, 2, TimeUnit.SECONDS);
            }
        });
        getBinding().threadPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService priorityThreadPool = new ThreadPoolExecutor(3,
                        3, 0L, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());
                for (int i = 1; i <= 10; i++) {
                    final int priority = i;
                    priorityThreadPool.execute(new PriorityRunnable(priority) {
                        @Override
                        public void doSth() {
                            String threadName = Thread.currentThread().getName();
                            Log.e("zxy", "线程：" + threadName + ",正在执行优先级为：" + priority + "的任务");
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
        getBinding().tvStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for (int i = 1; i <= 100; i++) {
                    final int priority = i;
                    customThreadPoolExecutor.execute(new PriorityRunnable(priority) {
                        @Override
                        public void doSth() {
                            runOnUiThread(new Runnable() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    getBinding().tvTest.setText(priority + "");
                                }
                            });
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
        getBinding().tvStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isPause) {
//                    customThreadPoolExecutor.resume();
//                    isPause = false;
//                } else {
//                    customThreadPoolExecutor.pause();
//                    isPause = true;
//                }
            }
        });
    }

    private boolean isPause = false;

    /**
     * 创建固定数量的线程池
     */
    private void createNewFixedThreadPool() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    String threadName = Thread.currentThread().getName();
                    Log.e("TAG", "线程" + threadName + "执行第" + index + "任务");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 创建一个可以根据实际情况调整线程
     * 池中线程的数量的线程池
     */
    private void createNewCachedThreadPool() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final int index = i;
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    String threadName = Thread.currentThread().getName();
                    Log.e("TAG", "线程:" + threadName + "执行第" + index + "任务");
                    try {
                        long time = index * 500;
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
