package com.tiding.android.ble.param;

import com.tiding.android.ble.BluetoothLeService;

import java.io.Serializable;

public class PeriodSetting extends Object implements Serializable {
    private int mStartTimeHour;
    private int mStartTimeMinute;
    private int mStopTimeHour;
    private int mStopTimeMinute;
    private int mStartDurationSeconds;
    private int mStopDurationMins;

    public int getmStartTimeHour() {
        return mStartTimeHour;
    }

    public int getmStartTimeMinute() {
        return mStartTimeMinute;
    }

    public int getmStopTimeHour() {
        return mStopTimeHour;
    }

    public int getmStopTimeMinute() {
        return mStopTimeMinute;
    }

    public int getmStartDurationSeconds() {
        return mStartDurationSeconds;
    }

    public int getmStopDurationMins() {
        return mStopDurationMins;
    }

    public PeriodSetting(int mStartTimeHour, int mStartTimeMinute, int mStopTimeHour,
                         int mStopTimeMinute, int mStartDurationSeconds, int mStopDurationMins) {
        this.mStartTimeHour = mStartTimeHour;
        this.mStartTimeMinute = mStartTimeMinute;
        this.mStopTimeHour = mStopTimeHour;
        this.mStopTimeMinute = mStopTimeMinute;
        this.mStartDurationSeconds = mStartDurationSeconds;
        this.mStopDurationMins = mStopDurationMins;
    }

    public void setPeriodSetting(PeriodSetting setting, int address, BluetoothLeService service) throws Exception {
        for (int i = 0; i < 6; i++) {
            int data = 0;
            switch (i) {
                case 0:
                    data = setting.getmStartTimeHour();
                    break;
                case 1:
                    data = setting.getmStartTimeMinute();
                    break;
                case 2:
                    data = setting.getmStopTimeHour();
                    break;
                case 3:
                    data = setting.getmStopTimeMinute();
                    break;
                case 4:
                    data = setting.getmStartDurationSeconds();
                    break;
                case 5:
                    data = setting.getmStopDurationMins();
                    break;
            }

            byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(address + i, data);
            byte[] respFrame = service.sendToSerial(reqFrame, 8);
            if (!ModbusParam.isValid(reqFrame, respFrame)) {
                throw new Exception("invalid modbus response");
            }
        }
        this.mStartTimeHour = setting.getmStartTimeHour();
        this.mStartTimeMinute = setting.getmStartTimeMinute();
        this.mStopTimeHour = setting.getmStopTimeHour();
        this.mStopTimeMinute = setting.getmStopTimeMinute();
        this.mStartDurationSeconds = setting.getmStartDurationSeconds();
        this.mStopDurationMins = setting.getmStopDurationMins();
    }
}
