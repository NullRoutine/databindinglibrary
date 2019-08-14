package com.twq.databindinghelper.module;

import android.os.Bundle;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivitySignBinding;

/**
 * Created by tang.wangqiang
 * on 2019/8/13
 */
public class SignActivity extends DataBindingActivity<ActivitySignBinding> {
    @Override
    public void create(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign;
    }
}
