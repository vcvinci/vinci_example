/**
 * FileName: BitSetConvertUtils
 * Author:   HuangTaiHong
 * Date:     2019/1/30 13:59
 * Description: BitSet and byte convert to each other.
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.vcvinci.common.util;

import java.util.BitSet;

/**
 * 〈BitSet and byte convert to each other.〉
 *
 * @author HuangTaiHong
 * @create 2019/1/30
 * @since 1.0.0
 */
public class BitSetConvertUtils {
    /**
     * 〈from bit set to byte.〉
     *
     * @param bitSet
     * @return  > byte
     * @author HuangTaiHong
     * @date 2019.01.30 13:59:34
     */
    public static byte toByte(BitSet bitSet) {
        int value = 0;
        for (int i = 0; i < bitSet.length(); ++i) {
            if (bitSet.get(i)) {
                value += 1 << i;
            }
        }
        if (bitSet.length() > 7) {
            throw new IllegalArgumentException("The byte value " + value + " generated according to bit set " + bitSet + " is out of range, should be limited between [" + Byte.MIN_VALUE + "] to [" + Byte.MAX_VALUE + "]");
        }
        return (byte) value;
    }

    /**
     * 〈from byte to bit set.〉
     *
     * @param value
     * @return  > java.util.BitSet
     * @author HuangTaiHong
     * @date 2019.01.30 13:59:46
     */
    public static BitSet toBitSet(int value) {
        if (value > Byte.MAX_VALUE || value < Byte.MIN_VALUE) {
            throw new IllegalArgumentException("The value " + value + " is out of byte range, should be limited between [" + Byte.MIN_VALUE + "] to [" + Byte.MAX_VALUE + "]");
        }
        BitSet bs = new BitSet();
        int index = 0;
        while (value != 0) {
            if (value % 2 != 0) {
                bs.set(index);
            }
            ++index;
            value = (byte) (value >> 1);
        }
        return bs;
    }
}