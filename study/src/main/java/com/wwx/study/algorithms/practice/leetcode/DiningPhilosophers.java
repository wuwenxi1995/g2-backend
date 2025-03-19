package com.wwx.study.algorithms.practice.leetcode;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 哲学家进餐问题
 * <p>
 * https://leetcode.cn/problems/the-dining-philosophers/
 *
 * @author wuwenxi 2023-01-16
 */
public class DiningPhilosophers {

    private static ReentrantLock[] locks = new ReentrantLock[]{
            new ReentrantLock(),
            new ReentrantLock(),
            new ReentrantLock(),
            new ReentrantLock(),
            new ReentrantLock()
    };
    private static Semaphore limit = new Semaphore(4);

    public DiningPhilosophers() {
    }

    public void wantsToEat(int philosopher,
                           Runnable pickLeftFork,
                           Runnable pickRightFork,
                           Runnable eat,
                           Runnable putLeftFork,
                           Runnable putRightFork) throws InterruptedException {
        limit.acquire();

        //
        pickLeftFork.run();
        pickRightFork.run();
        eat.run();
        putLeftFork.run();
        putRightFork.run();
        limit.release();
    }
}
