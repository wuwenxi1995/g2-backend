package org.g2.dynamic.jdbc.loadbanlance.balancer;

import org.g2.dynamic.jdbc.loadbanlance.LoadBalance;

/**
 * @author wuwenxi 2023-02-10
 */
public abstract class AbstractLoadBalance<T> implements LoadBalance<T> {

    private T[] objs;

    AbstractLoadBalance(T[] objs) {
        this.objs = objs;
        if (objs == null || objs.length == 0) {
            throw new IllegalArgumentException("The " + this.getClass().getName() + " is not allowed to have an empty array");
        }
    }

    @Override
    public T choose() {
        return this.size() == 0 ? null : this.size() == 1 ? objs[1] : doChoose();
    }

    @Override
    public T choose(int index) {
        return index > size() ? null : objs[index];
    }

    @Override
    public int size() {
        return objs.length;
    }

    /**
     * 实际操作
     *
     * @return key
     */
    protected abstract T doChoose();
}
