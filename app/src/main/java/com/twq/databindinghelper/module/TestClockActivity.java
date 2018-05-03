package com.twq.databindinghelper.module;

import android.os.Bundle;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityTestClockBinding;

/**
 * Created by tang.wangqiang on 2018/5/3.
 */

public class TestClockActivity extends DataBindingActivity<ActivityTestClockBinding> {
    @Override
    public void create(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_clock;
    }
}
