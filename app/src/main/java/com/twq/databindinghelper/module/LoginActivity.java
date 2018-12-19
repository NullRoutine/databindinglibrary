package com.twq.databindinghelper.module;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.twq.databindinghelper.R;
import com.twq.databindinghelper.net.ApiServer;
import com.twq.databindinghelper.net.RetrofitUtil;
import com.twq.databindinghelper.net.RxHelp;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by tang.wangqiang on 2018/10/26.
 */

public class LoginActivity extends AppCompatActivity {

    EditText edt_code, edt_password;
    Button btn_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_login);
        edt_code = findViewById(R.id.edt_code);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin(edt_code.getText().toString(), edt_password.getText().toString());
            }
        });
    }

    private void doLogin(String usr, String pass) {
        Map<String, String> map = new HashMap<>();
        map.put("DEVICE_ID", "318A92B9E1");
        map.put("USER_ID", usr);
        map.put("PASSWORD", pass);
        map.put("VER", "1");
        map.put("BUSI_ID", "0001");
        RetrofitUtil.build().getApi(ApiServer.class)
                .doLogin(map)
                .compose(RxHelp.<JsonObject>applySchedulers())
                .subscribe(new Consumer<JsonObject>() {
                    @Override
                    public void accept(JsonObject jsonObject) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

}
