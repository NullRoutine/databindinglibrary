package com.twq.databindinghelper.module.choosepicture;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.twq.databindinghelper.BuildConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.functions.Consumer;

/**
 * 拍照图库
 * Created by tang.wangqiang on 2018/5/29.
 */

public class PictureUtil {

    public static final String APP_NAME = "NULLROUTINE";
    public static final String CAMERA_PATH = "/" + APP_NAME + "/CameraImage/";
    public static final String POSTFIX = ".JPEG";
    private Activity activity;
    private File cameraFile;
    private File imageFile;
    Uri imageUri, cameraUri;
    private File folderDir;

    public final static int REQUEST_IMAGE_PHOTO = 0x01;
    public final static int REQUEST_IMAGE_CAMERA = 0x02;
    public final static int REQUEST_IMAGE_CUT_CAMERA = 0x03;

    public PictureUtil(Activity activity) {
        this.activity = activity;
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory() : activity.getCacheDir();
        folderDir = new File(rootDir.getAbsolutePath() + CAMERA_PATH);
        createNewFile();

    }

    /**
     * 防止加载缓存，也可换成bitmap加载
     */
    private void createNewFile() {
        try {
            if (!folderDir.exists()) {
                folderDir.mkdirs();
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = APP_NAME + "_" + timeStamp + "";
            cameraFile = new File(folderDir, fileName + "camera" + POSTFIX);
            imageFile = new File(folderDir, fileName + "image" + POSTFIX);
            if (cameraFile.exists()) {
                cameraFile.delete();
            }
            if (imageFile.exists()) {
                imageFile.delete();
            }
            cameraFile.createNewFile();
            imageFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getFolderDir() {
        return folderDir;
    }

    public void delitePicture() {
        delAllFile(folderDir.getAbsolutePath());
    }

    //删除指定文件夹下所有文件
    //param path 文件夹完整绝对路径
    public boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 删除文件夹下所有内容
     *
     * @param folderPath（绝对路径）
     */
    public void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 选择相机
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void ChooseCamera() {
        if (hasCamera()) {
            if (hasPermission(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                takeCamera();
            } else {
                RxPermissions rxPermissions = new RxPermissions(activity);
                rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    takeCamera();
                                } else {
                                    Toast.makeText(activity, "请在系统设置中为App开启权限后重试", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        } else {
            Toast.makeText(activity, "没有可用相机", Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void ChooseImage() {
        if (hasSdcard()) {
            if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                choosePicture();
            } else {
                RxPermissions rxPermissions = new RxPermissions(activity);
                rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    choosePicture();
                                } else {
                                    Toast.makeText(activity, "请在系统设置中为App开启权限后重试", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        } else {
            Toast.makeText(activity, "没有SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开图库
     */
    private void choosePicture() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(pickIntent, REQUEST_IMAGE_PHOTO);
    }

    /**
     * 相机
     */
    private void takeCamera() {
        //拍照
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
            String authority = BuildConfig.APPLICATION_ID;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                cameraUri = FileProvider.getUriForFile(activity, authority, cameraFile);//通过FileProvider创建一个content类型的Uri
            } else {
                cameraUri = Uri.fromFile(cameraFile);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            activity.startActivityForResult(cameraIntent, REQUEST_IMAGE_CAMERA);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data, ChooseListener chooseListener) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAMERA:
                    imageUri = Uri.fromFile(imageFile);
                    ActionUtils.startActivityForImageCut(activity, REQUEST_IMAGE_CUT_CAMERA, cameraUri, imageUri, 480, 480);
                    break;
                case REQUEST_IMAGE_PHOTO:
                    if (data != null) {
                        imageUri = Uri.fromFile(imageFile);
                        Uri briefUri = Uri.parse(ActionUtils.getPath(activity, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            String authority = BuildConfig.APPLICATION_ID;
                            briefUri = FileProvider.getUriForFile(activity, authority, new File(briefUri.getPath()));
                        }
                        ActionUtils.startActivityForImageCut(activity, REQUEST_IMAGE_CUT_CAMERA, briefUri, imageUri, 480, 480);
                    }
                    break;
                case REQUEST_IMAGE_CUT_CAMERA:
                    if (chooseListener != null && imageUri != null) {
                        chooseListener.choose(imageUri, imageFile.getPath());
                        Log.e("TAG", imageFile.getPath());
                    }
                    break;
            }
        }
    }


    /**
     * 判断是否有权限
     *
     * @return
     */
    public boolean hasPermission(String... permission) {
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                for (String permissions : permission) {
                    result = ContextCompat.checkSelfPermission(activity, permissions) == PackageManager.PERMISSION_GRANTED;
                    if (!result) {
                        break;
                    }
                }
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                for (String permissions : permission) {
                    result = PermissionChecker.checkSelfPermission(activity, permissions) == PackageManager.PERMISSION_GRANTED;
                    if (!result) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 判断是否有可用相机
     *
     * @return
     */
    private boolean hasCamera() {
        PackageManager pm = activity.getPackageManager();
        // FEATURE_CAMERA - 后置相机
        // FEATURE_CAMERA_FRONT - 前置相机
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                && !pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            Log.i("camera", "non-support");
            return false;
        } else {
            Log.i("camera", "support");
            return true;
        }
    }

    public interface ChooseListener {
        void choose(Uri uri, String path);
    }
}
