package com.twq.databindinghelper.module;

import android.app.Activity;
import android.os.Bundle;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityWaveViewBinding;

/**
 * Created by tang.wangqiang on 2018/5/10.
 */

public class WaveActivity extends DataBindingActivity<ActivityWaveViewBinding> {
    @Override
    public void create(Bundle savedInstanceState) {
        getBinding().waveView.move();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wave_view;
    }
}
