package com.tiding.android.ble.param;

import com.tiding.android.ble.BluetoothLeService;

public class HouseTemperatureSetting extends Object {
    public static final byte[] REQ_FRAME = ModbusParam.getReadMultiReqFrame(0x0240, 10);

    private int mHouseCoolFunction;             // 0.棚内降温功能
    private int mCoolStartTemperature;          // 1.降温启动温度
    private int mCoolStopTemperature;           // 2.降温停止温度
    private int mHouseHighTemperatureWarning;   // 3.棚内高温报警温度
    private int mHouseLowTemperatureWarning;    // 4.棚内低温报警温度
    private int mHouseHeatFunction;             // 5.棚内加温功能
    private int mHeatStartTemperature;          // 6.加温启动温度
    private int mHeatStopTemperature;           // 7.加温停止温度
    private int mLinkNewFan;                    // 8.是否联动新风
    private int mLinkHumidity;                  // 9.是否联动加湿

    public static HouseTemperatureSetting create(BluetoothLeService service) throws Exception {
        byte[] respFrame = service.sendToSerial(REQ_FRAME, ModbusParam.respFrameMustLen(REQ_FRAME));
        if (!ModbusParam.isValid(REQ_FRAME, respFrame)) {
            throw new Exception("invalid modbus response");
        }

        int[] respData = ModbusParam.parseRespData(respFrame);
        HouseTemperatureSetting result = new HouseTemperatureSetting();
        for (int i = 0; i < respData.length; i++) {
            switch (i) {
                case 0:
                    result.mHouseCoolFunction = respData[i];
                    break;
                case 1:
                    result.mCoolStartTemperature = respData[i];
                    break;
                case 2:
                    result.mCoolStopTemperature = respData[i];
                    break;
                case 3:
                    result.mHouseHighTemperatureWarning = respData[i];
                    break;
                case 4:
                    result.mHouseLowTemperatureWarning = respData[i];
                    break;
                case 5:
                    result.mHouseHeatFunction = respData[i];
                    break;
                case 6:
                    result.mHeatStartTemperature = respData[i];
                    break;
                case 7:
                    result.mHeatStopTemperature = respData[i];
                    break;
                case 8:
                    result.mLinkNewFan = respData[i];
                    break;
                case 9:
                    result.mLinkHumidity = respData[i];
                    break;
            }
        }
        return result;
    }

    public int getmHouseCoolFunction() {
        return mHouseCoolFunction;
    }

    public void setmHouseCoolFunction(int mHouseCoolFunction, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0240, mHouseCoolFunction);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mHouseCoolFunction = mHouseCoolFunction;
    }

    public int getmCoolStartTemperature() {
        return mCoolStartTemperature;
    }

    public void setmCoolStartTemperature(int mCoolStartTemperature, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0241, mCoolStartTemperature);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mCoolStartTemperature = mCoolStartTemperature;
    }

    public int getmCoolStopTemperature() {
        return mCoolStopTemperature;
    }

    public void setmCoolStopTemperature(int mCoolStopTemperature, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0242, mCoolStopTemperature);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mCoolStopTemperature = mCoolStopTemperature;
    }

    public int getmHouseHighTemperatureWarning() {
        return mHouseHighTemperatureWarning;
    }

    public void setmHouseHighTemperatureWarning(int mHouseHighTemperatureWarning, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0243, mHouseHighTemperatureWarning);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mHouseHighTemperatureWarning = mHouseHighTemperatureWarning;
    }

    public int getmHouseLowTemperatureWarning() {
        return mHouseLowTemperatureWarning;
    }

    public void setmHouseLowTemperatureWarning(int mHouseLowTemperatureWarning, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0244, mHouseLowTemperatureWarning);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mHouseLowTemperatureWarning = mHouseLowTemperatureWarning;
    }

    public int getmHouseHeatFunction() {
        return mHouseHeatFunction;
    }

    public void setmHouseHeatFunction(int mHouseHeatFunction, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0245, mHouseHeatFunction);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mHouseHeatFunction = mHouseHeatFunction;
    }

    public int getmHeatStartTemperature() {
        return mHeatStartTemperature;
    }

    public void setmHeatStartTemperature(int mHeatStartTemperature, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0246, mHeatStartTemperature);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mHeatStartTemperature = mHeatStartTemperature;
    }

    public int getmHeatStopTemperature() {
        return mHeatStopTemperature;
    }

    public void setmHeatStopTemperature(int mHeatStopTemperature, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0247, mHeatStopTemperature);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mHeatStopTemperature = mHeatStopTemperature;
    }

    public int getmLinkNewFan() {
        return mLinkNewFan;
    }

    public void setmLinkNewFan(int mLinkNewFan, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0248, mLinkNewFan);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mLinkNewFan = mLinkNewFan;
    }

    public int getmLinkHumidity() {
        return mLinkHumidity;
    }

    public void setmLinkHumidity(int mLinkHumidity, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0249, mLinkHumidity);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mLinkHumidity = mLinkHumidity;
    }
}
