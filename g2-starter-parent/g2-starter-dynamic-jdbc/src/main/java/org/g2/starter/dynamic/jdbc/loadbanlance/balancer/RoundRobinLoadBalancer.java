package org.g2.starter.dynamic.jdbc.loadbanlance.balancer;

import org.g2.starter.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 *  轮询
 *
 * @author wuwenxi 2023-02-10
 */
public class RoundRobinLoadBalancer<T> extends AbstractLoadBalance<T> {

    private static final Logger log = LoggerFactory.getLogger(RoundRobinLoadBalancer.class);

    private final CycleNodeList<T> cycleNodeList;

    public RoundRobinLoadBalancer(T[] objs) {
        super(objs);
        this.cycleNodeList = new CycleNodeList<>(objs);
    }

    @Override
    protected T doChoose() {
        return this.cycleNodeList.get();
    }

    /**
     * 循环链表
     */
    private static class CycleNodeList<T> {
        private final AtomicReferenceFieldUpdater<CycleNodeList, Node> CAS = AtomicReferenceFieldUpdater.newUpdater(CycleNodeList.class, Node.class, "current");
        private Node<T> current;

        CycleNodeList(T[] objs) {
            init(objs);
        }

        private void init(T[] objs) {
            Node<T> head = new Node<>(objs[0]);
            Node<T> current = head;
            for (int i = 1; i < objs.length; i++) {
                Node<T> temp = new Node<>(objs[i]);
                current.next = temp;
                current = temp;
            }
            current.next = head;
            this.current = head;
        }

        private T get() {
            T value = this.current.value;
            if (!check()) {
                try {
                    while (true) {
                        value = this.current.value;
                        if (CAS.compareAndSet(this, this.current, this.current.next)) {
                            break;
                        }
                        TimeUnit.MILLISECONDS.sleep(100);
                    }
                } catch (Exception e) {
                    log.error("RoundRobinLoadBalancer get Node failed, errorMsg :{}", StringUtil.exceptionString(e));
                }
            }
            return value;
        }

        private boolean check() {
            return current == current.next;
        }
    }

    private static class Node<T> {
        private Node<T> next;
        private T value;

        Node(T value) {
            this.value = value;
        }
    }
}
