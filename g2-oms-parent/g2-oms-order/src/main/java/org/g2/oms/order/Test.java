package org.g2.oms.order;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-01
 */
public class Test {

    private String source = "0123456789";

    public static void main(String[] args) {

        // 未扩容前通过hash计算数组下标
        System.out.println(7 & 1);
        System.out.println(7 & 9);
        System.out.println(7 & 17);

        // 扩容 计算哪些在hash计算后下标会改变的值
        System.out.println(8 & 1);
        System.out.println(8 & 9);
        System.out.println(8 & 17);

        // 扩容后的地址
        System.out.println(15 & 1);
        System.out.println(15 & 9);
        System.out.println(15 & 17);

        Random random = new Random(47);
        Map<Integer, Object> map = new HashMap<>(16);

        for (int i = 0; i < 14; i++) {
            map.put(random.nextInt(100), UUID.randomUUID());
        }
        System.out.println(map.size());
    }

    @org.junit.Test
    public void test() {
        System.out.println("junit test");
        char[] chars = source.toCharArray();

        // 随机生成验证码
        String random = RandomStringUtils.random(6, 0, chars.length - 1, false, true, chars);
        System.out.println(random);
    }
}
