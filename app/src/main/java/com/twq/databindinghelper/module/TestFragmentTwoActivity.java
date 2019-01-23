package com.twq.databindinghelper.module;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityTestFragmentTwoBinding;
import com.twq.databindinghelper.module.fragment.TestFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试嵌套Viewpager的fragment
 * Created by tang.wangqiang on 2018/4/23.
 */

public class TestFragmentTwoActivity extends DataBindingActivity<ActivityTestFragmentTwoBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_fragment_two;
    }

    @Override
    public void create(Bundle savedInstanceState) {
        List<Fragment> list = new ArrayList<>();
        list.add(TestFragment.newInstance("我是One"));
        list.add(TestFragment.newInstance("我是Two"));
        list.add(TestFragment.newInstance("我是Three"));
        list.add(TestFragment.newInstance("我是four"));
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), list);
        getBinding().viewPager.setAdapter(adapter);
        getBinding().viewPager.setOffscreenPageLimit(3);
        getBinding().viewPager.setCurrentItem(0);
        getBinding().viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                reSetStatus();
                switch (position) {
                    case 0:
                        getBinding().radioHome.setChecked(true);
                        break;
                    case 1:
                        getBinding().radioCenter.setChecked(true);
                        break;
                    case 2:
                        getBinding().radioManager.setChecked(true);
                        break;
                    case 3:
                        getBinding().radioManager2.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getBinding().radioBottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_home:
                        getBinding().viewPager.setCurrentItem(0, true);
                        break;
                    case R.id.radio_center:
                        getBinding().viewPager.setCurrentItem(1, true);
                        break;
                    case R.id.radio_manager:
                        getBinding().viewPager.setCurrentItem(2, true);
                        break;
                    case R.id.radio_manager2:
                        getBinding().viewPager.setCurrentItem(3, true);
                        break;
                }
            }
        });
    }

    private void reSetStatus() {
        getBinding().radioHome.setChecked(false);
        getBinding().radioCenter.setChecked(false);
        getBinding().radioManager.setChecked(false);
        getBinding().radioManager2.setChecked(false);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public ViewPagerAdapter(FragmentManager fm, List<android.support.v4.app.Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }


        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.size();
        }
    }

}
