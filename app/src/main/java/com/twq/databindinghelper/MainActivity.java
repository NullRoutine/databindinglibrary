package com.twq.databindinghelper;

import android.os.Bundle;

import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityMainBinding;

/**
 * 入口
 * Created by Administrator on 2018/1/5 0005.
 */

public class MainActivity extends DataBindingActivity<ActivityMainBinding> {
    @Override
    public void create(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
