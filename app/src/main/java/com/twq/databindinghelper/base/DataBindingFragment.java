package com.twq.databindinghelper.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twq.databindinghelper.util.LogUtil;

/**
 * 基本fragment
 * Created by Administrator on 2017/12/29 0029.
 */

public abstract class DataBindingFragment<K extends ViewDataBinding> extends Fragment {

    protected boolean isInitView = false;//是否与View建立起映射关系
    protected Context mContext;
    protected LayoutInflater layoutInflater;
    protected View convertView;
    protected ViewDataBinding mViewDataBinding;

    /**
     * 这里用泛型定义得到的mViewDataBinding强转成具体的实例方便操作
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    protected K getBinding() {
        return (K) mViewDataBinding;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            assert inflater != null;
            mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);//慎用true
            if (mViewDataBinding != null) {
                convertView = mViewDataBinding.getRoot();
            } else {
                convertView = inflater.inflate(getLayoutId(), container, false);
            }
        } catch (NoClassDefFoundError e) {
            convertView = inflater.inflate(getLayoutId(), container, false);
        }
        this.layoutInflater = inflater;
        initView();
        LogUtil.e("Fragment onCreateView===>" + this.getClass().getName());
        return convertView;
    }

    /**
     * 加载页面布局文件
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 让布局中的view与fragment中的变量建立起映射
     */
    protected abstract void initView();

    /**
     * 查询view
     *
     * @param res
     * @param <T>
     * @return
     */
    protected <T extends View> T findView(View view, int res) {
        return view.findViewById(res);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG",getClass().getSimpleName()+"onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mContext != null) {
            mContext = null;
        }
    }

}
