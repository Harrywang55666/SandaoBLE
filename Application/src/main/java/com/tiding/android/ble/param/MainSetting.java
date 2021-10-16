package com.tiding.android.ble.param;

import android.support.annotation.NonNull;

import com.tiding.android.ble.BluetoothLeService;

import java.io.Serializable;

/**
 * 主界面参数状态
 */
public class MainSetting extends Object implements Serializable {
    public static final byte[] REQ_FRAME = ModbusParam.getReadMultiReqFrame(0x0200, 20);

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private int mWeekDay;
    private int mHouseCoolRunStatus;        // 棚内温控：降温运行状态
    private int mHouseHeatRunStatus;        // 棚内温控：加温运行状态
    private int mFungusCoolRunStatus;       // 菌包温控：降温运行状态
    private int mFungusHeatRunStatus;       // 菌包温控：加温运行状态
    private int mHumidityControlMode;       // 加湿控制模式
    private int mHumidityRunStatus;         // 加湿运行状态
    private int mNewFanControlMode;         // 新风控制模式
    private int mNewFanRunStatus;           // 新风运行状态
    private int mHouseTemperature;          // 棚内温度
    private int mFungusTemperature;         // 菌包温度
    private int mHouseTemperatureStatus;    // 棚内温度状态
    private int mFungusTemperatureStatus;   // 菌包温度状态
    private int mHumidityPieceLifeHour;     // 加湿片剩余寿命(小时)
    private int mErrorStatus;               // 故障标志(是否有故障)

    public static MainSetting create(@NonNull BluetoothLeService service) throws Exception {
        byte[] respFrame = service.sendToSerial(REQ_FRAME, ModbusParam.respFrameMustLen(REQ_FRAME));
        if (!ModbusParam.isValid(REQ_FRAME, respFrame)) {
            throw new Exception("invalid modbus response");
        }

        int[] respData = ModbusParam.parseRespData(respFrame);
        MainSetting result = new MainSetting();
        for (int i = 0; i < respData.length; i++) {
            switch (i) {
                case 0:
                    result.mYear = respData[i];
                    break;
                case 1:
                    result.mMonth = respData[i];
                    break;
                case 2:
                    result.mDay = respData[i];
                    break;
                case 3:
                    result.mHour = respData[i];
                    break;
                case 4:
                    result.mMinute = respData[i];
                    break;
                case 5:
                    result.mWeekDay = respData[i];
                    break;
                case 6:
                    result.mHouseCoolRunStatus = respData[i];
                    break;
                case 7:
                    result.mHouseHeatRunStatus = respData[i];
                    break;
                case 8:
                    result.mFungusCoolRunStatus = respData[i];
                    break;
                case 9:
                    result.mFungusHeatRunStatus = respData[i];
                    break;
                case 10:
                    result.mHumidityControlMode = respData[i];
                    break;
                case 11:
                    result.mHumidityRunStatus = respData[i];
                    break;
                case 12:
                    result.mNewFanControlMode = respData[i];
                    break;
                case 13:
                    result.mNewFanRunStatus = respData[i];
                    break;
                case 14:
                    result.mHouseTemperature = respData[i];
                    break;
                case 15:
                    result.mFungusTemperature = respData[i];
                    break;
                case 16:
                    result.mHouseTemperatureStatus = respData[i];
                    break;
                case 17:
                    result.mFungusTemperatureStatus = respData[i];
                    break;
                case 18:
                    result.mHumidityPieceLifeHour = respData[i];
                    break;
                case 19:
                    result.mErrorStatus = respData[i];
                    break;
            }
        }
        return result;
    }

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

    public int getmWeekDay() {
        return mWeekDay;
    }

    public int getmHouseCoolRunStatus() {
        return mHouseCoolRunStatus;
    }

    public int getmHouseHeatRunStatus() {
        return mHouseHeatRunStatus;
    }

    public int getmFungusCoolRunStatus() {
        return mFungusCoolRunStatus;
    }

    public int getmFungusHeatRunStatus() {
        return mFungusHeatRunStatus;
    }

    public int getmHumidityControlMode() {
        return mHumidityControlMode;
    }

    public int getmHumidityRunStatus() {
        return mHumidityRunStatus;
    }

    public int getmNewFanControlMode() {
        return mNewFanControlMode;
    }

    public int getmNewFanRunStatus() {
        return mNewFanRunStatus;
    }

    public int getmHouseTemperature() {
        return mHouseTemperature;
    }

    public int getmFungusTemperature() {
        return mFungusTemperature;
    }

    public int getmHouseTemperatureStatus() {
        return mHouseTemperatureStatus;
    }

    public int getmFungusTemperatureStatus() {
        return mFungusTemperatureStatus;
    }

    public int getmHumidityPieceLifeHour() {
        return mHumidityPieceLifeHour;
    }

    public int getmErrorStatus() {
        return mErrorStatus;
    }
}
