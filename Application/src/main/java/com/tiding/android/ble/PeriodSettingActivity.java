package com.tiding.android.ble;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.tiding.android.ble.param.PeriodSetting;

public class PeriodSettingActivity extends BaseActivity {
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_PERIOD_SETTING = "EXTRA_PERIOD_SETTING";
    public static final String EXTRA_POSITION = "EXTRA_POSITION";

    public static final int REQUEST_CODE = 1;
    public static final int RESULT_CODE_SUBMIT = 1;

    private int mPosition;

    private EditText mStartHourEditText;
    private EditText mStartMinuteEditText;
    private EditText mStopHourEditText;
    private EditText mStopMinuteEditText;
    private EditText mStartDurationSecondEditText;
    private EditText mStopDurationMinuteEditText;
    private Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_setting);

        mStartHourEditText = findViewById(R.id.start_hour);
        mStartMinuteEditText = findViewById(R.id.start_minute);
        mStopHourEditText = findViewById(R.id.stop_hour);
        mStopMinuteEditText = findViewById(R.id.stop_minute);
        mStartDurationSecondEditText = findViewById(R.id.start_duration_second);
        mStopDurationMinuteEditText = findViewById(R.id.stop_duration_minute);
        mSubmit = findViewById(R.id.submit);

        Intent intent = getIntent();
        String title = intent.getStringExtra(EXTRA_TITLE);
        getActionBar().setTitle(title);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mPosition = intent.getIntExtra(EXTRA_POSITION, 0);
        Bundle bundle = intent.getExtras();
        PeriodSetting setting = (PeriodSetting) bundle.getSerializable(EXTRA_PERIOD_SETTING);

        mStartHourEditText.setText(String.valueOf(setting.getmStartTimeHour()));
        mStartMinuteEditText.setText(String.valueOf(setting.getmStartTimeMinute()));
        mStopHourEditText.setText(String.valueOf(setting.getmStopTimeHour()));
        mStopMinuteEditText.setText(String.valueOf(setting.getmStopTimeMinute()));
        mStartDurationSecondEditText.setText(String.valueOf(setting.getmStartDurationSeconds()));
        mStopDurationMinuteEditText.setText(String.valueOf(setting.getmStopDurationMins()));

        mSubmit.setOnClickListener(v -> {
            PeriodSetting setting1 = new PeriodSetting(
                    Integer.parseInt(mStartHourEditText.getText().toString()),
                    Integer.parseInt(mStartMinuteEditText.getText().toString()),
                    Integer.parseInt(mStopHourEditText.getText().toString()),
                    Integer.parseInt(mStopMinuteEditText.getText().toString()),
                    Integer.parseInt(mStartDurationSecondEditText.getText().toString()),
                    Integer.parseInt(mStopDurationMinuteEditText.getText().toString())
            );
            Intent intent1 = new Intent();
            intent1.putExtra(EXTRA_POSITION, mPosition);
            Bundle bundle1 = new Bundle();
            bundle1.putSerializable(EXTRA_PERIOD_SETTING, setting1);
            intent1.putExtras(bundle1);
            setResult(RESULT_CODE_SUBMIT, intent1);
            finish();
        });
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
}
