package com.twq.databindinghelper.view.dialog;


import com.twq.databindinghelper.R;

/**
 * 测试
 * Created by Administrator on 2018/1/24 0024.
 */

public class CommonDialog extends BaseDialogFragment {

    public static CommonDialog newInstance() {
        CommonDialog dialog = new CommonDialog();
        return dialog;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_dailog_test;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected boolean Cancelable() {
        return true;
    }
}
