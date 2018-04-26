package com.twq.databindinghelper.module;

import android.os.Bundle;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityTestTextviewBinding;

/**
 * Created by tang.wangqiang on 2018/4/26.
 */

public class TestTextViewActivity extends DataBindingActivity<ActivityTestTextviewBinding> {
    @Override
    public void create(Bundle savedInstanceState) {
        getBinding().tvTest.setText("你好");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_textview;
    }
}
