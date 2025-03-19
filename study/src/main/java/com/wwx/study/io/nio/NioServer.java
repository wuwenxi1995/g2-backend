package com.wwx.study.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wuwenxi 2022-03-03
 */
public class NioServer {

    /**
     * 单reactor模型
     */
    private static class Reactor {

        private final Selector selector;
        private final Thread reactorServer;
        private final ServerSocketChannel serverSocketChannel;
        private final ExecutorService executorService;

        private volatile boolean running = false;
        private final AtomicInteger count = new AtomicInteger(0);

        Reactor(String hostname, int port) throws IOException {
            this.selector = Selector.open();
            this.serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(hostname, port), 1024);
            serverSocketChannel.configureBlocking(false);
            // 注册selector
            SelectionKey sc = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            // 创建启动线程
            this.reactorServer = Executors.defaultThreadFactory().newThread(this::run);
            // 创建线程池
            this.executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                    Runtime.getRuntime().availableProcessors(), 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(99999),
                    (r) -> new Thread(r, "NIO-" + count.getAndIncrement()), new ThreadPoolExecutor.AbortPolicy());
            // 每一个连接都有一个SelectionKey，注册acceptor来处理新连接
            sc.attach(new Acceptor(serverSocketChannel, selector, executorService));
        }

        private void run() {
            while (isRunning()) {
                try {
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    for (SelectionKey selectionKey : selectionKeys) {
                        // 分发事件
                        Runnable runnable = (Runnable) selectionKey.attachment();
                        if (runnable != null) {
                            // 一定为acceptor或handler
                            runnable.run();
                        }
                    }
                } catch (IOException e) {
                    //
                }
            }
        }

        boolean isRunning() {
            return running;
        }

        public void start() {
            if (isRunning()) {
                return;
            }
            synchronized (this) {
                if (!reactorServer.isAlive()) {
                    reactorServer.start();
                    this.running = true;
                }
            }
        }

        public void stop() throws IOException {
            synchronized (this) {
                if (!reactorServer.isInterrupted()) {
                    reactorServer.interrupt();
                }
                if (selector.isOpen()) {
                    selector.close();
                }
                if (serverSocketChannel.isOpen()) {
                    serverSocketChannel.close();
                }
                if (!executorService.isShutdown()) {
                    executorService.shutdown();
                }
            }
        }

        /**
         * 处理客户端新连接，并分发到处理链上
         */
        private static class Acceptor implements Runnable {

            private final ServerSocketChannel serverSocketChannel;
            private final Selector selector;
            private final ExecutorService executorService;

            Acceptor(ServerSocketChannel serverSocketChannel, Selector selector, ExecutorService executorService) {
                this.serverSocketChannel = serverSocketChannel;
                this.selector = selector;
                this.executorService = executorService;
            }

            @Override
            public void run() {
                try {
                    SocketChannel sc = serverSocketChannel.accept();
                    if (sc != null) {
                        sc.configureBlocking(false);
                        // 注册一个读写连接
                        SelectionKey s = sc.register(selector, SelectionKey.OP_READ);
                        // 单线程
                        // s.attach(new Handler(s));
                        // 多线程
                        s.attach(new MultiHandler(s, executorService));
                    }
                } catch (IOException e) {
                    //
                }
            }
        }

        /**
         * 单线程
         */
        private static class Handler implements Runnable {
            static final int READING = 0, WRITING = 1;

            private final SelectionKey sk;
            private int state;

            Handler(SelectionKey selectionKey) {
                this.sk = selectionKey;
                this.state = READING;
            }

            @Override
            public void run() {
                if (state == READING) {
                    read();
                } else if (state == WRITING) {
                    write();
                }
            }

            private void read() {
                // do something;
                sk.interestOps(SelectionKey.OP_WRITE);
                this.state = WRITING;
            }

            private void write() {
                // do something;
                sk.interestOps(SelectionKey.OP_READ);
                this.state = READING;
            }
        }

        private static class MultiHandler implements Runnable {

            static final int READING = 0, WRITING = 1;
            private int state;

            private final SelectionKey sk;
            private final ExecutorService executorService;

            MultiHandler(SelectionKey selectionKey, ExecutorService executorService) {
                this.sk = selectionKey;
                this.state = READING;
                this.executorService = executorService;
            }

            @Override
            public void run() {
                if (state == READING) {
                    read();
                } else if (state == WRITING) {
                    write();
                }
            }

            private void read() {
                // do something;
                // executorService.submit(() -> { });
                sk.interestOps(SelectionKey.OP_WRITE);
                this.state = WRITING;
            }

            private void write() {
                // do something;
                // executorService.submit(() -> { });
                sk.interestOps(SelectionKey.OP_READ);
                this.state = READING;
            }
        }
    }

    /**
     * 多reactor 模型
     */
    private static class MultiReactor {

        private final ServerSocketChannel serverSocketChannel;
        private final Worker[] workers;
        private final AtomicInteger count = new AtomicInteger(0);
        private final int workerCount;
        private final Thread server;
        private final Selector selector;

        private volatile boolean running = false;

        MultiReactor(String hostname, int port) throws IOException {
            this.selector = Selector.open();
            this.serverSocketChannel = ServerSocketChannel.open();
            this.serverSocketChannel.bind(new InetSocketAddress(hostname, port), 1024);
            this.serverSocketChannel.configureBlocking(false);
            // 注册到Selector上
            this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
            // reactor数量
            this.workerCount = Runtime.getRuntime().availableProcessors();
            // 初始化工作数组
            this.workers = new Worker[workerCount];
            // 初始化服务线程
            this.server = Executors.defaultThreadFactory().newThread(this::run);
        }

        private void run() {
            while (isRunning()) {
                try {
                    SocketChannel sc = serverSocketChannel.accept();
                    Worker worker;
                    // 轮询worker工作节点
                    for (; ; ) {
                        int expect = count.get(), update;
                        worker = workers[expect];
                        update = (update = expect + 1) == workerCount ? 0 : update;
                        if (count.compareAndSet(expect, update)) {
                            break;
                        }
                    }
                    // 处理新连接
                    sc.socket().setKeepAlive(true);
                    sc.socket().setTcpNoDelay(true);
                    worker.register(sc);
                } catch (IOException e) {
                    //
                }
            }
        }

        boolean isRunning() {
            return running;
        }

        public void start() throws IOException {
            if (isRunning()) {
                return;
            }
            synchronized (this) {
                this.running = true;
                if (!server.isAlive()) {
                    // 初始化工作数组、启动工作线程
                    for (int i = 0; i < workers.length; i++) {
                        Worker worker = new Worker();
                        worker.start();
                        workers[i] = worker;
                    }
                    // 启动服务
                    server.start();
                }
            }
        }

        public void stop() throws IOException {
            synchronized (this) {
                if (!server.isInterrupted()) {
                    server.interrupt();
                }
                for (Worker worker : workers) {
                    if (worker.isRunning()) {
                        try {
                            worker.stop();
                        } catch (IOException e) {
                            //
                        }
                    }
                }
                if (this.serverSocketChannel.isOpen()) {
                    this.serverSocketChannel.close();
                }
                if (this.selector.isOpen()) {
                    this.selector.close();
                }
                this.running = false;
            }
        }

        /**
         * 工作线程
         */
        private static class Worker {

            private final Selector selector;
            private final ExecutorService executorService;
            private final Thread workerThread;
            private final AtomicInteger count = new AtomicInteger(0);

            private volatile boolean running = false;

            Worker() throws IOException {
                this.selector = Selector.open();
                this.executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                        Runtime.getRuntime().availableProcessors(), 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(9999),
                        (r) -> new Thread(r, "NIO-" + count.getAndIncrement()), new ThreadPoolExecutor.AbortPolicy());
                this.workerThread = Executors.defaultThreadFactory().newThread(this::run);
            }

            private void run() {
                while (isRunning()) {
                    try {
                        selector.select();
                        // 处理事件
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        for (SelectionKey selectionKey : selectionKeys) {
                            if (selectionKey.isValid()) {
                                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                                try {
                                    if (selectionKey.isReadable()) {
                                        read(socketChannel);
                                    } else if (selectionKey.isWritable()) {
                                        write(socketChannel);
                                    }
                                } catch (Exception e) {
                                    socketChannel.socket().close();
                                }
                            }
                        }
                    } catch (IOException e) {
                        //
                    }
                }
            }

            void register(SocketChannel sc) throws IOException {
                if (isRunning()) {
                    sc.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_CONNECT | SelectionKey.OP_WRITE);
                }
            }

            private void read(SocketChannel sc) {
                executorService.submit(() -> {
                });
            }

            private void write(SocketChannel sc) {
                //
                executorService.submit(() -> {
                });
            }

            boolean isRunning() {
                return running;
            }

            void start() {
                if (isRunning()) {
                    return;
                }
                synchronized (this) {
                    this.running = true;
                    if (!workerThread.isAlive()) {
                        workerThread.start();
                    }
                }
            }

            void stop() throws IOException {
                synchronized (this) {
                    if (!workerThread.isInterrupted()) {
                        workerThread.interrupt();
                    }
                    if (!executorService.isShutdown()) {
                        executorService.shutdown();
                    }
                    if (selector.isOpen()) {
                        selector.close();
                    }
                    this.running = false;
                }
            }

            void wakeup() {
                this.selector.wakeup();
            }
        }

    }
}