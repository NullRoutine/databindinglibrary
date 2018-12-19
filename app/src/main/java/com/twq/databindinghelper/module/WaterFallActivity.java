package com.twq.databindinghelper.module;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityWaterFallBinding;
import com.twq.databindinghelper.util.DeviceUtil;
import com.twq.databindinghelper.util.LoadImgUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 瀑布流
 * Created by tang.wangqiang on 2018/8/27.
 */

public class WaterFallActivity extends DataBindingActivity<ActivityWaterFallBinding> {
    WaterFallAdapter apter;

    @Override
    public void create(Bundle savedInstanceState) {
        getBinding().recycleView.setHasFixedSize(true);
        getBinding().recycleView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        apter = new WaterFallAdapter(new ArrayList<String>(), mContext);
        getBinding().recycleView.setAdapter(apter);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("https://ws1.sinaimg.cn/large/0065oQSqly1fuh5fsvlqcj30sg10onjk.jpg");
        }
        apter.setImgUrlList(list);
        getBinding().recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    apter.setFlying(false);
//                } else {
//                    apter.setFlying(true);
//                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_water_fall;
    }

    public static class WaterFallAdapter extends RecyclerView.Adapter<WaterFallAdapter.OneViewHolder> {
        private List<Integer> integerList;//产生随机高度
        private List<String> imgUrlList;//数据源
        private Context context;
        private boolean flying;

        public void setFlying(boolean flying) {
            this.flying = flying;
            notifyDataSetChanged();
        }

        public void setImgUrlList(List<String> imgUrlList) {
            this.imgUrlList = imgUrlList;
            notifyDataSetChanged();
        }

        public WaterFallAdapter(List<String> imgUrlList, Context context) {
            this.imgUrlList = imgUrlList;
            //记录为每个控件产生的随机高度,避免滑回到顶部出现空白
            integerList = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                int height = new Random().nextInt(50) + 200;//[100,300)的随机数
                height = DeviceUtil.dip2px(context, height);
                integerList.add(height);
            }
            this.context = context;
        }

        @Override
        public OneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OneViewHolder(LayoutInflater.from(context).inflate(R.layout.item_water_fall, parent, false));
        }

        @Override
        public void onBindViewHolder(OneViewHolder holder, int position) {
            //由于需要实现瀑布流的效果,所以就需要动态的改变控件的高度了
            ViewGroup.LayoutParams params = holder.imgPicture.getLayoutParams();
            params.height = integerList.get(position);
            holder.imgPicture.setLayoutParams(params);
            if (!flying) {
                LoadImgUtil.loadImage(context, imgUrlList.get(position), holder.imgPicture);
            }
        }

        @Override
        public void onViewRecycled(OneViewHolder holder) {
            super.onViewRecycled(holder);
            ImageView imageView = holder.imgPicture;
            if (imageView != null) {

            }
        }

        @Override
        public int getItemCount() {
            return imgUrlList.size() > 0 ? imgUrlList.size() : 0;
        }

        static class OneViewHolder extends RecyclerView.ViewHolder {
            private ImageView imgPicture;

            OneViewHolder(View itemView) {
                super(itemView);
                imgPicture = itemView.findViewById(R.id.img_picture);
            }
        }
    }
}
