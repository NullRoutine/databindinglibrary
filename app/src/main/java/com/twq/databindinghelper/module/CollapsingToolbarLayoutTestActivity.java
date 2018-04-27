package com.twq.databindinghelper.module;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityCollapsingLayoutBinding;

/**
 * Created by tang.wangqiang on 2018/4/27.
 */

public class CollapsingToolbarLayoutTestActivity extends DataBindingActivity<ActivityCollapsingLayoutBinding> {
    private float mSelfHeight = 0;//用以判断是否得到正确的宽高值
    private float mTitleScale;
    private float mSubScribeScale;
    private float mSubScribeScaleX;
    private float mHeadImgScale;

    @Override
    public void create(Bundle savedInstanceState) {
        final float screenW = getResources().getDisplayMetrics().widthPixels;
        final float toolbarHeight = getResources().getDimension(R.dimen.toolbar_height);
        final float initHeight = getResources().getDimension(R.dimen.subscription_head);

        getBinding().viewAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (mSelfHeight == 0) {
                    mSelfHeight = getBinding().subscriptionTitle.getHeight();
                    float distanceTitle = getBinding().subscriptionTitle.getTop() + (mSelfHeight - toolbarHeight) / 2.0f;
                    float distanceSubscribe = getBinding().subscribe.getY() + (getBinding().subscribe.getHeight() - toolbarHeight) / 2.0f;
                    float distanceHeadImg = getBinding().ivHead.getY() + (getBinding().ivHead.getHeight() - toolbarHeight) / 2.0f;
                    float distanceSubscribeX = screenW / 2.0f - (getBinding().subscribe.getWidth() / 2.0f + getResources().getDimension(R.dimen.toolbar_height));
                    mTitleScale = distanceTitle / (initHeight - toolbarHeight);
                    mSubScribeScale = distanceSubscribe / (initHeight - toolbarHeight);
                    mHeadImgScale = distanceHeadImg / (initHeight - toolbarHeight);
                    mSubScribeScaleX = distanceSubscribeX / (initHeight - toolbarHeight);
                }
                float scale = 1.0f - (-verticalOffset) / (initHeight - toolbarHeight);
                getBinding().ivHead.setScaleX(scale);
                getBinding().ivHead.setScaleY(scale);
                getBinding().ivHead.setTranslationY(mHeadImgScale * verticalOffset);
                getBinding().subscriptionTitle.setTranslationY(mTitleScale * verticalOffset);
                getBinding().subscribe.setTranslationY(mSubScribeScale * verticalOffset);
                getBinding().subscribe.setTranslationX(-mSubScribeScaleX * verticalOffset);

            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collapsing_layout;
    }
}
