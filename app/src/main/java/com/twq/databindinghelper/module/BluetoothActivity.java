package com.twq.databindinghelper.module;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityBluetoothBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 蓝牙
 * Created by tang.wangqiang on 2018/5/17.
 */

public class BluetoothActivity extends DataBindingActivity<ActivityBluetoothBinding> {
    private BluetoothAdapter mBluetoothAdapter;

    private static final int REQUEST_ENABLE_BT = 0x01;
    private List<BluetoothDevice> list = new ArrayList<>();

    private MyAdapter adapter;
    private BluetoothGatt bluetoothGatt;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void create(Bundle savedInstanceState) {
        adapter = new MyAdapter(list, this);
        getBinding().recycleView.setLayoutManager(new LinearLayoutManager(this));
        getBinding().recycleView.setAdapter(adapter);
        getBinding().btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                assert bluetoothManager != null;
                mBluetoothAdapter = bluetoothManager.getAdapter();
                if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {//检测蓝牙开启
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            }
        });
        getBinding().btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBinding().tvCoo.setText("暂无设备");
                saomiao();
            }
        });


    }

    // 广播接收器
    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 收到的广播类型
            String action = intent.getAction();
            // 发现设备的广播
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 从intent中获取设备
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 判断是否配对过
                if (device.getBondState() != BluetoothDevice.BOND_BONDED && !list.contains(device)) {
                    // 添加到列表
                    Log.i("TAG", "onLeScan: " + device.getName() + "/t" + device.getAddress() + "/t" + device.getBondState());
                    list.add(device);
                    adapter.setMlist(list);
                }
                // 搜索完成
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                // 关闭进度条
                setProgressBarIndeterminateVisibility(true);
                setTitle("搜索完成！");
            }
        }
    };

    private void saomiao() {
        Thread scanT = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void run() {
                list.clear();
                mBluetoothAdapter.startLeScan(callback);
            }
        });
        scanT.start();
        // 找到设备的广播
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        // 注册广播
        registerReceiver(receiver, filter);
        // 搜索完成的广播
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        // 注册广播
        registerReceiver(receiver, filter);
        // 判断是否在搜索,如果在搜索，就取消搜索
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        // 开始搜索
        mBluetoothAdapter.startDiscovery();
    }

    //扫描回调
    public BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
//            Log.i("TAG", "onLeScan: " + bluetoothDevice.getName() + "/t" + bluetoothDevice.getAddress() + "/t" + bluetoothDevice.getBondState());
            //重复过滤方法，列表中包含不该设备才加入列表中，并刷新列表
            if (!list.contains(bluetoothDevice)) {
                //将设备加入列表数据中
                list.add(bluetoothDevice);
//                adapter.setMlist(list);
            }

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bluetooth;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected String[] getPermission() {
        return new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
        };
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.viewHolder> {

        private List<BluetoothDevice> mlist;
        private Context context;

        MyAdapter(List<BluetoothDevice> mlist, Context context) {
            this.mlist = mlist;
            this.context = context;
        }

        public void setMlist(List<BluetoothDevice> mlist) {
            this.mlist = mlist;
            notifyDataSetChanged();
        }

        @Override
        public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_bluetooth, parent, false);
            viewHolder holder = new viewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final viewHolder holder, int position) {
            final BluetoothDevice bd = mlist.get(position);
            holder.tv_name.setText(bd.getName());
            holder.tv_ip.setText(bd.getAddress());
            holder.root_view.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void onClick(View v) {
                    //连接设备的方法,返回值为bluetoothgatt类型
                    bluetoothGatt = bd.connectGatt(context, false, mBluetoothGattCallback);
                    holder.tv_name.setText("连接" + bd.getName() + "中...");
                }
            });
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return list != null ? list.size() : 0;
        }

        class viewHolder extends RecyclerView.ViewHolder {
            private TextView tv_name, tv_ip;
            private LinearLayout root_view;

            private viewHolder(View itemView) {
                super(itemView);
                tv_name = itemView.findViewById(R.id.tv_name);
                tv_ip = itemView.findViewById(R.id.tv_ip);
                root_view = itemView.findViewById(R.id.root_view);
            }
        }
    }

    private BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyUpdate(gatt, txPhy, rxPhy, status);
        }

        @Override
        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyRead(gatt, txPhy, rxPhy, status);
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, final int newState) {//检测连接状态
            super.onConnectionStateChange(gatt, status, newState);
            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void run() {
                    String status;
                    switch (newState) {
                        //已经连接
                        case BluetoothGatt.STATE_CONNECTED:
                            getBinding().tvCoo.setText("已连接");
//                            //该方法用于获取设备的服务，寻找服务
                            bluetoothGatt.discoverServices();
                            break;
                        //正在连接
                        case BluetoothGatt.STATE_CONNECTING:
                            getBinding().tvCoo.setText("正在连接");
                            break;
                        //连接断开
                        case BluetoothGatt.STATE_DISCONNECTED:
                            getBinding().tvCoo.setText("已断开");
                            break;
                        //正在断开
                        case BluetoothGatt.STATE_DISCONNECTING:
                            getBinding().tvCoo.setText("断开中");
                            break;
                    }
                }
            });
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.d("TAG", "callback characteristic read status " + status + " in thread " + Thread.currentThread());
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("TAG", "read value: " + characteristic.getValue());
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
        }
    };
}
