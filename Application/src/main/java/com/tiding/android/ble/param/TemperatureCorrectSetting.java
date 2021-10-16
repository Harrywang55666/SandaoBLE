package com.tiding.android.ble.param;

import android.support.annotation.NonNull;

import com.tiding.android.ble.BluetoothLeService;

public class TemperatureCorrectSetting extends Object {
    public static final byte[] REQ_FRAME = ModbusParam.getReadMultiReqFrame(0x0140, 2);

    private int mHouseTemperatureCorrect;   // 棚内温度校准偏差值（有正负）
    private int mFungusTemperatureCorrect;   // 菌包温度校准偏差值（有正负）

    public static TemperatureCorrectSetting create(@NonNull BluetoothLeService service) throws Exception {
        byte[] respFrame = service.sendToSerial(REQ_FRAME, ModbusParam.respFrameMustLen(REQ_FRAME));
        if (!ModbusParam.isValid(REQ_FRAME, respFrame)) {
            throw new Exception("invalid modbus response");
        }

        int[] respData = ModbusParam.parseRespData(respFrame);
        TemperatureCorrectSetting result = new TemperatureCorrectSetting();
        for (int i = 0; i < respData.length; i++) {
            switch (i) {
                case 0:
                    result.mHouseTemperatureCorrect = respData[i];
                    break;
                case 1:
                    result.mFungusTemperatureCorrect = respData[i];
                    break;
            }
        }
        return result;
    }

    public int getmHouseTemperatureCorrect() {
        return mHouseTemperatureCorrect;
    }

    public void setmHouseTemperatureCorrect(int mHouseTemperatureCorrect, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0140, mHouseTemperatureCorrect);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mHouseTemperatureCorrect = mHouseTemperatureCorrect;
    }

    public int getmFungusTemperatureCorrect() {
        return mFungusTemperatureCorrect;
    }

    public void setmFungusTemperatureCorrect(int mFungusTemperatureCorrect, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0141, mFungusTemperatureCorrect);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mFungusTemperatureCorrect = mFungusTemperatureCorrect;
    }
}
