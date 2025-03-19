package org.g2.starter.core.thread.task;

import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wuwenxi 2021-08-03
 */
public abstract class TaskPoolExecutor<T> {

    /**
     * 任务执行器状态
     */
    private final AtomicReference<Status> status = new AtomicReference<>(Status.RUNNING);
    /**
     * 工作线程数量
     */
    private final AtomicInteger wct = new AtomicInteger(0);
    /**
     * 工作任务
     */
    private HashSet<Worker> workers = new HashSet<>();

    private ReentrantLock mainLock = new ReentrantLock(true);

    private int coreTaskSize;
    private int maxTaskSize;
    private int keepAliveTime;
    private TimeUnit timeUnit;
    private boolean allowCoreThreadTimeOut = false;
    private BlockingQueue<T> workingQueue;
    private ThreadFactory threadFactory;
    private RejectTaskHandler rejectTaskHandler;

    private final class Worker implements Runnable {

        T firstTask;
        final Thread thread;
        volatile AtomicInteger completedTasks = new AtomicInteger(0);

        Worker(T firstTask) {
            this.firstTask = firstTask;
            this.thread = threadFactory.newThread(this);
        }

        @Override
        public void run() {
            runWorker(this);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            @SuppressWarnings("unchecked")
            Worker worker = (Worker) o;
            return completedTasks.get() == worker.completedTasks.get() &&
                    Objects.equals(firstTask, worker.firstTask) &&
                    Objects.equals(thread, worker.thread);
        }

        @Override
        public int hashCode() {
            return Objects.hash(firstTask, thread, completedTasks);
        }
    }

    private void runWorker(Worker w) {
        Thread wt = Thread.currentThread();
        T task = w.firstTask;
        w.firstTask = null;
        boolean completedAbruptly = true;
        try {
            while (task != null || (task = getTask()) != null) {
                //
                Status rs = status.get();

                try {
                    process(task);
                } finally {
                    task = null;
                    w.completedTasks.incrementAndGet();
                }
            }
            completedAbruptly = false;
        } finally {
            processWorkerExit(w, completedAbruptly);
        }
    }

    private void processWorkerExit(Worker w, boolean completedAbruptly) {
        if (completedAbruptly) {
            decrementWorkerCount();
        }
        ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            workers.remove(w);
        } finally {
            mainLock.unlock();
        }
    }

    private T getTask() {
        boolean timeout = false;
        for (; ; ) {

            if (!status.get().checkStatus() || workingQueue.isEmpty()) {
                decrementWorkerCount();
                return null;
            }

            int wc = wct.get();
            boolean timed = allowCoreThreadTimeOut || wc > coreTaskSize;

            if ((wc > maxTaskSize || (timed && timeout))
                    && (wc > 1 || workingQueue.isEmpty())) {
                if (compareAndDecrementWorkerCount(wc)) {
                    return null;
                }
                continue;
            }

            try {
                T task = timed ? workingQueue.poll(keepAliveTime, timeUnit) : workingQueue.take();
                if (task != null) {
                    return task;
                }
                timeout = true;
            } catch (InterruptedException e) {
                timeout = false;
            }
        }
    }

    public final void add(T task) {
        if (task == null) {
            throw new NullPointerException();
        }
        if (wct.get() < coreTaskSize) {
            if (addWorker(task, true)) {
                return;
            }
        }
        // 如果任务工作器状态正常，并且能够加入到工作队列
        if (status.get().checkStatus() && workingQueue.offer(task)) {
            Status recheck = this.status.get();
            // 再次检查状态
            if (!recheck.checkStatus() && remove(task)) {
                reject(task);
            } else if (wct.get() == 0) {
                addWorker(null, false);
            }
        } else if (!addWorker(task, false)) {
            // 拒绝策略
            reject(task);
        }
    }

    private boolean addWorker(T firstTask, boolean core) {
        retry:
        for (; ; ) {
            Status rs = status.get();
            if (!rs.checkStatus()) {
                return false;
            }
            for (; ; ) {
                int c = wct.get();
                if (c >= (core ? coreTaskSize : maxTaskSize)) {
                    return false;
                }
                if (compareAndIncrementWorkerCount(c)) {
                    break retry;
                }
                Status check = this.status.get();
                if (check.compareTo(rs) != 0) {
                    continue retry;
                }
            }
        }
        boolean workerStarted = false;
        boolean workerAdded = false;
        Worker w = null;
        try {
            w = new Worker(firstTask);
            final Thread t = w.thread;
            if (t != null) {
                ReentrantLock mainLock = this.mainLock;
                mainLock.lock();
                try {
                    if (status.get().checkStatus()) {
                        // 如果线程已经启动，发生异常
                        if (t.isAlive()) {
                            throw new IllegalThreadStateException();
                        }
                        workers.add(w);
                        workerAdded = true;
                    }
                } finally {
                    mainLock.unlock();
                }
                if (workerAdded) {
                    t.start();
                    workerStarted = true;
                }
            }
        } finally {
            if (!workerStarted) {
                addWorkerFailed(w);
            }
        }
        return workerStarted;
    }

    private boolean remove(T task) {
        return workingQueue.remove(task);
    }

    private void reject(Object task) {
        rejectTaskHandler.rejectedExecution(task);
    }

    private boolean compareAndIncrementWorkerCount(int workCount) {
        return wct.compareAndSet(workCount, workCount + 1);
    }

    private boolean compareAndDecrementWorkerCount(int expect) {
        return wct.compareAndSet(expect, expect - 1);
    }

    private void decrementWorkerCount() {
        for (; ; ) {
            if (compareAndDecrementWorkerCount(wct.get())) {
                break;
            }
        }
    }

    private void addWorkerFailed(Worker w) {
        ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            if (w != null) {
                workers.remove(w);
            }
            decrementWorkerCount();
        } finally {
            mainLock.unlock();
        }
    }

    public TaskPoolExecutor(int coreTaskSize, int maxTaskSize, int keepAliveTime, TimeUnit timeUnit, BlockingQueue<T> workingQueue, ThreadFactory threadFactory, RejectTaskHandler rejectTaskHandler) {
        this.coreTaskSize = coreTaskSize;
        this.maxTaskSize = maxTaskSize;
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.workingQueue = workingQueue;
        this.threadFactory = threadFactory;
        this.rejectTaskHandler = rejectTaskHandler;
    }

    public void setCoreTaskSize(int coreTaskSize) {
        this.coreTaskSize = coreTaskSize;
    }

    public void setMaxTaskSize(int maxTaskSize) {
        this.maxTaskSize = maxTaskSize;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    }

    public void setWorkingQueue(BlockingQueue<T> workingQueue) {
        this.workingQueue = workingQueue;
    }

    public void setThreadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    public void setRejectTaskHandler(RejectTaskHandler rejectTaskHandler) {
        this.rejectTaskHandler = rejectTaskHandler;
    }

    private enum Status {
        /**
         * 任务器状态
         */
        RUNNING,
        SHUTDOWN,
        STOP;

        public boolean checkStatus() {
            return this.equals(RUNNING);
        }
    }

    public interface RejectTaskHandler {
        /**
         * 任务拒绝
         *
         * @param task 任务
         */
        void rejectedExecution(Object task);
    }

    protected abstract void process(T data);

}
