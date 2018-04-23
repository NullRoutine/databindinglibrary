package com.twq.databindinghelper.module.fragment;

import android.os.Bundle;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.base.DataBindingFragment;
import com.twq.databindinghelper.databinding.FragmentTestBinding;

/**
 * Created by tang.wangqiang on 2018/4/23.
 */

public class TestFragment extends DataBindingFragment<FragmentTestBinding> {

    public static TestFragment newInstance(String msg) {
        TestFragment fragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", msg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }


    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        assert bundle != null;
        String data = bundle.getString("title");
        if (data != null) {
            getBinding().tvTitle.setText(data);
        }
    }
}
