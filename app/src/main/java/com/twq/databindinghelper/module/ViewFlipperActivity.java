package com.twq.databindinghelper.module;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ViewFlipper;

import com.twq.databindinghelper.R;

/**
 * Created by tang.wangqiang on 2018/12/19.
 */

public class ViewFlipperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_flipper);
        ViewFlipper viewFlipper = findViewById(R.id.view_flipper);
        viewFlipper.addView(View.inflate(this, R.layout.view_flipper, null));
        viewFlipper.addView(View.inflate(this, R.layout.view_flipper, null));
        viewFlipper.addView(View.inflate(this, R.layout.view_flipper, null));
    }
}
