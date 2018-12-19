package com.twq.databindinghelper.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.twq.databindinghelper.view.SwipeBackLayout;

import java.util.ArrayList;

/**
 * 使用dataBinding项目（带6.0权限检测简单封装）
 * Created by Administrator on 2017/12/28 0028.
 */

public abstract class DataBindingActivity<K extends ViewDataBinding> extends AppCompatActivity {

    protected ViewDataBinding mViewDataBinding;//dataBinding加载布局
    protected View mMainView;//布局获取的View
    protected Context mContext;//

    /**
     * 这里用泛型定义得到的mViewDataBinding强转成具体的实例方便操作
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    protected K getBinding() {
        return (K) mViewDataBinding;
    }

    /**
     * 权限列表
     */
    protected ArrayList<String> mPermissionList;
    protected static final int PERMISSION = 0x01;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        try {
            mViewDataBinding = DataBindingUtil.setContentView((Activity) mContext, getLayoutId());
            if (mViewDataBinding != null) {
                mMainView = mViewDataBinding.getRoot();
            } else {
                mMainView = LayoutInflater.from(this).inflate(getLayoutId(), null);
                setContentView(mMainView);
            }
        } catch (NoClassDefFoundError e) {
            mMainView = LayoutInflater.from(this).inflate(getLayoutId(), null);
            setContentView(mMainView);
        }
        create(savedInstanceState);
        if (getPermission() != null) {
            requestPermission();
        }
        SwipeBackLayout swipeBackLayout = new SwipeBackLayout(this);
        swipeBackLayout.bindActivity();
    }

    /**
     * 跳转不带参数
     *
     * @param context
     * @param c
     * @param isFinish
     */
    protected void launch(Context context, Class<? extends DataBindingActivity> c, boolean isFinish) {
        if (!isFinish) {
            Intent intent = new Intent(context, c);
            context.startActivity(intent);
        }
    }

    /**
     * 初始化工作
     *
     * @param savedInstanceState
     */
    public abstract void create(Bundle savedInstanceState);

    /**
     * 获取布局ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 查询view
     *
     * @param res
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T findView(int res) {
        return (T) findViewById(res);
    }

    /**
     * 查询view
     *
     * @param res
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T findView(View view, int res) {
        return (T) view.findViewById(res);
    }

    protected void launchOne(Context context, Class<? extends DataBindingActivity> c, boolean isFinish) {
        if (!isFinish) {
            Intent intent = new Intent(context, c);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mContext != null) {
            mContext = null;
        }
    }

    /**
     * 6.0权限列表
     *
     * @return
     */
    protected String[] getPermission() {
        return null;
    }

    /**
     * 权限请求
     */
    @TargetApi(Build.VERSION_CODES.M)
    protected void requestPermission() {
        if (getAndroidVersion(Build.VERSION_CODES.M)) {
            String[] permissions = getPermission();
            if (mPermissionList == null) {
                mPermissionList = new ArrayList<>();
            }
            mPermissionList.clear();
            if (permissions != null && permissions.length != 0) {
                for (String permission : permissions) {
                    if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                        mPermissionList.add(permission);
                    }
                }
                if (mPermissionList != null && mPermissionList.size() != 0) {
                    requestPermissions(mPermissionList.toArray(new String[mPermissionList.size()]), PERMISSION);
                }
            }
        }
    }

    /**
     * 参数1：requestCode-->是requestPermissions()方法传递过来的请求码。
     * 参数2：permissions-->是requestPermissions()方法传递过来的需要申请权限
     * 参数3：grantResults-->是申请权限后，系统返回的结果，PackageManager.PERMISSION_GRANTED表示授权成功，PackageManager.PERMISSION_DENIED表示授权失败。
     * grantResults和permissions是一一对应的
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isNoPremission = true;
        if (requestCode == PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    isNoPremission = false;
                    break;
                }
            }
            if (!isNoPremission) {
                NoPermission();
            }
        }
    }

    /**
     * 权限请求失败回调
     */
    protected void NoPermission() {

    }

    /**
     * 判断是否有权限
     *
     * @return
     */
    public boolean hasPermission(String... permission) {
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                for (String permissions : permission) {
                    result = checkSelfPermission(permissions) == PackageManager.PERMISSION_GRANTED;
                    if (!result) {
                        break;
                    }
                }

            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                for (String permissions : permission) {
                    result = PermissionChecker.checkSelfPermission(mContext, permissions) == PackageManager.PERMISSION_GRANTED;
                    if (!result) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    /*判断当前版本是不是大于version*/
    public Boolean getAndroidVersion(int version) {
        if (Build.VERSION.SDK_INT >= version) {
            return true;

        } else {
            return false;
        }
    }
}
