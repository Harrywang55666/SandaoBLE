/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tiding.android.ble;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tiding.android.ble.param.ErrorSetting;
import com.tiding.android.ble.param.TimeSetting;
import com.tiding.android.ble.param.MainSetting;

import java.util.Calendar;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class DeviceControlActivity extends BaseActivity implements View.OnClickListener {
    public static final String EXTRA_BUNDLE = "bundle";
    public static final String EXTRA_MAIN_SETTING = "main_setting";
    public static final String EXTRA_RESET_ALL_SETTING = "reset_all_setting";

    private final static int REQUEST_SYSTEM_ACTIVITY = 1;
    private final static String TAG = DeviceControlActivity.class.getSimpleName();
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static BluetoothLeService BluetoothLeServiceInstance;

    private TextView mConnectionStateTextView;          // 连接状态
    private TextView mDeviceAddressTextView;            // 设备地址
    private TextView mNowTimeTextView;                  // 当前时间
    private TextView mHouseTemperatureTextView;         // 棚内温度
    private TextView mHouseRunningStatusTextView;       // 棚内运行状态
    private TextView mHouseTemperatureStatusTextView;   // 棚内温度状态
    private TextView mFungusTemperatureTextView;        // 菌包温度
    private TextView mFungusRunningStatusTextView;      // 菌包运行状态
    private TextView mFungusTemperatureStatusTextView;  // 菌包温度状态
    private TextView mHumidityControlModeTextView;      // 加湿控制模式
    private TextView mHumidityStatusTextView;           // 加湿运行状态
    private TextView mNewFanControlModeTextView;        // 新风控制模式
    private TextView mNewFanStatusTextView;             // 新风运行状态
    private Button mHouseSettingButton;                 // 棚内设置
    private Button mFungusSettingButton;                // 菌包设置
    private Button mHumiditySettingButton;              // 加湿设置
    private Button mNewFanSettingButton;                // 新风设置
    private Button mSystemSettingButton;                // 系统设置
    private LinearLayout mLayout;                       // 故障面板
    private TextView mErrorInfoTextView;                // 故障标题
    private Button mRefreshErrorButton;                 // 刷新故障

    private String mDeviceName;
    private String mDeviceAddress;
    private boolean mConnected = false;
    private boolean mDiscovered = false;
    private boolean mLoading = true;
    private MainSetting mMainSetting;

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            BluetoothLeServiceInstance = ((BluetoothLeService.LocalBinder) service).getService();
            if (!BluetoothLeServiceInstance.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            BluetoothLeServiceInstance.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            BluetoothLeServiceInstance = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                mDiscovered = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                updateUI();
            } else if (BluetoothLeService.ACTION_GATT_DISCOVERED.equals(action)) {
                mDiscovered = true;
                updateUI();
            }
        }
    };

    /**
     * 根据mConnected更新UI
     */
    private void updateUI() {
        if (!mDiscovered) {
            // 清理UI
            mHouseSettingButton.setEnabled(false);
            mFungusSettingButton.setEnabled(false);
            mHumiditySettingButton.setEnabled(false);
            mNewFanSettingButton.setEnabled(false);
            mSystemSettingButton.setEnabled(false);
            mRefreshErrorButton.setEnabled(false);
            return;
        }

        mLoading = true;
        invalidateOptionsMenu();

        // 获取串口参数
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR); // 更新数据的时候已经默认20开头
        int month = cal.get(Calendar.MONTH) + 1; // Java January从0开始
        int day = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int weekDay = cal.get(Calendar.DAY_OF_WEEK) - 1; // Java Monday从第1开始
        TimeSetting timeSetting;
        try {
            mMainSetting = MainSetting.create(BluetoothLeServiceInstance);
            timeSetting = new TimeSetting(
                    year, month, day, hour, minute
            );
            TimeSetting.SetTimeSetting(timeSetting, BluetoothLeServiceInstance);
        } catch (Exception e) {
            if ("invalid modbus response".equals(e.getMessage())) {
                ToastShow(R.string.invalid_response, Toast.LENGTH_SHORT);
            } else if ("bluetooth adapter not init".equals(e.getMessage())) {
                ToastShow(R.string.bt_adapter_not_init, Toast.LENGTH_SHORT);
            } else {
                ToastShow(R.string.access_failed, Toast.LENGTH_SHORT);
            }
            e.printStackTrace();
            return;
        }

        // 更新UI界面
        mNowTimeTextView.setText(
                String.format("%d-%02d-%02d %02d:%02d %s",
                        year, month, day, hour, minute,
                        timeSetting.DayString(weekDay)
                )
        );
        mHouseTemperatureTextView.setText(
                String.format("%.1f", mMainSetting.getmHouseTemperature() / 10.)
        );
        mHouseRunningStatusTextView.setText(
                String.format("降温: %s 加温: %s",
                        mMainSetting.getmHouseCoolRunStatus() == 0 ? "停止" : "运行",
                        mMainSetting.getmHouseHeatRunStatus() == 0 ? "停止" : "运行"
                )
        );
        mHouseTemperatureStatusTextView.setText(
                String.format("%s",
                        new String[]{"无", "正常", "故障"}[mMainSetting.getmHouseTemperatureStatus()]
                )
        );
        mFungusTemperatureTextView.setText(
                String.format("%.1f", mMainSetting.getmFungusTemperature() / 10.)
        );
        mFungusRunningStatusTextView.setText(
                String.format("降温: %s 加温: %s",
                        mMainSetting.getmFungusCoolRunStatus() == 0 ? "停止" : "运行",
                        mMainSetting.getmFungusHeatRunStatus() == 0 ? "停止" : "运行"
                )
        );
        mFungusTemperatureStatusTextView.setText(
                String.format("%s",
                        new String[]{"无", "正常", "故障"}[mMainSetting.getmFungusTemperatureStatus()]
                )
        );
        mHumidityControlModeTextView.setText(
                String.format("%s",
                        new String[]{"手动开", "自动", "手动关"}[mMainSetting.getmHumidityControlMode()])
        );
        mHumidityStatusTextView.setText(
                String.format("%s",
                        mMainSetting.getmHumidityRunStatus() == 0 ? "停止" : "运行")
        );
        mNewFanControlModeTextView.setText(
                String.format("%s",
                        new String[]{"手动开", "自动", "手动关"}[mMainSetting.getmNewFanControlMode()])
        );
        mNewFanStatusTextView.setText(
                String.format("%s",
                        mMainSetting.getmNewFanRunStatus() == 0 ? "停止" : "运行")
        );

        mHouseSettingButton.setEnabled(true);
        mFungusSettingButton.setEnabled(true);
        mHumiditySettingButton.setEnabled(true);
        mNewFanSettingButton.setEnabled(true);
        mSystemSettingButton.setEnabled(true);
        mRefreshErrorButton.setEnabled(true);

        reloadErrorInfo();
        ToastShow(R.string.init_complete, Toast.LENGTH_SHORT);

        mLoading = false;
        invalidateOptionsMenu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        // Sets up UI references.
        mDeviceAddressTextView = findViewById(R.id.device_address);
        mConnectionStateTextView = findViewById(R.id.connection_state);
        mNowTimeTextView = findViewById(R.id.datetime);
        mHouseTemperatureTextView = findViewById(R.id.house_temperature);
        mHouseRunningStatusTextView = findViewById(R.id.house_running_status);
        mHouseTemperatureStatusTextView = findViewById(R.id.house_temperature_status);
        mFungusTemperatureTextView = findViewById(R.id.fungus_temperature);
        mFungusRunningStatusTextView = findViewById(R.id.fungus_running_status);
        mFungusTemperatureStatusTextView = findViewById(R.id.fungus_temperature_status);
        mHumidityControlModeTextView = findViewById(R.id.humidity_control_mode);
        mHumidityStatusTextView = findViewById(R.id.humidity_status);
        mNewFanControlModeTextView = findViewById(R.id.newfan_control_mode);
        mNewFanStatusTextView = findViewById(R.id.newfan_status);
        mHouseSettingButton = findViewById(R.id.house_temperature_setting);
        mFungusSettingButton = findViewById(R.id.fungus_temperature_setting);
        mHumiditySettingButton = findViewById(R.id.humidity_setting);
        mNewFanSettingButton = findViewById(R.id.newfan_setting);
        mErrorInfoTextView = findViewById(R.id.error_setting_info);
        mLayout = findViewById(R.id.layout);
        mSystemSettingButton = findViewById(R.id.system_setting);
        mRefreshErrorButton = findViewById(R.id.refresh_error);

        // Sets up listeners
        mHouseSettingButton.setOnClickListener(this);
        mFungusSettingButton.setOnClickListener(this);
        mHumiditySettingButton.setOnClickListener(this);
        mNewFanSettingButton.setOnClickListener(this);
        mSystemSettingButton.setOnClickListener(this);
        mRefreshErrorButton.setOnClickListener(this);

        // Sets up UI
        mDeviceAddressTextView.setText(mDeviceAddress);
        getActionBar().setTitle(mDeviceName);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        updateUI();

        // Create service
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void onClick(View v) {
        if (!mConnected) {
            ToastShow(R.string.bt_adapter_not_init, Toast.LENGTH_SHORT);
            return;
        }
        switch (v.getId()) {
            case R.id.house_temperature_setting:
                houseTemperatureSettingOnClick();
                break;
            case R.id.fungus_temperature_setting:
                fungusTemperatureSettingOnClick();
                break;
            case R.id.humidity_setting:
                humiditySettingOnClick();
                break;
            case R.id.newfan_setting:
                newFanSettingOnClick();
                break;
            case R.id.system_setting:
                systemSettingOnClick();
                break;
            case R.id.refresh_error:
                reloadErrorInfo();
                break;
        }
    }

    private void houseTemperatureSettingOnClick() {
        final Intent intent = new Intent(this, HouseTemperatureSettingActivity.class);
        startActivity(intent);
    }

    private void fungusTemperatureSettingOnClick() {
        final Intent intent = new Intent(this, FungusTemperatureSettingActivity.class);
        startActivity(intent);
    }

    private void humiditySettingOnClick() {
        final Intent intent = new Intent(this, HumiditySettingActivity.class);
        startActivity(intent);
    }

    private void newFanSettingOnClick() {
        final Intent intent = new Intent(this, NewFanSettingActivity.class);
        startActivity(intent);
    }

    private void systemSettingOnClick() {
        final Intent intent = new Intent(this, SystemSettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_MAIN_SETTING, mMainSetting);
        intent.putExtra(EXTRA_BUNDLE, bundle);
        startActivityForResult(intent, REQUEST_SYSTEM_ACTIVITY);
    }

    private void reloadErrorInfo() {
        int[] errorResIDs;
        try {
            errorResIDs = ErrorSetting.create(BluetoothLeServiceInstance).getErrorResIDs();
        } catch (Exception e) {
            if ("invalid modbus response".equals(e.getMessage())) {
                ToastShow(R.string.invalid_response, Toast.LENGTH_SHORT);
            } else if ("bluetooth adapter not init".equals(e.getMessage())) {
                ToastShow(R.string.bt_adapter_not_init, Toast.LENGTH_SHORT);
            } else {
                ToastShow(R.string.access_failed, Toast.LENGTH_SHORT);
            }
            e.printStackTrace();
            return;
        }

        mLayout.removeAllViews();
        if (errorResIDs.length == 0) {
            mErrorInfoTextView.setText(R.string.error_setting_info);
            TextView textView = new TextView(this);
            textView.setText("无故障！");
            ToastShow(R.string.access_success, Toast.LENGTH_SHORT);
            return;
        }

        mErrorInfoTextView.setText(String.format("故障信息(%d):", errorResIDs.length));
        for (int i = 0; i < errorResIDs.length; i++) {
            TextView textView = new TextView(this);
            textView.setText(errorResIDs[i]);
            textView.setTextColor(Color.RED);
            mLayout.addView(textView);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (BluetoothLeServiceInstance != null) {
            final boolean result = BluetoothLeServiceInstance.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        BluetoothLeServiceInstance = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        if (mLoading)
            menu.findItem(R.id.menu_refresh).setActionView(R.layout.actionbar_indeterminate_progress);
        else
            menu.findItem(R.id.menu_refresh).setActionView(null);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_connect:
                mLoading = true;
                invalidateOptionsMenu();
                BluetoothLeServiceInstance.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mLoading = false;
                invalidateOptionsMenu();
                BluetoothLeServiceInstance.disconnect();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(() -> mConnectionStateTextView.setText(resourceId));
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCOVERED);
        return intentFilter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode != RESULT_OK) {
            ToastShow(R.string.result_code_invalid, Toast.LENGTH_SHORT);
            return;
        }

        switch (requestCode) {
            case REQUEST_SYSTEM_ACTIVITY:
                if (intent.getBooleanExtra(EXTRA_RESET_ALL_SETTING, false)) {
                    ToastShow(R.string.refreshing, Toast.LENGTH_SHORT);
                    updateUI();
                }
                break;
        }
    }
}
