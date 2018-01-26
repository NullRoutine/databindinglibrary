package com.twq.databindinghelper.bean;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2018/1/26 0026.
 */

public class ViewWrapper {
    private View view;

    public ViewWrapper(View view) {
        this.view = view;
    }

    //宽度Setter方法
    public void setWidth(int width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        view.setLayoutParams(layoutParams);
        view.invalidate();
    }

    public int getWidth() {
        return view.getLayoutParams().width;
    }
}
