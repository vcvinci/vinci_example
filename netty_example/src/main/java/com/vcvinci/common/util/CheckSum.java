package com.vcvinci.common.util;

import org.apache.commons.io.EndianUtils;
import org.apache.commons.lang3.RandomUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * @author yupeng.sun@ucarinc.com
 * @version 1.0 创建时间：2017-12-15 下午5:23:27
 * @Description:
 */
public class CheckSum {

    /**
     * CRC16校验通过的固定值
     */
    public static final short OK16;

    /**
     * CRC8校验通过的固定值
     */
    public static final byte OK8;

    public static final int OK32;

    static {
        byte[] array = RandomUtils.nextBytes(10);
        ByteBuffer readMode = ByteBuffer.wrap(array, 0, array.length);
        ByteBuffer writeMode = readMode.duplicate();

        writeMode.position(writeMode.limit() - 1);
        readMode.limit(writeMode.position());

        writeMode.put(CheckSum.crc8(readMode));
        readMode.limit(writeMode.position());

        OK8 = CheckSum.crc8(readMode);

        writeMode.position(writeMode.limit() - 2);
        readMode.limit(writeMode.position());

        CheckSum.putCRC16(writeMode, CheckSum.crc16(readMode));
        readMode.limit(writeMode.position());

        OK16 = CheckSum.crc16(readMode);

        writeMode.position(writeMode.limit() - 4);
        readMode.limit(writeMode.position());

        CheckSum.putCRC32(writeMode, CheckSum.crc32(readMode));
        readMode.limit(writeMode.position());

        OK32 = CheckSum.crc32(readMode);
    }

    public static int crc32(ByteBuffer byteBuffer) {
        // jdk的实现已经做了 预设全1 + 结果取反
        CRC32 checksum = new CRC32();
        checksum.update(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining());
        return (int) checksum.getValue();
    }

    /**
     * 预设全1 + 结果取反，解决数据头尾增删0导致的问题
     * 
     * @see <a href=
     *      "https://en.wikipedia.org/wiki/Computation_of_cyclic_redundancy_checks#CRC_variants">CRC_variants</a>
     * @param byteBuffer
     * @param checksum
     * @return
     */
    private static long getChecksum(ByteBuffer byteBuffer, Checksum checksum) {
        checksum.update(0xFF);
        checksum.update(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining());
        return checksum.getValue() ^ (~0L);
    }

    public static byte crc8(ByteBuffer buffer) {
        return (byte) (getChecksum(buffer, new CRC8()));
    }

    public static short crc16(ByteBuffer buffer) {
        return (short) (getChecksum(buffer, new CRC16()));
    }

    public static void putCRC16(ByteBuffer buffer, short crc16) {
        buffer.putShort(ByteOrder.LITTLE_ENDIAN == buffer.order() ? crc16 : EndianUtils.swapShort(crc16));
    }

    public static void putCRC32(ByteBuffer buffer, int crc32) {
        buffer.putInt(ByteOrder.LITTLE_ENDIAN == buffer.order() ? crc32 : EndianUtils.swapInteger(crc32));
    }
}

/**
 * @author The OpenPilot Team, http://www.openpilot.org Copyright (C) 2011.
 * @author Tau Labs, http://taulabs.org Copyright (C) 2012-2013.
 * @brief functions to calculate CRC8 checksums for usage in the UAVTalk-Protocol
 */
class CRC8 implements Checksum {

    /** CRC lookup table - values from PYCRC **/
    private final static byte[] CRC8_TABLE = { (byte) 0x00, (byte) 0x07, (byte) 0x0e, (byte) 0x09, (byte) 0x1c,
            (byte) 0x1b, (byte) 0x12, (byte) 0x15, (byte) 0x38, (byte) 0x3f, (byte) 0x36, (byte) 0x31, (byte) 0x24,
            (byte) 0x23, (byte) 0x2a, (byte) 0x2d, (byte) 0x70, (byte) 0x77, (byte) 0x7e, (byte) 0x79, (byte) 0x6c,
            (byte) 0x6b, (byte) 0x62, (byte) 0x65, (byte) 0x48, (byte) 0x4f, (byte) 0x46, (byte) 0x41, (byte) 0x54,
            (byte) 0x53, (byte) 0x5a, (byte) 0x5d, (byte) 0xe0, (byte) 0xe7, (byte) 0xee, (byte) 0xe9, (byte) 0xfc,
            (byte) 0xfb, (byte) 0xf2, (byte) 0xf5, (byte) 0xd8, (byte) 0xdf, (byte) 0xd6, (byte) 0xd1, (byte) 0xc4,
            (byte) 0xc3, (byte) 0xca, (byte) 0xcd, (byte) 0x90, (byte) 0x97, (byte) 0x9e, (byte) 0x99, (byte) 0x8c,
            (byte) 0x8b, (byte) 0x82, (byte) 0x85, (byte) 0xa8, (byte) 0xaf, (byte) 0xa6, (byte) 0xa1, (byte) 0xb4,
            (byte) 0xb3, (byte) 0xba, (byte) 0xbd, (byte) 0xc7, (byte) 0xc0, (byte) 0xc9, (byte) 0xce, (byte) 0xdb,
            (byte) 0xdc, (byte) 0xd5, (byte) 0xd2, (byte) 0xff, (byte) 0xf8, (byte) 0xf1, (byte) 0xf6, (byte) 0xe3,
            (byte) 0xe4, (byte) 0xed, (byte) 0xea, (byte) 0xb7, (byte) 0xb0, (byte) 0xb9, (byte) 0xbe, (byte) 0xab,
            (byte) 0xac, (byte) 0xa5, (byte) 0xa2, (byte) 0x8f, (byte) 0x88, (byte) 0x81, (byte) 0x86, (byte) 0x93,
            (byte) 0x94, (byte) 0x9d, (byte) 0x9a, (byte) 0x27, (byte) 0x20, (byte) 0x29, (byte) 0x2e, (byte) 0x3b,
            (byte) 0x3c, (byte) 0x35, (byte) 0x32, (byte) 0x1f, (byte) 0x18, (byte) 0x11, (byte) 0x16, (byte) 0x03,
            (byte) 0x04, (byte) 0x0d, (byte) 0x0a, (byte) 0x57, (byte) 0x50, (byte) 0x59, (byte) 0x5e, (byte) 0x4b,
            (byte) 0x4c, (byte) 0x45, (byte) 0x42, (byte) 0x6f, (byte) 0x68, (byte) 0x61, (byte) 0x66, (byte) 0x73,
            (byte) 0x74, (byte) 0x7d, (byte) 0x7a, (byte) 0x89, (byte) 0x8e, (byte) 0x87, (byte) 0x80, (byte) 0x95,
            (byte) 0x92, (byte) 0x9b, (byte) 0x9c, (byte) 0xb1, (byte) 0xb6, (byte) 0xbf, (byte) 0xb8, (byte) 0xad,
            (byte) 0xaa, (byte) 0xa3, (byte) 0xa4, (byte) 0xf9, (byte) 0xfe, (byte) 0xf7, (byte) 0xf0, (byte) 0xe5,
            (byte) 0xe2, (byte) 0xeb, (byte) 0xec, (byte) 0xc1, (byte) 0xc6, (byte) 0xcf, (byte) 0xc8, (byte) 0xdd,
            (byte) 0xda, (byte) 0xd3, (byte) 0xd4, (byte) 0x69, (byte) 0x6e, (byte) 0x67, (byte) 0x60, (byte) 0x75,
            (byte) 0x72, (byte) 0x7b, (byte) 0x7c, (byte) 0x51, (byte) 0x56, (byte) 0x5f, (byte) 0x58, (byte) 0x4d,
            (byte) 0x4a, (byte) 0x43, (byte) 0x44, (byte) 0x19, (byte) 0x1e, (byte) 0x17, (byte) 0x10, (byte) 0x05,
            (byte) 0x02, (byte) 0x0b, (byte) 0x0c, (byte) 0x21, (byte) 0x26, (byte) 0x2f, (byte) 0x28, (byte) 0x3d,
            (byte) 0x3a, (byte) 0x33, (byte) 0x34, (byte) 0x4e, (byte) 0x49, (byte) 0x40, (byte) 0x47, (byte) 0x52,
            (byte) 0x55, (byte) 0x5c, (byte) 0x5b, (byte) 0x76, (byte) 0x71, (byte) 0x78, (byte) 0x7f, (byte) 0x6a,
            (byte) 0x6d, (byte) 0x64, (byte) 0x63, (byte) 0x3e, (byte) 0x39, (byte) 0x30, (byte) 0x37, (byte) 0x22,
            (byte) 0x25, (byte) 0x2c, (byte) 0x2b, (byte) 0x06, (byte) 0x01, (byte) 0x08, (byte) 0x0f, (byte) 0x1a,
            (byte) 0x1d, (byte) 0x14, (byte) 0x13, (byte) 0xae, (byte) 0xa9, (byte) 0xa0, (byte) 0xa7, (byte) 0xb2,
            (byte) 0xb5, (byte) 0xbc, (byte) 0xbb, (byte) 0x96, (byte) 0x91, (byte) 0x98, (byte) 0x9f, (byte) 0x8a,
            (byte) 0x8d, (byte) 0x84, (byte) 0x83, (byte) 0xde, (byte) 0xd9, (byte) 0xd0, (byte) 0xd7, (byte) 0xc2,
            (byte) 0xc5, (byte) 0xcc, (byte) 0xcb, (byte) 0xe6, (byte) 0xe1, (byte) 0xe8, (byte) 0xef, (byte) 0xfa,
            (byte) 0xfd, (byte) 0xf4, (byte) 0xf3 };

    private byte crc = 0;

    @Override
    public void update(int b) {
        this.crc = CRC8_TABLE[(this.crc ^ (byte) b) & 0xFF];
    }

    @Override
    public void update(byte[] b, int off, int len) {
        for (int i = off; i < off + len; i++) {
            this.crc = CRC8_TABLE[(this.crc ^ b[i]) & 0xFF];
        }
    }

    @Override
    public long getValue() {
        return this.crc;
    }

    @Override
    public void reset() {
        this.crc = 0;
    }
}

/**
 * CRC-16/IBM
 */
class CRC16 implements Checksum {

    private static final int[] TABLE = { 0x0000, 0xC0C1, 0xC181, 0x0140, 0xC301, 0x03C0, 0x0280, 0xC241, 0xC601, 0x06C0,
            0x0780, 0xC741, 0x0500, 0xC5C1, 0xC481, 0x0440, 0xCC01, 0x0CC0, 0x0D80, 0xCD41, 0x0F00, 0xCFC1, 0xCE81,
            0x0E40, 0x0A00, 0xCAC1, 0xCB81, 0x0B40, 0xC901, 0x09C0, 0x0880, 0xC841, 0xD801, 0x18C0, 0x1980, 0xD941,
            0x1B00, 0xDBC1, 0xDA81, 0x1A40, 0x1E00, 0xDEC1, 0xDF81, 0x1F40, 0xDD01, 0x1DC0, 0x1C80, 0xDC41, 0x1400,
            0xD4C1, 0xD581, 0x1540, 0xD701, 0x17C0, 0x1680, 0xD641, 0xD201, 0x12C0, 0x1380, 0xD341, 0x1100, 0xD1C1,
            0xD081, 0x1040, 0xF001, 0x30C0, 0x3180, 0xF141, 0x3300, 0xF3C1, 0xF281, 0x3240, 0x3600, 0xF6C1, 0xF781,
            0x3740, 0xF501, 0x35C0, 0x3480, 0xF441, 0x3C00, 0xFCC1, 0xFD81, 0x3D40, 0xFF01, 0x3FC0, 0x3E80, 0xFE41,
            0xFA01, 0x3AC0, 0x3B80, 0xFB41, 0x3900, 0xF9C1, 0xF881, 0x3840, 0x2800, 0xE8C1, 0xE981, 0x2940, 0xEB01,
            0x2BC0, 0x2A80, 0xEA41, 0xEE01, 0x2EC0, 0x2F80, 0xEF41, 0x2D00, 0xEDC1, 0xEC81, 0x2C40, 0xE401, 0x24C0,
            0x2580, 0xE541, 0x2700, 0xE7C1, 0xE681, 0x2640, 0x2200, 0xE2C1, 0xE381, 0x2340, 0xE101, 0x21C0, 0x2080,
            0xE041, 0xA001, 0x60C0, 0x6180, 0xA141, 0x6300, 0xA3C1, 0xA281, 0x6240, 0x6600, 0xA6C1, 0xA781, 0x6740,
            0xA501, 0x65C0, 0x6480, 0xA441, 0x6C00, 0xACC1, 0xAD81, 0x6D40, 0xAF01, 0x6FC0, 0x6E80, 0xAE41, 0xAA01,
            0x6AC0, 0x6B80, 0xAB41, 0x6900, 0xA9C1, 0xA881, 0x6840, 0x7800, 0xB8C1, 0xB981, 0x7940, 0xBB01, 0x7BC0,
            0x7A80, 0xBA41, 0xBE01, 0x7EC0, 0x7F80, 0xBF41, 0x7D00, 0xBDC1, 0xBC81, 0x7C40, 0xB401, 0x74C0, 0x7580,
            0xB541, 0x7700, 0xB7C1, 0xB681, 0x7640, 0x7200, 0xB2C1, 0xB381, 0x7340, 0xB101, 0x71C0, 0x7080, 0xB041,
            0x5000, 0x90C1, 0x9181, 0x5140, 0x9301, 0x53C0, 0x5280, 0x9241, 0x9601, 0x56C0, 0x5780, 0x9741, 0x5500,
            0x95C1, 0x9481, 0x5440, 0x9C01, 0x5CC0, 0x5D80, 0x9D41, 0x5F00, 0x9FC1, 0x9E81, 0x5E40, 0x5A00, 0x9AC1,
            0x9B81, 0x5B40, 0x9901, 0x59C0, 0x5880, 0x9841, 0x8801, 0x48C0, 0x4980, 0x8941, 0x4B00, 0x8BC1, 0x8A81,
            0x4A40, 0x4E00, 0x8EC1, 0x8F81, 0x4F40, 0x8D01, 0x4DC0, 0x4C80, 0x8C41, 0x4400, 0x84C1, 0x8581, 0x4540,
            0x8701, 0x47C0, 0x4680, 0x8641, 0x8201, 0x42C0, 0x4380, 0x8341, 0x4100, 0x81C1, 0x8081, 0x4040, };

    private int crc = 0x0000;

    @Override
    public void update(int b) {
        this.crc = (this.crc >>> 8) ^ TABLE[(this.crc ^ b) & 0xFF];
    }

    @Override
    public void update(byte[] b, int off, int len) {
        for (int i = off; i < off + len; i++) {
            crc = (this.crc >>> 8) ^ TABLE[(this.crc ^ b[i]) & 0xFF];
        }
    }

    @Override
    public long getValue() {
        return this.crc;
    }

    @Override
    public void reset() {
        this.crc = 0;
    }
}