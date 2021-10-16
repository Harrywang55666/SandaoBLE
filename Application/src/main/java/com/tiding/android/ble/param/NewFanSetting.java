package com.tiding.android.ble.param;

import com.tiding.android.ble.BluetoothLeService;

public class NewFanSetting extends Object {
    public static final byte[] REQ_FRAME = ModbusParam.getReadMultiReqFrame(0x1100, 0x19);
    private PeriodSetting[] mPeriodSettings;
    private int mControlMode;

    public static NewFanSetting create(BluetoothLeService service) throws Exception {
        byte[] respFrame = service.sendToSerial(REQ_FRAME, ModbusParam.respFrameMustLen(REQ_FRAME));
        if (!ModbusParam.isValid(REQ_FRAME, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        int[] respData = ModbusParam.parseRespData(respFrame);
        int mControlMode = respData[0];

        NewFanSetting result = new NewFanSetting();
        PeriodSetting[] mPeriodSettings = new PeriodSetting[4];
        // 从第2个元素开始，第1个是控制模式
        for (int i = 1; i < respData.length; i += 6) {
            int startTimeHour = 0, startTimeMinute = 0, stopTimeHour = 0, stopTimeMinute = 0, startDurationSeconds = 0, stopDurationMins = 0;
            for (int j = 0; j < 6; j++) {
                switch (j) {
                    case 0:
                        startTimeHour = respData[i + j];
                        break;
                    case 1:
                        startTimeMinute = respData[i + j];
                        break;
                    case 2:
                        stopTimeHour = respData[i + j];
                        break;
                    case 3:
                        stopTimeMinute = respData[i + j];
                        break;
                    case 4:
                        startDurationSeconds = respData[i + j];
                        break;
                    case 5:
                        stopDurationMins = respData[i + j];
                        break;
                }
            }
            mPeriodSettings[i / 6] = new PeriodSetting(
                    startTimeHour, startTimeMinute,
                    stopTimeHour, stopTimeMinute,
                    startDurationSeconds, stopDurationMins
            );
        }
        result.mPeriodSettings = mPeriodSettings;
        result.mControlMode = mControlMode;
        return result;
    }

    public PeriodSetting[] getmPeriodSettings() {
        return mPeriodSettings;
    }

    public int getmControlMode() {
        return mControlMode;
    }

    public void setPeriodSettingItem(PeriodSetting setting, int position, BluetoothLeService service) throws Exception {
        int address = (0x11 << 8) | (byte) ((6 * position) + 1);
        mPeriodSettings[position].setPeriodSetting(setting, address, service);
    }

    public void setmControlMode(int mControlMode, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x1100, mControlMode);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mControlMode = mControlMode;
    }
}
