package com.tiding.android.ble;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tiding.android.ble.param.SilenceSetting;
import com.tiding.android.ble.param.TemperatureCorrectSetting;
import com.tiding.android.ble.param.TimeSetting;
import com.tiding.android.ble.param.WriteOnlySetting;

public class SystemSettingActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {
    private Button mResetLifetimeButton;
    private TextView mLifetimeTextView;
    private Button mResetAllSettingsButton;
    private boolean mResetAllSettingsPressed;

    private TimeSetting mTimeSetting;
    private TemperatureCorrectSetting mTemperatureCorrectSetting;
    private SilenceSetting mSilenceSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);

        mResetAllSettingsPressed = false;
        mLifetimeTextView = findViewById(R.id.lifetime);
        mResetLifetimeButton = findViewById(R.id.reset_lifetime);
        mResetAllSettingsButton = findViewById(R.id.reset_all_settings);

        mResetAllSettingsButton.setOnClickListener(this);
        mResetLifetimeButton.setOnClickListener(this);

        getActionBar().setTitle(R.string.title_activity_system_setting);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        try {
            mTimeSetting = TimeSetting.create(DeviceControlActivity.BluetoothLeServiceInstance);
            mTemperatureCorrectSetting = TemperatureCorrectSetting.create(DeviceControlActivity.BluetoothLeServiceInstance);
            mSilenceSetting = SilenceSetting.create(DeviceControlActivity.BluetoothLeServiceInstance);
        } catch (Exception e) {
            if ("invalid modbus response".equals(e.getMessage())) {
                ToastShow(R.string.invalid_response, Toast.LENGTH_SHORT);
            } else if ("bluetooth adapter not init".equals(e.getMessage())) {
                ToastShow(R.string.bt_adapter_not_init, Toast.LENGTH_SHORT);
            } else {
                ToastShow(R.string.access_failed, Toast.LENGTH_SHORT);
            }
            e.printStackTrace();
        }
        super.onResume();
    }

    private void resetLifeTimeOnClick() {
        try {
            WriteOnlySetting.resetLifeHour(DeviceControlActivity.BluetoothLeServiceInstance);
            mLifetimeTextView.setText(String.valueOf(4000));
        } catch (Exception e) {
            if ("invalid modbus response".equals(e.getMessage())) {
                ToastShow(R.string.invalid_response, Toast.LENGTH_SHORT);
            } else if ("bluetooth adapter not init".equals(e.getMessage())) {
                ToastShow(R.string.bt_adapter_not_init, Toast.LENGTH_SHORT);
            } else {
                ToastShow(R.string.access_failed, Toast.LENGTH_SHORT);
            }
            e.printStackTrace();
        }

        ToastShow(R.string.access_success, Toast.LENGTH_SHORT);
    }

    private void resetAllSettingOnClick() {
        try {
            WriteOnlySetting.resetAllSettings(DeviceControlActivity.BluetoothLeServiceInstance);
        } catch (Exception e) {
            if ("invalid modbus response".equals(e.getMessage())) {
                ToastShow(R.string.invalid_response, Toast.LENGTH_SHORT);
            } else if ("bluetooth adapter not init".equals(e.getMessage())) {
                ToastShow(R.string.bt_adapter_not_init, Toast.LENGTH_SHORT);
            } else {
                ToastShow(R.string.access_failed, Toast.LENGTH_SHORT);
            }
            e.printStackTrace();
        }

        ToastShow(R.string.access_success, Toast.LENGTH_SHORT);
        mResetAllSettingsPressed = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_lifetime:
                resetLifeTimeOnClick();
                break;
            case R.id.reset_all_settings:
                resetAllSettingOnClick();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId != EditorInfo.IME_ACTION_DONE) {
            return false;
        }

        try {
            switch (v.getId()) {
//                case R.id.ble_name:
//                    updateBLEName();
//                    break;
            }
        } catch (Exception e) {
            if ("invalid modbus response".equals(e.getMessage())) {
                ToastShow(R.string.invalid_response, Toast.LENGTH_SHORT);
            } else if ("bluetooth adapter not init".equals(e.getMessage())) {
                ToastShow(R.string.bt_adapter_not_init, Toast.LENGTH_SHORT);
            } else {
                ToastShow(R.string.access_failed, Toast.LENGTH_SHORT);
            }
            e.printStackTrace();
            return false;
        }

        ToastShow(R.string.access_success, Toast.LENGTH_SHORT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(DeviceControlActivity.EXTRA_RESET_ALL_SETTING, mResetAllSettingsPressed);
        if (mResetAllSettingsPressed) {
            ToastShow(R.string.refreshing, Toast.LENGTH_SHORT);
        }
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
