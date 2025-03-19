package com.wwx.study.io.nio;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 多Reactor模型，由controller和work构成
 *
 * @author wuwenxi 2022-03-07
 */
public class ServerSocket {

    private volatile boolean running = false;
    /**
     * controller
     */
    private Acceptor controlPlaneAcceptor;
    private Processor controlPlaneProcessor;

    /**
     * work
     */
    private List<Acceptor> dataPlaneAcceptor;
    private List<Processor> dataPlaneProcessor;

    public void start() {
    }

    public void stop() {
    }

    public boolean isRunning() {
        return running;
    }

    /**
     * acceptor
     */
    private static class Acceptor extends AbstractServerThread {

        private final Selector selector;

        Acceptor() throws IOException {
            this.selector = Selector.open();
        }

        @Override
        public void run() {
            try {
                startupComplete();
                while (isAlive()) {
                    try {
                        selector.select(500);
                    } catch (IOException e) {
                        //
                    }
                }
            } finally {
                shutdownComplete();
            }
        }

    }

    /**
     * processor
     */
    private static class Processor extends AbstractServerThread {

        @Override
        public void run() {

        }
    }

    // handler

    private static abstract class AbstractServerThread implements Runnable {

        private AtomicBoolean alive = new AtomicBoolean(true);
        private final CountDownLatch startupLatch = new CountDownLatch(1);
        private CountDownLatch shutdownLatch = new CountDownLatch(0);

        void wakeUp() {
        }

        boolean isStartup() {
            return startupLatch.getCount() == 0;
        }

        boolean isAlive() {
            return alive.get();
        }

        void initiateShutdown() {
            if (alive.getAndSet(false)) {
                wakeUp();
            }
        }

        void awaitShutdown() throws InterruptedException {
            shutdownLatch.await();
        }

        void awaitStartup() throws InterruptedException {
            startupLatch.await();
        }

        final void startupComplete() {
            shutdownLatch = new CountDownLatch(1);
            startupLatch.countDown();
        }

        final void shutdownComplete() {
            shutdownLatch.countDown();
        }

        final void close(SocketChannel sc) {
            if (sc != null) {
                try {
                    sc.socket().close();
                    sc.close();
                } catch (IOException e) {
                    //
                }
            }
        }
    }
}
