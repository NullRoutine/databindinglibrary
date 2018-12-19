package com.twq.databindinghelper.module;

import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.view.View;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityTestTextviewBinding;
import com.twq.databindinghelper.service.MyVpnService;

/**
 * Created by tang.wangqiang on 2018/4/26.
 */

public class TestTextViewActivity extends DataBindingActivity<ActivityTestTextviewBinding> {
    @Override
    public void create(Bundle savedInstanceState) {
        getBinding().tvTest.setText("你好");
        getBinding().btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = VpnService.prepare(getApplicationContext());
                if (intent != null) {
                    startActivityForResult(intent, 0);
                } else {
                    onActivityResult(0, RESULT_OK, null);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent intent = new Intent(this, MyVpnService.class);
            startService(intent);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_textview;
    }
}
