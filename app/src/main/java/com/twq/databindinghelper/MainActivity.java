package com.twq.databindinghelper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityMainBinding;
import com.twq.databindinghelper.module.AnimatorActivity;
import com.twq.databindinghelper.module.TestKotlinActivity;
import com.twq.databindinghelper.module.TestRefreshActivity;
import com.twq.databindinghelper.view.dialog.CommonDialog;

import cn.passguard.PassGuardEdit;
import cn.passguard.doAction;

/**
 * 入口
 * Created by Administrator on 2018/1/5 0005.
 */

public class MainActivity extends DataBindingActivity<ActivityMainBinding> {
    static {
        System.loadLibrary("PassGuard");
    }

    @Override
    public void create(Bundle savedInstanceState) {
        initPassGuard();
        getBinding().img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestKotlinActivity.Companion.launch(mContext);

            }
        });
        getBinding().buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorActivity.Companion.launch(mContext);
            }
        });
        getBinding().buttonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog commonDialog = CommonDialog.newInstance();
                commonDialog.showDialog(commonDialog, getSupportFragmentManager());
            }
        });
        getBinding().buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TestRefreshActivity.launch(mContext);
                launchOne(mContext, TestRefreshActivity.class, isFinishing());
            }
        });
        doAction action = new doAction() {
            public void doActionFunction() {
//                Toast.makeText(
//                        getApplicationContext(),
//                        getBinding().edt.isKeyBoardShowing() ? "KeyBoardShow"
//                                : "KeyBoardHide", Toast.LENGTH_LONG).show();
            }
        };
        getBinding().edt.setKeyBoardHideAction(action);
        getBinding().edt.setKeyBoardShowAction(action);
        getBinding().edt.setWatchOutside(true);
        getBinding().edt.initPassGuardKeyBoard();
        getBinding().edt.setInputType(InputType.TYPE_NULL);
        getBinding().button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNormalDialog("MD5", getBinding().edt.getMD5());
                PassGuardEdit passGuardEdit = getBinding().edt;
                passGuardEdit.setCipherKey("abcdefghijklmnopqrstuvwxyz123456");
                passGuardEdit.setPublicKey("3081890281810092d9d8d04fb5f8ef9b8374f21690fd46fdbf49b40eeccdf416b4e2ac2044b0cfe3bd67eb4416b26fd18c9d3833770a526fd1ab66a83ed969af74238d6c900403fc498154ec74eaf420e7338675cad7f19332b4a56be4ff946b662a3c2d217efbe4dc646fb742b8c62bfe8e25fd5dc59e7540695fa8b9cd5bfd9f92dfad009d230203010001");
                passGuardEdit.setEccKey("9C59F5DFCC66B5ADCBA4528CC86C8ABCB27E650D2A83BDFA0F6CC2F9C4FF7462|2D6AD73F8D11567A33F5648390F91488829EDDF31754012E4DED88C07B66184C");
                Log.e("TAG", getBinding().edt.getMD5() + "\n" + passGuardEdit.getRSACiphertext() + "\n" + passGuardEdit.getRSAAESCiphertext());
            }
        });
    }

    private void initPassGuard() {
        PassGuardEdit
                .setLicense("Tk9YMFlQSnlnOEF5dkpORCsrUGVSNWpCU3lFb1JtWklZYWxweWRrOEZz" +
                        "MUJ6Q3M1Z3Jxcm40TDFESmpnOVpkeUVvN0xZOFh0SzZwOWZ1eWdnUGtY" +
                        "enNCQmNKMkFRY2l2cTlETG9WTEQ3aGN6ejgyMUwvbk4zUEZpamtaM3BC" +
                        "S3VKcksrT2N5Y1Rqb1VDc2JiNzFBTVpGSFJydGxGRUZVZGxZZnRlcWdU" +
                        "ZXVnPXsiaWQiOjAsInR5cGUiOiJ0ZXN0IiwicGxhdGZvcm0iOjIsIm5v" +
                        "dGJlZm9yZSI6IjIwMTgwMjAxIiwibm90YWZ0ZXIiOiIyMDE4MDUwMSJ9");
        PassGuardEdit.antiScreenShot(this);
        PassGuardEdit.setNO_OFF(true);
        getBinding().edt.setButtonPress(true);
    }

    private void showNormalDialog(String title, String msg) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setTitle(title);
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        // 显示
        normalDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!PassGuardEdit.checkActivity(this)) {
            Toast.makeText(this, "您的XX界面被覆盖，请确认环境是否安全！", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
