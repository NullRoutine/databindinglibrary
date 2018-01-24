package com.twq.databindinghelper.view.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by Administrator on 2018/1/24 0024.
 */

public abstract class BaseDialogFragment extends DialogFragment {

    protected View convertView;//根布局

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {//设置宽度
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog == null) {
            return null;
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        try {
            convertView = inflater.inflate(getLayoutId(), container, false);
        } catch (NoClassDefFoundError e) {
            convertView = inflater.inflate(getLayoutId(), container, false);
        }
        dialog.setCancelable(Cancelable());
        dialog.setCanceledOnTouchOutside(Cancelable());
        initView();
        return convertView;
    }

    protected abstract int getLayoutId();//加载布局

    protected abstract void initView();//初始化

    protected boolean Cancelable() {
        return false;//是否点击外部消失
    }

    public void showDialog(BaseDialogFragment baseDialogFragment, FragmentManager fragmentManager) {
        //这里直接调用show方法会报java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
        //因为show方法中是通过commit进行的提交(通过查看源码)
        //这里为了修复这个问题，使用commitAllowingStateLoss()方法
        //注意：DialogFragment是继承自android.app.Fragment，这里要注意同v4包中的Fragment区分，别调用串了
        //DialogFragment有自己的好处，可能也会带来别的问题
        //dialog.show(getFragmentManager(), "SignDialog");
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(baseDialogFragment, BaseDialogFragment.class.getSimpleName());
        ft.commitAllowingStateLoss();
    }

}
