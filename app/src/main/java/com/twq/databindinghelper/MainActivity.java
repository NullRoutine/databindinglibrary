package com.twq.databindinghelper;

import android.os.Bundle;

import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityMainBinding;

public class MainActivity extends DataBindingActivity<ActivityMainBinding> {


    @Override
    public void create(Bundle savedInstanceState) {
        getBinding().tv1.setText("我是MVVM");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

}
