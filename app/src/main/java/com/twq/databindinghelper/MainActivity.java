package com.twq.databindinghelper;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityMainBinding;
import com.twq.databindinghelper.databinding.ActivityMainTwoBinding;
import com.twq.databindinghelper.module.AnimatorActivity;
import com.twq.databindinghelper.module.CollapsingToolbarLayoutTestActivity;
import com.twq.databindinghelper.module.TestFragmentActivity;
import com.twq.databindinghelper.module.TestFragmentTwoActivity;
import com.twq.databindinghelper.module.TestTextViewActivity;
import com.twq.databindinghelper.module.TestThreadActivity;
import com.twq.databindinghelper.view.dialog.CommonDialog;

/**
 * 入口
 * Created by Administrator on 2018/1/5 0005.
 */

public class MainActivity extends DataBindingActivity<ActivityMainTwoBinding> {
    private int a;

    @Override
    public void create(Bundle savedInstanceState) {
        try {
            a = Integer.parseInt(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("TAG", "++" + a);
        getBinding().img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog commonDialog = CommonDialog.newInstance();
                commonDialog.showDialog(commonDialog, getSupportFragmentManager());
            }
        });
        getBinding().btnAnimator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(mContext, AnimatorActivity.class, isFinishing());
            }
        });
        getBinding().btnFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(mContext, TestFragmentActivity.class, isFinishing());
            }
        });
        getBinding().btnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(mContext, TestTextViewActivity.class, isFinishing());
            }
        });
        getBinding().btnFragmentTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(mContext, TestFragmentTwoActivity.class, isFinishing());
            }
        });
        getBinding().btnToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(mContext, CollapsingToolbarLayoutTestActivity.class, isFinishing());
            }
        });
        getBinding().btnThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(mContext, TestThreadActivity.class, isFinishing());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_two;
    }
}
