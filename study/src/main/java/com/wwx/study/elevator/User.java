package com.wwx.study.elevator;

import org.springframework.util.Assert;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wuwenxi 2022-06-27
 */
public class User {

    private Condition condition;
    private int level;

    private final ReentrantLock reentrantLock;
    private final Elevator.State state;

    public User(Elevator elevator, ReentrantLock reentrantLock) {
        this.reentrantLock = reentrantLock;
        Random random = new Random();
        int num = random.nextInt(2);
        this.state = num == 0 ? Elevator.State.UP : Elevator.State.DOWN;
        do {
            this.level = random.nextInt(34) + 1;
        } while (level != elevator.getCurrentLevel());
    }

    public Elevator.State getState() {
        return state;
    }

    public int getLevel() {
        return level;
    }

    public void await() throws InterruptedException {
        this.condition = reentrantLock.newCondition();
        condition.await();
    }

    public void wakeup() {
        Assert.notNull(condition, "");
        condition.signal();
    }
}
