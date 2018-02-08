package com.twq.databindinghelper;

import android.os.Bundle;
import android.view.View;

import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityMainBinding;
import com.twq.databindinghelper.module.AnimatorActivity;
import com.twq.databindinghelper.module.TestKotlinActivity;
import com.twq.databindinghelper.module.TestRefreshActivity;
import com.twq.databindinghelper.view.dialog.CommonDialog;

/**
 * 入口
 * Created by Administrator on 2018/1/5 0005.
 */

public class MainActivity extends DataBindingActivity<ActivityMainBinding> {

    @Override
    public void create(Bundle savedInstanceState) {
        getBinding().img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestKotlinActivity.Companion.launch(mContext);

            }
        });
        getBinding().buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorActivity.Companion.launch(mContext);
            }
        });
        getBinding().buttonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog commonDialog = CommonDialog.newInstance();
                commonDialog.showDialog(commonDialog, getSupportFragmentManager());
            }
        });
        getBinding().buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestRefreshActivity.launch(mContext);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
