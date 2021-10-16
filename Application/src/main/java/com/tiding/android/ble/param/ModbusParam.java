package com.tiding.android.ble.param;

import android.support.annotation.NonNull;

import net.wimpi.modbus.util.ModbusUtil;

import java.util.Arrays;

public class ModbusParam extends Object {
    /**
     * 计算
     *
     * @param reqFrame
     * @return
     */
    public static int respFrameMustLen(@NonNull byte[]reqFrame) {
        return 2 * (reqFrame[4] << 8 | reqFrame[5]) + 5;
    }

    /**
     * 计算CRC，封装了ModbusUtil.calculateCRC方法
     *
     * @param data
     * @return
     */
    public static byte[] calculateCRC(byte[] data) {
        int[] originCRC = ModbusUtil.calculateCRC(data, 0, data.length);
        byte[] resultCRC = new byte[originCRC.length];
        for (int i = 0; i < originCRC.length; i++) {
            resultCRC[i] = (byte) originCRC[i];
        }
        return resultCRC;
    }

    /**
     * TODO
     * @param address
     * @param length
     * @return
     */
    public static byte[] getReadMultiReqFrame(int address, int length) {
        byte[] reqData = new byte[]{0x01, 0x03, (byte) (address >> 8), (byte) address, (byte) (length >> 8), (byte) length};
        byte[] crc = ModbusParam.calculateCRC(reqData);
        byte[] reqFrame = new byte[reqData.length + crc.length];
        System.arraycopy(reqData, 0, reqFrame, 0, reqData.length);
        System.arraycopy(crc, 0, reqFrame, reqData.length, crc.length);
        return reqFrame;
    }

    /**
     * TODO
     * @param address
     * @param value
     * @return
     */
    public static byte[] getWriteSingleReqFrame(int address, int value) {
        byte[] reqData = new byte[]{0x01, 0x10, (byte) (address >> 8), (byte) address, 0x00, 0x01, 0x02, (byte) (value >> 8), (byte) value};
        byte[] crc = ModbusParam.calculateCRC(reqData);
        byte[] reqFrame = new byte[reqData.length + crc.length];
        System.arraycopy(reqData, 0, reqFrame, 0, reqData.length);
        System.arraycopy(crc, 0, reqFrame, reqData.length, crc.length);
        return reqFrame;
    }

    /**
     * 校验得到的响应数据帧是否合法，其中包括头部校验以及CRC校验
     *
     * @param reqFrame
     * @param respFrame
     * @return
     */
    public static boolean isValid(@NonNull byte[] reqFrame, @NonNull byte[] respFrame) {
        byte reqSlaveId = reqFrame[0];
        byte reqFuncCode = reqFrame[1];

        byte respSlaveId = respFrame[0];
        byte respFuncCode = respFrame[1];

        // 校验头
        if (reqSlaveId != respSlaveId || reqFuncCode != respFuncCode) {
            return false;
        }

        // 校验CRC
        byte[] respData = new byte[respFrame.length - 2];
        byte[] crcData = new byte[2];
        System.arraycopy(respFrame, 0, respData, 0, respData.length);
        System.arraycopy(respFrame, respData.length, crcData, 0, 2);
        byte[] validCRC = calculateCRC(respData);
        return Arrays.equals(validCRC, crcData);
    }

    public static int[] parseRespData(byte[] respFrame) throws Exception {
        int dataLen = respFrame.length - 5;  // modbus头部3字节，尾部2字节
        if (dataLen < 0 || dataLen % 2 != 0) {
            throw new Exception("invalid input response frame");
        }
        byte[] bytesRespData = new byte[dataLen];
        int[] resultRespData = new int[dataLen / 2];
        System.arraycopy(respFrame, 3, bytesRespData, 0, bytesRespData.length);
        for (int i = 0; i < resultRespData.length; i++) {
            int high = ((int) bytesRespData[2 * i] & 0xFF) << 8;
            int low = (int) bytesRespData[2 * i + 1] & 0xFF;
            resultRespData[i] = high | low;
        }

        return resultRespData;
    }
}
