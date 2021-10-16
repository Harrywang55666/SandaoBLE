package com.tiding.android.ble.param;

import com.tiding.android.ble.BluetoothLeService;

public class TimeSetting extends Object {
    public static String DayString(int weekDay) {
        return new String[]{
                "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六",
        }[weekDay];
    }

    public static final byte[] REQ_FRAME = ModbusParam.getReadMultiReqFrame(0x0290, 5);

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    public int getmYear() {
        return mYear;
    }

    public int getmMonth() {
        return mMonth;
    }

    public int getmDay() {
        return mDay;
    }

    public int getmHour() {
        return mHour;
    }

    public int getmMinute() {
        return mMinute;
    }

    public TimeSetting(int mYear, int mMonth, int mDay, int mHour, int mMinute) {
        this.mYear = mYear;
        this.mMonth = mMonth;
        this.mDay = mDay;
        this.mHour = mHour;
        this.mMinute = mMinute;
    }

    public static void SetTimeSetting(TimeSetting setting, BluetoothLeService service) throws Exception {
        byte[] reqData = new byte[]{0x01, 0x10, 0x03, 0x00, 0x00, 0x05, 0x0A,
                (byte) (setting.mYear >> 8), (byte) setting.mYear,
                (byte) (setting.mMonth >> 8), (byte) setting.mMonth,
                (byte) (setting.mDay >> 8), (byte) setting.mDay,
                (byte) (setting.mHour >> 8), (byte) setting.mHour,
                (byte) (setting.mMinute >> 8), (byte) setting.mMinute,
        };
        byte[] crc = ModbusParam.calculateCRC(reqData);
        byte[] reqFrame = new byte[reqData.length + crc.length];
        System.arraycopy(reqData, 0, reqFrame, 0, reqData.length);
        System.arraycopy(crc, 0, reqFrame, reqData.length, crc.length);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
    }

    public static TimeSetting create(BluetoothLeService service) throws Exception {
        byte[] respFrame = service.sendToSerial(REQ_FRAME, ModbusParam.respFrameMustLen(REQ_FRAME));
        if (!ModbusParam.isValid(REQ_FRAME, respFrame)) {
            throw new Exception("invalid modbus response");
        }

        int[] respData = ModbusParam.parseRespData(respFrame);
        int mYear = 0, mMonth = 0, mDay = 0, mHour = 0, mMinute = 0;
        for (int i = 0; i < respData.length; i++) {
            switch (i) {
                case 0:
                    mYear = respData[i];
                    break;
                case 1:
                    mMonth = respData[i];
                    break;
                case 2:
                    mDay = respData[i];
                    break;
                case 3:
                    mHour = respData[i];
                    break;
                case 4:
                    mMinute = respData[i];
                    break;
            }
        }

        return new TimeSetting(mYear, mMonth, mDay, mHour, mMinute);
    }
}
