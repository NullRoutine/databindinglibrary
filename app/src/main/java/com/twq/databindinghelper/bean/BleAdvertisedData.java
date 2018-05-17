package com.twq.databindinghelper.bean;

import java.util.List;
import java.util.UUID;

/**
 * Created by tang.wangqiang on 2018/5/17.
 */

public class BleAdvertisedData {
    private List<UUID> mUuids;
    private String mName;
    private String ip;

    public BleAdvertisedData(List<UUID> mUuids, String mName) {
        this.mUuids = mUuids;
        this.mName = mName;
    }

    public List<UUID> getmUuids() {
        return mUuids;
    }

    public void setmUuids(List<UUID> mUuids) {
        this.mUuids = mUuids;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
