package com.tiding.android.ble;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.tiding.android.ble.param.HumiditySetting;
import com.tiding.android.ble.param.PeriodSetting;

public class HumiditySettingActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Button mControlModeButton;
    private ListView mPeriodListView;
    private PeriodListAdapter mPeriodListAdapter;

    private HumiditySetting mHumiditySetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity_setting);

        mControlModeButton = findViewById(R.id.humidity_control_mode_btn);
        mPeriodListView = findViewById(R.id.humidity_period_list);

        mControlModeButton.setOnClickListener(this);
        mPeriodListView.setOnItemClickListener(this);
        mPeriodListAdapter = new PeriodListAdapter(this.getLayoutInflater());
        mPeriodListView.setAdapter(mPeriodListAdapter);

        getActionBar().setTitle(R.string.title_activity_humidity_setting);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        try {
            mHumiditySetting = HumiditySetting.create(DeviceControlActivity.BluetoothLeServiceInstance);
        } catch (Exception e) {
            mControlModeButton.setEnabled(false);
            mPeriodListView.setEnabled(false);
            super.onResume();
            if ("invalid modbus response".equals(e.getMessage())) {
                ToastShow(R.string.invalid_response, Toast.LENGTH_SHORT);
            } else if ("bluetooth adapter not init".equals(e.getMessage())) {
                ToastShow(R.string.bt_adapter_not_init, Toast.LENGTH_SHORT);
            } else {
                ToastShow(R.string.access_failed, Toast.LENGTH_SHORT);
            }
            return;
        }

        String text;
        switch (mHumiditySetting.getmControlMode()) {
            case 0:
                text = "手动开";
                break;
            case 1:
                text = "自动";
                break;
            default:
                text = "手动关";
                break;
        }
        mControlModeButton.setText(text);
        mPeriodListAdapter.setmList(mHumiditySetting.getmPeriodSettings());
        ToastShow(R.string.init_complete, Toast.LENGTH_SHORT);
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 如果不是通过确定按钮提交，则无需处理
        if (resultCode != PeriodSettingActivity.RESULT_CODE_SUBMIT) {
            return;
        }
        int position = data.getIntExtra(PeriodSettingActivity.EXTRA_POSITION, 0);
        PeriodSetting setting = (PeriodSetting) data.getExtras().getSerializable(PeriodSettingActivity.EXTRA_PERIOD_SETTING);
        try {
            mHumiditySetting.setPeriodSettingItem(setting, position, DeviceControlActivity.BluetoothLeServiceInstance);
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

        mPeriodListAdapter.setmListItem(position, setting);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.humidity_control_mode_btn:
                controlModeOnClick();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, PeriodSettingActivity.class);
        intent.putExtra(PeriodSettingActivity.EXTRA_TITLE, String.format("%02d时段", position + 1));
        intent.putExtra(PeriodSettingActivity.EXTRA_POSITION, position);
        Bundle bundle = new Bundle();
        PeriodSetting periodSetting = mPeriodListAdapter.getmListItem(position);
        bundle.putSerializable(PeriodSettingActivity.EXTRA_PERIOD_SETTING, periodSetting);
        intent.putExtras(bundle);
        startActivityForResult(intent, PeriodSettingActivity.REQUEST_CODE);
    }

    private void controlModeOnClick() {
        try {
            mHumiditySetting.setmControlMode((mHumiditySetting.getmControlMode() + 1) % 3,
                    DeviceControlActivity.BluetoothLeServiceInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String text;
        switch (mHumiditySetting.getmControlMode()) {
            case 0:
                text = "手动开";
                break;
            case 1:
                text = "自动";
                break;
            default:
                text = "手动关";
                break;
        }
        mControlModeButton.setText(text);
    }
}
