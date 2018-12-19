package com.twq.databindinghelper.net;

import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * 接口
 * Created by tang.wangqiang on 2018/4/9.
 */

public interface ApiServer {

    @POST("auth")
    Observable<JsonObject> doLogin(@QueryMap Map<String, String> map);
}
