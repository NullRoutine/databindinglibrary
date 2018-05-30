package com.twq.databindinghelper.module.choosepicture;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.print.PrintHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityChoosePictureBinding;
import com.twq.databindinghelper.util.LoadImgUtil;

/**
 * 图片选择
 * Created by tang.wangqiang on 2018/5/30.
 */

public class ChoosePictureActivity extends DataBindingActivity<ActivityChoosePictureBinding> {

    private BottomSheetDialog mBottomSheetDialog;
    private PictureUtil pictureUtil;

    @Override
    public void create(Bundle savedInstanceState) {
        getBinding().btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureUtil = new PictureUtil((Activity) mContext);
                if (mBottomSheetDialog == null) {
                    mBottomSheetDialog = new BottomSheetDialog(mContext);
                    View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_choose_picture, null, false);
                    TextView tv_camera = dialogView.findViewById(R.id.tv_camera);
                    TextView tv_photo = dialogView.findViewById(R.id.tv_photo);
                    TextView tv_cancel = dialogView.findViewById(R.id.tv_cancel);
                    tv_camera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pictureUtil.ChooseCamera();
                            mBottomSheetDialog.dismiss();
                        }
                    });
                    tv_photo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pictureUtil.ChooseImage();
                            mBottomSheetDialog.dismiss();
                        }
                    });
                    tv_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBottomSheetDialog.dismiss();
                        }
                    });
                    mBottomSheetDialog.setContentView(dialogView);
                    mBottomSheetDialog.show();
                } else {
                    mBottomSheetDialog.show();
                }
            }
        });
        getBinding().btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPhotoPrint();
            }
        });
    }

    private void doPhotoPrint() {
        PrintHelper photoPrinter = new PrintHelper(mContext);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_app_bg);
        photoPrinter.printBitmap("droids.jpg - test print", bitmap);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_picture;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pictureUtil.onActivityResult(requestCode, resultCode, data, new PictureUtil.ChooseListener() {
            @Override
            public void choose(Uri uri, String path) {
                LoadImgUtil.loadCircleImage(mContext, uri, getBinding().imgPhoto);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pictureUtil != null)
            pictureUtil.delitePicture();
    }
}
