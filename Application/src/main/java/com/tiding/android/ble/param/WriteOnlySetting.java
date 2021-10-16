package com.tiding.android.ble.param;

import com.tiding.android.ble.BluetoothLeService;

public class WriteOnlySetting extends Object {
    public static void resetLifeHour(BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0400, 13);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
    }

    public static void resetAllSettings(BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0400, 18);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
    }
}
