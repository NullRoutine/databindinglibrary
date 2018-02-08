package com.twq.databindinghelper.module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityTestRefreshBinding;
import com.twq.databindinghelper.view.RefreshLayout;

/**
 * Created by Administrator on 2018/2/8 0008.
 */

public class TestRefreshActivity extends DataBindingActivity<ActivityTestRefreshBinding> {

    @Override
    public void create(Bundle savedInstanceState) {
        getBinding().refresh.setmRefreshListener(new RefreshLayout.RefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getBinding().refresh.RefreshDown();
                    }
                }, 3000);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_refresh;
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, TestRefreshActivity.class);
        context.startActivity(intent);
    }
}
