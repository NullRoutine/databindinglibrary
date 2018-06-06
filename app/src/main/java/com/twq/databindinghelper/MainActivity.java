package com.twq.databindinghelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityMainTwoBinding;
import com.twq.databindinghelper.module.AnimatorActivity;
import com.twq.databindinghelper.module.BluetoothActivity;
import com.twq.databindinghelper.module.CollapsingToolbarLayoutTestActivity;
import com.twq.databindinghelper.module.PrintActivity;
import com.twq.databindinghelper.module.TestClockActivity;
import com.twq.databindinghelper.module.TestFragmentActivity;
import com.twq.databindinghelper.module.TestFragmentTwoActivity;
import com.twq.databindinghelper.module.TestTextViewActivity;
import com.twq.databindinghelper.module.TestThreadActivity;
import com.twq.databindinghelper.module.TestWaterBitmapActivity;
import com.twq.databindinghelper.module.WaveActivity;
import com.twq.databindinghelper.module.choosepicture.ChoosePictureActivity;
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
//                launch(mContext, TestCollapsingActivity.class, isFinishing());
            }
        });
        getBinding().btnThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(mContext, TestThreadActivity.class, isFinishing());
            }
        });
        getBinding().btnTestClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(mContext, TestClockActivity.class, isFinishing());
            }
        });
        getBinding().btnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(mContext, TestWaterBitmapActivity.class, isFinishing());
            }
        });
        getBinding().btnWaveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(mContext, WaveActivity.class, isFinishing());
            }
        });
        getBinding().btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(mContext, BluetoothActivity.class, isFinishing());
            }
        });
        getBinding().btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(mContext, ChoosePictureActivity.class, isFinishing());
            }
        });
        getBinding().btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PrintActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//注意
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_two;
    }
}
