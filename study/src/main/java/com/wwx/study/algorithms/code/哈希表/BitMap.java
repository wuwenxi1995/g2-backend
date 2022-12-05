package com.wwx.study.algorithms.code.哈希表;

/**
 * 位图
 * <p>
 * 1. int数据类型 -> 4字节 -> 一个字节8bit -> 一个int数据类型 = 32bit
 * 2. 利用int数组的位信息来记录数据
 * </p>
 *
 * @author wuwenxi 2022-07-26
 */
public class BitMap {

    private int[] element;

    public BitMap(int size) {
        this.element = new int[size];
    }

    /**
     * 找到第bit位置上的状态
     *
     * @param bit 位置
     * @return 状态
     */
    public int search(int bit) {
        int numIndex = bit / 32;
        int bitIndex = bit % 32;
        //
        return (element[numIndex] >> bitIndex) & 1;
    }

    /**
     * 更新第bit位上状态为1
     *
     * @param bit 位置
     */
    public void update(int bit) {
        int numIndex = bit / 32;
        int bitIndex = bit % 32;
        element[numIndex] = (element[numIndex] | (1 << bitIndex));
    }

    /**
     * 更新第bit位上状态为0
     *
     * @param bit 位置
     */
    public void remove(int bit) {
        int numIndex = bit / 32;
        int bitIndex = bit % 32;
        element[numIndex] = element[numIndex] & (~(1 << bitIndex));
    }
}
