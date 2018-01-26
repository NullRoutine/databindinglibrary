package com.twq.databindinghelper;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityMainBinding;
import com.twq.databindinghelper.module.AnimatorActivity;

/**
 * 入口
 * Created by Administrator on 2018/1/5 0005.
 */

public class MainActivity extends DataBindingActivity<ActivityMainBinding> {
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
                AnimatorActivity.launch(mContext);
//                CommonDialog commonDialog = CommonDialog.newInstance();
//                commonDialog.showDialog(commonDialog, getSupportFragmentManager());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
