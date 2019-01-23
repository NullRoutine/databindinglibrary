package com.twq.databindinghelper.module.fragment;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.base.DataBindingFragment;
import com.twq.databindinghelper.base.LazyFragment;
import com.twq.databindinghelper.databinding.FragmentTestBinding;
import com.twq.databindinghelper.util.LogUtil;

/**
 * Created by tang.wangqiang on 2018/4/23.
 */

public class TestFragment extends LazyFragment {
    private CheckBox checkBoxOne, checkBoxOneTwo, checkBoxThree;
    private TextView textView;

    public static TestFragment newInstance(String msg) {
        TestFragment fragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", msg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }


    @Override
    protected void initView() {
        checkBoxOne = convertView.findViewById(R.id.checkbox_one);
        checkBoxOneTwo = convertView.findViewById(R.id.checkbox_two);
        checkBoxThree = convertView.findViewById(R.id.checkbox_three);
        textView = convertView.findViewById(R.id.tv_title);
        Bundle bundle = getArguments();
        assert bundle != null;
        String data = bundle.getString("title");
        if (data != null && data.equals("我是One")) {
            LogUtil.e("====>" + checkBoxOne.isChecked());
            LogUtil.e("====>" + checkBoxOneTwo.isChecked());
            LogUtil.e("====>" + checkBoxThree.isChecked());
//            checkBoxOne.setChecked(true);
            checkBoxOne.post(new Runnable() {
                @Override
                public void run() {
                    checkBoxOne.setChecked(true);
                }
            });
            checkBoxOneTwo.post(new Runnable() {
                @Override
                public void run() {
                    checkBoxOneTwo.setChecked(false);
                }
            });
            checkBoxThree.post(new Runnable() {
                @Override
                public void run() {
                    checkBoxThree.setChecked(false);
                }
            });
//            checkBoxOneTwo.setChecked(false);
//            checkBoxThree.setChecked(false);
            LogUtil.e("====>" + checkBoxOne.isChecked());
            LogUtil.e("====>" + checkBoxOneTwo.isChecked());
            LogUtil.e("====>" + checkBoxThree.isChecked());
        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void reset() {
        if (checkBoxOne != null) {
            checkBoxOne.setChecked(true);
            checkBoxOneTwo.setChecked(false);
            checkBoxThree.setChecked(false);
        }
    }

    @Override
    protected void lazyLoad() {
        if (isInitView && isVisible) {
            Bundle bundle = getArguments();
            assert bundle != null;
            String data = bundle.getString("title");
            textView.setText(data);
            LogUtil.e("====>" + data);
        }
    }

    @Override
    protected void onInvisible() {

    }
}
