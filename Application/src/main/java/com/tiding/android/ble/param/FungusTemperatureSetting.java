package com.tiding.android.ble.param;

import com.tiding.android.ble.BluetoothLeService;

public class FungusTemperatureSetting extends Object {
    public static final byte[] REQ_FRAME = ModbusParam.getReadMultiReqFrame(0x0260, 10);

    private int mFungusCoolFunction;            // 0.菌包降温功能
    private int mCoolStartTemperature;          // 1.降温启动温度
    private int mCoolStopTemperature;           // 2.降温停止温度
    private int mFungusHighTemperatureWarning;  // 3.菌包高温报警温度
    private int mFungusLowTemperatureWarning;   // 4.菌包低温报警温度
    private int mFungusHeatFunction;            // 5.菌包加温功能
    private int mHeatStartTemperature;          // 6.加温启动温度
    private int mHeatStopTemperature;           // 7.加温停止温度
    private int mLinkNewFan;                    // 8.是否联动新风
    private int mLinkHumidity;                  // 9.是否联动加湿

    public static FungusTemperatureSetting create(BluetoothLeService service) throws Exception {
        byte[] respFrame = service.sendToSerial(REQ_FRAME, ModbusParam.respFrameMustLen(REQ_FRAME));
        if (!ModbusParam.isValid(REQ_FRAME, respFrame)) {
            throw new Exception("invalid modbus response");
        }

        int[] respData = ModbusParam.parseRespData(respFrame);
        FungusTemperatureSetting result = new FungusTemperatureSetting();
        for (int i = 0; i < respData.length; i++) {
            switch (i) {
                case 0:
                    result.mFungusCoolFunction = respData[i];
                    break;
                case 1:
                    result.mCoolStartTemperature = respData[i];
                    break;
                case 2:
                    result.mCoolStopTemperature = respData[i];
                    break;
                case 3:
                    result.mFungusHighTemperatureWarning = respData[i];
                    break;
                case 4:
                    result.mFungusLowTemperatureWarning = respData[i];
                    break;
                case 5:
                    result.mFungusHeatFunction = respData[i];
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

    public int getmFungusCoolFunction() {
        return mFungusCoolFunction;
    }

    public int getmCoolStartTemperature() {
        return mCoolStartTemperature;
    }

    public int getmCoolStopTemperature() {
        return mCoolStopTemperature;
    }

    public int getmFungusHighTemperatureWarning() {
        return mFungusHighTemperatureWarning;
    }

    public int getmFungusLowTemperatureWarning() {
        return mFungusLowTemperatureWarning;
    }

    public int getmFungusHeatFunction() {
        return mFungusHeatFunction;
    }

    public int getmHeatStartTemperature() {
        return mHeatStartTemperature;
    }

    public int getmHeatStopTemperature() {
        return mHeatStopTemperature;
    }

    public int getmLinkNewFan() {
        return mLinkNewFan;
    }

    public int getmLinkHumidity() {
        return mLinkHumidity;
    }

    public void setmFungusCoolFunction(int mFungusCoolFunction, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0260, mFungusCoolFunction);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mFungusCoolFunction = mFungusCoolFunction;
    }

    public void setmCoolStartTemperature(int mCoolStartTemperature, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0261, mCoolStartTemperature);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mCoolStartTemperature = mCoolStartTemperature;
    }

    public void setmCoolStopTemperature(int mCoolStopTemperature, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0262, mCoolStopTemperature);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mCoolStopTemperature = mCoolStopTemperature;
    }

    public void setmFungusHighTemperatureWarning(int mFungusHighTemperatureWarning, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0263, mFungusHighTemperatureWarning);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mFungusHighTemperatureWarning = mFungusHighTemperatureWarning;
    }

    public void setmFungusLowTemperatureWarning(int mFungusLowTemperatureWarning, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0264, mFungusLowTemperatureWarning);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mFungusLowTemperatureWarning = mFungusLowTemperatureWarning;
    }

    public void setmFungusHeatFunction(int mFungusHeatFunction, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0265, mFungusHeatFunction);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mFungusHeatFunction = mFungusHeatFunction;
    }

    public void setmHeatStartTemperature(int mHeatStartTemperature, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0266, mHeatStartTemperature);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mHeatStartTemperature = mHeatStartTemperature;
    }

    public void setmHeatStopTemperature(int mHeatStopTemperature, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0267, mHeatStopTemperature);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mHeatStopTemperature = mHeatStopTemperature;
    }

    public void setmLinkNewFan(int mLinkNewFan, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0268, mLinkNewFan);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mLinkNewFan = mLinkNewFan;
    }

    public void setmLinkHumidity(int mLinkHumidity, BluetoothLeService service) throws Exception {
        byte[] reqFrame = ModbusParam.getWriteSingleReqFrame(0x0269, mLinkHumidity);
        byte[] respFrame = service.sendToSerial(reqFrame, 8);
        if (!ModbusParam.isValid(reqFrame, respFrame)) {
            throw new Exception("invalid modbus response");
        }
        this.mLinkHumidity = mLinkHumidity;
    }
}
