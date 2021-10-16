package com.tiding.android.ble.param;

import com.tiding.android.ble.BluetoothLeService;
import com.tiding.android.ble.R;

public class ErrorSetting {
    public static final byte[] REQ_FRAME = ModbusParam.getReadMultiReqFrame(0x170, 1);

    public static final int[] ALL_ERROR_RESID = new int[]{
            R.string.system_time_reset,
            R.string.app_will_expire,
            R.string.app_expired,
            R.string.water_pump_error,
            R.string.change_humidity_piece,
            R.string.new_fan_error,
            R.string.house_temperature_too_high,
            R.string.house_temperature_too_low,
            R.string.fungus_temperature_too_high,
            R.string.fungus_temperature_too_low,
            R.string.house_temperature_sensor_error,
            R.string.fungus_temperature_sensor_error,
    };

    private int mFlag;

    public int[] getErrorResIDs() {
        int[] result = new int[0];
        for (int i = 0; i < 16; i++) {
            if ((mFlag & (1 << i)) != 0) {
                int newResult[] = new int[result.length + 1];
                System.arraycopy(result, 0, newResult, 0, result.length);
                newResult[newResult.length - 1] = ALL_ERROR_RESID[i];
                result = newResult;
            }
        }
        return result;
    }

    public ErrorSetting(int mFlag) {
        this.mFlag = mFlag;
    }

    public static ErrorSetting create(BluetoothLeService service) throws Exception {
        byte[] respFrame = service.sendToSerial(REQ_FRAME, ModbusParam.respFrameMustLen(REQ_FRAME));
        if (!ModbusParam.isValid(REQ_FRAME, respFrame)) {
            throw new Exception("invalid modbus response");
        }

        int[] respData = ModbusParam.parseRespData(respFrame);
        return new ErrorSetting(respData[0]);
    }

}
