package com.wwx.study.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.Executors;

/**
 * @author wuwenxi 2022-03-03
 */
public class NioTest {

    private static class Server {
        private final Selector selector;
        private final ServerSocketChannel serverSocketChannel;
        private final Thread server;

        private volatile boolean running = false;

        Server(String hostname, int port) throws IOException {
            this.selector = Selector.open();
            this.serverSocketChannel = ServerSocketChannel.open();
            // 非阻塞
            this.serverSocketChannel.configureBlocking(false);
            // 绑定ip
            this.serverSocketChannel.bind(new InetSocketAddress(hostname, port), 1024);
            this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            // 初始化工作线程
            this.server = Executors.defaultThreadFactory().newThread(this::run);
        }

        private void run() {
            while (isRunning()) {
                try {
                    selector.select(100);
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    for (SelectionKey selectionKey : selectionKeys) {
                        if (selectionKey.isValid()) {
                            // 客户端接入请求信息
                            if (selectionKey.isAcceptable()) {
                                ServerSocketChannel socketChannel = (ServerSocketChannel) selectionKey.channel();
                                SocketChannel sc = socketChannel.accept();
                                sc.configureBlocking(false);
                                // 注册一个读监听
                                socketChannel.register(selector, SelectionKey.OP_READ);
                            }
                            // 服务端接收客户端写请求
                            if (selectionKey.isReadable()) {
                                SocketChannel sc = (SocketChannel) selectionKey.channel();

                                // sc.read()
                            }
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
                if (!server.isAlive()) {
                    server.start();
                    this.running = true;
                }
            }
        }

        public void stop() throws IOException {
            if (!server.isInterrupted()) {
                server.interrupt();
            }
            if (selector.isOpen()) {
                selector.close();
            }
            if (serverSocketChannel.isOpen()) {
                serverSocketChannel.close();
            }
        }

    }

    private static class Client {

    }
}
