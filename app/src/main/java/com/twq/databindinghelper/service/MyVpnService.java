package com.twq.databindinghelper.service;

import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;

import com.twq.databindinghelper.util.LogUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

/**
 * Created by tang.wangqiang on 2018/9/12.
 */

public class MyVpnService extends VpnService {

    private Thread mThread;
    private ParcelFileDescriptor mInterface;
    //a. Configure a builder for the interface.
    Builder builder = new Builder();

    @Override
    public void onCreate() {
        super.onCreate();
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //a. Configure the TUN and get the interface.http://192.168.22.20:8083/
                    mInterface = builder.setSession("MyVPNService")
                            .addAddress("192.168.0.1", 24)
                            .addDnsServer("8.8.8.8")
                            .addRoute("0.0.0.0", 0).establish();
                    //b. Packets to be sent are queued in this input stream.
                    FileInputStream in = new FileInputStream(
                            mInterface.getFileDescriptor());
                    LogUtil.e("==============>" + in.read());
                    //b. Packets received need to be written to this output stream.
                    FileOutputStream out = new FileOutputStream(
                            mInterface.getFileDescriptor());
                    LogUtil.e("===============>" + out.toString());
                    //c. The UDP channel can be used to pass/get ip package to/from server
                    DatagramChannel tunnel = DatagramChannel.open();
                    // Connect to the server, localhost is used for demonstration only.
                    tunnel.connect(new InetSocketAddress("127.0.0.1", 8087));
                    //d. Protect this socket, so package send by it will not be feedback to the vpn service.
                    protect(tunnel.socket());
                    //e. Use a loop to pass packets.
                    while (true) {
                        //get packet with in
                        //put packet to tunnel
                        //get packet form tunnel
                        //return packet with out
                        //sleep is a must
                        Thread.sleep(100);
                    }

                } catch (Exception e) {
                    // Catch any exception
                    e.printStackTrace();
                } finally {
                    try {
                        if (mInterface != null) {
                            mInterface.close();
                            mInterface = null;
                        }
                    } catch (Exception e) {

                    }
                }
            }

        }, "MyVpnRunnable");

        //start the service
        mThread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (mThread != null) {
            mThread.interrupt();
        }
        super.onDestroy();
    }
}
