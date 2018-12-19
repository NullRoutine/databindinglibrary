package com.twq.databindinghelper.net;


import com.twq.databindinghelper.util.LogUtil;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * retrofit
 * Created by Administrator on 2017/8/24 0024.
 */

public class RetrofitUtil {

    public static volatile RetrofitUtil retrofitUtil;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(getBaseURL())
            .client(OkHttpUtils.getInstance())
//            .addConverterFactory(RsaGsonConverterFactory.create())
            .addConverterFactory(FastJsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    public RetrofitUtil() {
    }

    public <T> T getApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }

    /**
     * 服务接口单例
     *
     * @return Retrofit
     */
    public static synchronized RetrofitUtil build() {
        if (retrofitUtil == null) {//双重检测同步延迟加载
            synchronized (RetrofitUtil.class) {
                if (retrofitUtil == null) {
                    retrofitUtil = new RetrofitUtil();
                }
            }
        }
        return retrofitUtil;
    }


    /**
     * 获取URL  根据版本切换不同版本
     *
     * @return
     */
    public static String getBaseURL() {
        if (LogUtil.isDebug) {
            return "http://test.nbzhwj.cn/ydzf/";//测试
        } else {
            return "http://sys.nbzhwj.cn/ydzf/";//正式
        }
    }
}
