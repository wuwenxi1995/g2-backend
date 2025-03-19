package com.wwx.study.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * @author wuwenxi 2022-03-03
 */
public class BioTest {

    private static class Server {
        private final ServerSocket serverSocket;
        private final Thread server;
        private volatile boolean running = false;

        Server(int port) throws IOException {
            this.serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(port));
            this.server = Executors.defaultThreadFactory().newThread(this::run);
        }

        private void run() {
            while (isRunning()) {
                try {
                    Socket accept = serverSocket.accept();
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
                server.start();
                this.running = true;
            }
        }

        public void stop() throws IOException {
            if (!isRunning()) {
                return;
            }
            synchronized (this) {
                if (server.isAlive()) {
                    server.interrupt();
                }
                if (!serverSocket.isClosed()) {
                    serverSocket.close();
                }
                this.running = false;
            }
        }
    }

    private static class Client {
        private final Socket socket;

        Client(String hostname, int port) throws IOException {
            this.socket = new Socket(hostname, port);
        }

        public void start() throws IOException {
            OutputStream outputStream = socket.getOutputStream();
        }

        public void write(byte[] date) throws IOException {
            InputStream inputStream = this.socket.getInputStream();

        }
    }
}
