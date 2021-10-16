package com.tiding.android.ble;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tiding.android.ble.param.FungusTemperatureSetting;

public class FungusTemperatureSettingActivity extends BaseActivity implements TextView.OnEditorActionListener, View.OnClickListener {
    private Button mCoolFunctionButton;             // 降温功能
    private EditText mCoolStartTemperatureEditText; // 降温启动温度
    private EditText mCoolStopTemperatureEditText;  // 降温停止温度
    private EditText mErrorHighTemperatureEditText; // 高温报警温度
    private EditText mErrorLowTemperatureEditText;  // 低温报警温度
    private Button mHeatFunctionButton;             // 加温功能
    private EditText mHeatStartTemperatureEditText; // 降温启动温度
    private EditText mHeatStopTemperatureEditText;  // 降温停止温度
    private Button mLinkNewFanButton;               // 联动新风
    private Button mLinkHumidityButton;             // 联动加湿

    private FungusTemperatureSetting mFungusTemperatureSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fungus_temperature_setting);

        mCoolFunctionButton = findViewById(R.id.fungus_cool_function);
        mCoolStartTemperatureEditText = findViewById(R.id.fungus_cool_start_temperature);
        mCoolStopTemperatureEditText = findViewById(R.id.fungus_cool_stop_temperature);
        mErrorHighTemperatureEditText = findViewById(R.id.fungus_error_high_temperature);
        mErrorLowTemperatureEditText = findViewById(R.id.fungus_error_low_temperature);
        mHeatFunctionButton = findViewById(R.id.fungus_heat_function);
        mHeatStartTemperatureEditText = findViewById(R.id.fungus_heat_start_temperature);
        mHeatStopTemperatureEditText = findViewById(R.id.fungus_heat_stop_temperature);
        mLinkNewFanButton = findViewById(R.id.fungus_link_newfan);
        mLinkHumidityButton = findViewById(R.id.fungus_link_humidity);

        mCoolStopTemperatureEditText.setOnEditorActionListener(this);
        mCoolStartTemperatureEditText.setOnEditorActionListener(this);
        mCoolStopTemperatureEditText.setOnEditorActionListener(this);
        mErrorHighTemperatureEditText.setOnEditorActionListener(this);
        mErrorLowTemperatureEditText.setOnEditorActionListener(this);
        mCoolFunctionButton.setOnClickListener(this);
        mHeatStartTemperatureEditText.setOnEditorActionListener(this);
        mHeatStopTemperatureEditText.setOnEditorActionListener(this);
        mHeatFunctionButton.setOnClickListener(this);
        mLinkNewFanButton.setOnClickListener(this);
        mLinkHumidityButton.setOnClickListener(this);

        getActionBar().setTitle(R.string.title_activity_fungus_temperature_setting);
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
    protected void onResume() {
        try {
            mFungusTemperatureSetting = FungusTemperatureSetting.create(DeviceControlActivity.BluetoothLeServiceInstance);
            mCoolFunctionButton.setText(String.format("%s",
                    mFungusTemperatureSetting.getmFungusCoolFunction() == 0 ? "关闭" : "开启"
            ));
            mCoolStartTemperatureEditText.setText(String.format("%.1f",
                    mFungusTemperatureSetting.getmCoolStartTemperature() / 10.
            ));
            mCoolStopTemperatureEditText.setText(String.format("%.1f",
                    mFungusTemperatureSetting.getmCoolStopTemperature() / 10.
            ));
            mErrorHighTemperatureEditText.setText(String.format("%.1f",
                    mFungusTemperatureSetting.getmFungusHighTemperatureWarning() / 10.
            ));
            mErrorLowTemperatureEditText.setText(String.format("%.1f",
                    mFungusTemperatureSetting.getmFungusLowTemperatureWarning() / 10.
            ));
            mHeatFunctionButton.setText(String.format("%s",
                    mFungusTemperatureSetting.getmFungusHeatFunction() == 0 ? "关闭" : "开启"
            ));
            mHeatStartTemperatureEditText.setText(String.format("%.1f",
                    mFungusTemperatureSetting.getmHeatStartTemperature() / 10.
            ));
            mHeatStopTemperatureEditText.setText(String.format("%.1f",
                    mFungusTemperatureSetting.getmHeatStopTemperature() / 10.
            ));
            mLinkNewFanButton.setText(String.format("%s",
                    mFungusTemperatureSetting.getmLinkNewFan() == 0 ? "关闭" : "开启"
            ));
            mLinkHumidityButton.setText(String.format("%s",
                    mFungusTemperatureSetting.getmLinkHumidity() == 0 ? "关闭" : "开启"
            ));
        } catch (Exception e) {
            mCoolFunctionButton.setEnabled(false);
            mCoolStartTemperatureEditText.setEnabled(false);
            mCoolStopTemperatureEditText.setEnabled(false);
            mErrorHighTemperatureEditText.setEnabled(false);
            mErrorLowTemperatureEditText.setEnabled(false);
            mHeatFunctionButton.setEnabled(false);
            mHeatStartTemperatureEditText.setEnabled(false);
            mHeatStopTemperatureEditText.setEnabled(false);
            mLinkNewFanButton.setEnabled(false);
            mLinkHumidityButton.setEnabled(false);

            super.onResume();
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
        ToastShow(R.string.init_complete, Toast.LENGTH_SHORT);
        super.onResume();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId != EditorInfo.IME_ACTION_DONE) {
            return false;
        }
        try {
            switch (v.getId()) {
                case R.id.fungus_cool_start_temperature:
                    updateCoolStartTemperature();
                    break;
                case R.id.fungus_cool_stop_temperature:
                    updateCoolStopTemperature();
                    break;
                case R.id.fungus_error_high_temperature:
                    updateErrorHighTemperature();
                    break;
                case R.id.fungus_error_low_temperature:
                    updateErrorLowTemperature();
                    break;
                case R.id.fungus_heat_start_temperature:
                    updateHeatStartTemperature();
                    break;
                case R.id.fungus_heat_stop_temperature:
                    updateHeatStopTemperature();
                    break;
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fungus_cool_function:
                coolFunctionOnClick();
                break;
            case R.id.fungus_heat_function:
                heatFunctionOnClick();
                break;
            case R.id.fungus_link_newfan:
                linkNewFanOnClick();
                break;
            case R.id.fungus_link_humidity:
                linkHumidityOnClick();
                break;
        }
    }

    private void updateCoolStartTemperature() throws Exception {
        mFungusTemperatureSetting.setmCoolStartTemperature(
                (int) (Float.parseFloat(mCoolStartTemperatureEditText.getText().toString()) * 10),
                DeviceControlActivity.BluetoothLeServiceInstance
        );
    }

    private void updateCoolStopTemperature() throws Exception {
        mFungusTemperatureSetting.setmCoolStopTemperature(
                (int) (Float.parseFloat(mCoolStopTemperatureEditText.getText().toString()) * 10),
                DeviceControlActivity.BluetoothLeServiceInstance
        );
    }

    private void updateErrorHighTemperature() throws Exception {
        mFungusTemperatureSetting.setmFungusHighTemperatureWarning(
                (int) (Float.parseFloat(mErrorHighTemperatureEditText.getText().toString()) * 10),
                DeviceControlActivity.BluetoothLeServiceInstance
        );
    }

    private void updateErrorLowTemperature() throws Exception {
        mFungusTemperatureSetting.setmFungusLowTemperatureWarning(
                (int) (Float.parseFloat(mErrorLowTemperatureEditText.getText().toString()) * 10),
                DeviceControlActivity.BluetoothLeServiceInstance
        );
    }

    private void updateHeatStartTemperature() throws Exception {
        mFungusTemperatureSetting.setmHeatStartTemperature(
                (int) (Float.parseFloat(mHeatStartTemperatureEditText.getText().toString()) * 10),
                DeviceControlActivity.BluetoothLeServiceInstance
        );
    }

    private void updateHeatStopTemperature() throws Exception {
        mFungusTemperatureSetting.setmHeatStopTemperature(
                (int) (Float.parseFloat(mHeatStopTemperatureEditText.getText().toString()) * 10),
                DeviceControlActivity.BluetoothLeServiceInstance
        );
    }

    private void coolFunctionOnClick() {
        try {
            mFungusTemperatureSetting.setmFungusCoolFunction(
                    (mFungusTemperatureSetting.getmFungusCoolFunction() + 1) % 2,
                    DeviceControlActivity.BluetoothLeServiceInstance
            );
            mCoolFunctionButton.setText(mFungusTemperatureSetting.getmFungusCoolFunction() == 0 ? R.string.stop : R.string.open);
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
    }

    private void heatFunctionOnClick() {
        try {
            mFungusTemperatureSetting.setmFungusHeatFunction(
                    (mFungusTemperatureSetting.getmFungusHeatFunction() + 1) % 2,
                    DeviceControlActivity.BluetoothLeServiceInstance
            );
            mHeatFunctionButton.setText(mFungusTemperatureSetting.getmFungusHeatFunction() == 0 ? R.string.stop : R.string.open);
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
    }

    private void linkNewFanOnClick() {
        try {
            mFungusTemperatureSetting.setmLinkNewFan(
                    (mFungusTemperatureSetting.getmLinkNewFan() + 1) % 2,
                    DeviceControlActivity.BluetoothLeServiceInstance
            );
            mLinkNewFanButton.setText(mFungusTemperatureSetting.getmLinkNewFan() == 0 ? R.string.stop : R.string.open);
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
    }

    private void linkHumidityOnClick() {
        try {
            mFungusTemperatureSetting.setmLinkHumidity(
                    (mFungusTemperatureSetting.getmLinkHumidity() + 1) % 2,
                    DeviceControlActivity.BluetoothLeServiceInstance
            );
            mLinkHumidityButton.setText(mFungusTemperatureSetting.getmLinkHumidity() == 0 ? R.string.stop : R.string.open);
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
    }
}
