package com.wwx.study.elevator;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 电梯
 *
 * @author wuwenxi 2022-06-27
 */
public class Elevator implements Runnable {

    private volatile int currentLevel;
    private State state;

    private final int maxLevel;
    private final int stopLevel;
    private final Condition waitLock;

    public Elevator() {
        this.maxLevel = 34;
        this.stopLevel = 0;
        this.currentLevel = 0;
        this.state = State.WAITING;
        this.waitLock = new ReentrantLock(true).newCondition();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (state.equals(State.WAITING)) {
                    waitLock.await();
                } else if (state.equals(State.UP)) {

                } else {

                }
            } catch (InterruptedException e) {

            }
        }
    }

    public void add(User user, State state, int level) {

    }

    int getCurrentLevel() {
        return this.currentLevel;
    }

    public enum State {
        /**
         * 电梯运行状态
         */
        WAITING,
        UP,
        DOWN
    }
}
