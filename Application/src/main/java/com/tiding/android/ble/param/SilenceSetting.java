package com.tiding.android.ble.param;

import android.support.annotation.NonNull;

import com.tiding.android.ble.BluetoothLeService;

public class SilenceSetting extends Object {
    public static final byte[] REQ_FRAME = ModbusParam.getReadMultiReqFrame(0x0120, 1);

    private int mSilence;   // 消音

    public static SilenceSetting create(@NonNull BluetoothLeService service) throws Exception {
        byte[] respFrame = service.sendToSerial(REQ_FRAME, ModbusParam.respFrameMustLen(REQ_FRAME));
        if (!ModbusParam.isValid(REQ_FRAME, respFrame)) {
            throw new Exception("invalid modbus response");
        }

        int[] respData = ModbusParam.parseRespData(respFrame);
        SilenceSetting result = new SilenceSetting();
        for (int i = 0; i < respData.length; i++) {
            switch (i) {
                case 0:
                    result.mSilence = respData[i];
                    break;
            }
        }
        return result;
    }

    public int getmSilence() {
        return mSilence;
    }

    public void setmSilence(int mSilence, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0120, mSilence);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mSilence = mSilence;
    }
}
