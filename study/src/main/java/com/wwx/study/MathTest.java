package com.wwx.study;

/**
 * @author wuwenxi 2024-05-07
 */
public class MathTest {

    public static void main(String[] args) {
        System.out.println("根据体积求长，cbrt:" + Math.cbrt(8));
        System.out.println("根据角度求正旋值，sin:" + Math.sin(Math.PI / 6));
        System.out.println("根据正旋值求角度，asin:" + Math.asin(0.5));
        System.out.println("求e的指数，exp:" + Math.exp(1));
        System.out.println("求底为e的对数，log:" + Math.log(Math.E));
        System.out.println("求底为10的对数，log10:" + Math.log10(10));
        System.out.println("平方跟, sqrt:" + Math.sqrt(9));
        System.out.println(Math.ceil(10));
        // System.out.println("根据角度，toRadians:" + Math.toRadians(60));

    }
}
