package com.twq.databindinghelper.module;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityTestFragmentBinding;
import com.twq.databindinghelper.module.fragment.TestFragment;

/**
 * 测试fragment
 * Created by tang.wangqiang on 2018/4/23.
 */

public class TestFragmentActivity extends DataBindingActivity<ActivityTestFragmentBinding> implements RadioGroup.OnCheckedChangeListener {
    private FragmentManager mFragmentManager;

    private TestFragment tabOneFragment = null;
    private TestFragment tabTwoFragment = null;
    private TestFragment tabThreeFragment = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_fragment;
    }

    @Override
    public void create(Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();
        getBinding().idContent.setVisibility(View.VISIBLE);
        getBinding().radioBottom.setOnCheckedChangeListener(this);
        setTabSelection(0);
    }


    /**
     * 将所有的Fragment都置为隐藏状态。
     * 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (tabOneFragment != null) {
            transaction.hide(tabOneFragment);
        }
        if (tabTwoFragment != null) {
            transaction.hide(tabTwoFragment);
        }
        if (tabThreeFragment != null) {
            transaction.hide(tabThreeFragment);
        }
    }

    private void setTabSelection(int index) {
        // 开启Fragment事务
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
//        hideFragments(transaction);
        switch (index) {
            case 0:
                if (tabOneFragment == null) {
                    tabOneFragment = TestFragment.newInstance("我是One");
//                    transaction.add(R.id.id_content, tabOneFragment);

                }
//                else {
//                    transaction.show(tabOneFragment);
//                    tabOneFragment.reset();
//                }
                transaction.replace(R.id.id_content, tabOneFragment);
//                tabOneFragment.reset();
                break;
            case 1:
                if (tabTwoFragment == null) {
                    tabTwoFragment = TestFragment.newInstance("我是Two");
//                    transaction.add(R.id.id_content, tabTwoFragment);
                }
//                else {
//                    transaction.show(tabTwoFragment);
//                }
                transaction.replace(R.id.id_content, tabTwoFragment);
                break;
            case 2:
                if (tabThreeFragment == null) {
                    tabThreeFragment = TestFragment.newInstance("我是Three");
//                    transaction.add(R.id.id_content, tabThreeFragment);
                }
//                else {
//                    transaction.show(tabThreeFragment);
//                }
                transaction.replace(R.id.id_content, tabThreeFragment);
                break;
        }
        transaction.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_home:
                setTabSelection(0);
                break;
            case R.id.radio_center:
                setTabSelection(1);
                break;
            case R.id.radio_manager:
                setTabSelection(2);
                break;
        }
    }
}
