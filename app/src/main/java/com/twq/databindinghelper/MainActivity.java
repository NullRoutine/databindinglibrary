package com.twq.databindinghelper;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.bean.TestBean;
import com.twq.databindinghelper.databinding.ActivityMainBinding;
import com.twq.databindinghelper.module.TestKotlinActivity;

/**
 * 入口
 * Created by Administrator on 2018/1/5 0005.
 */

public class MainActivity extends DataBindingActivity<ActivityMainBinding> {
    private int a;
    private String string = "{\n" +
            "\t\"code\": 200,\n" +
            "\t\"data\": {\n" +
            "\t\t\"entries\": [{\n" +
            "\t\t\t\"CREATETIMESTAMP\": \"2018-02-02 10:20:21.925\",\n" +
            "\t\t\t\"PATH\": \"1eab7db9ec374e3d8a3565ebdaa7b8b9\",\n" +
            "\t\t\t\"DESCRIPTION\": \"文件上传\",\n" +
            "\t\t\t\"LENGTH\": 311584,\n" +
            "\t\t\t\"USERNAME\": \"root:管理员\",\n" +
            "\t\t\t\"ID\": \"1eab7db9ec374e3d8a3565ebdaa7b8b9\",\n" +
            "\t\t\t\"FILETYPE\": \"jpg\",\n" +
            "\t\t\t\"serviceName\": null,\n" +
            "\t\t\t\"FILENAME\": \"7e5aa839d090eb71f2944a23d7882f6c.jpg\"\n" +
            "\t\t}]\n" +
            "\t},\n" +
            "\t\"message\": \"成功！\"\n" +
            "}";

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
                TestBean t = new Gson().fromJson(string, TestBean.class);
                Log.e("TAG", "++" + t.getData().getEntries().toString());
//                AnimatorActivity.Companion.launch(mContext);
                TestKotlinActivity.Companion.launch(mContext);
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
