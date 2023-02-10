package org.g2.dynamic.jdbc.loadbanlance.balancer;

import java.util.Random;

/**
 * 随机
 *
 * @author wuwenxi 2023-02-10
 */
public class RandomLoadBalancer<T> extends AbstractLoadBalance<T> {

    public RandomLoadBalancer(T[] objs) {
        super(objs);
    }

    @Override
    protected T doChoose() {
        int size = size();
        return choose(new Random(size).nextInt());
    }
}
