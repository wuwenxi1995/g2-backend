package com.wwx.study.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者消费者模型
 *
 * @author wuwenxi 2022-03-08
 */
public class ProducerCustomerModel {

    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant(5, 2, 20, 4, 80);
        restaurant.start();
    }

    interface ProducerCustomer {
        void start();

        void stop();

        boolean isStop();

        boolean isRunning();
    }

    /**
     * 餐厅
     */
    private static class Restaurant implements ProducerCustomer {
        private final List<Waiter> waiters;
        private final List<Chef> chefs;
        private final PriorityBlockingQueue<Table> tables;
        private final BlockingQueue<Ticket> customerTicket;

        private final Condition lock;
        private final ExecutorService executorService;
        private final CountDownLatch countDownLatch;

        private volatile boolean running = false;

        Restaurant(int waiterNum, int chefNum, int tableNum, int tableSize, int ticketBlockingNum) {
            this.waiters = new ArrayList<>();
            this.chefs = new ArrayList<>();
            this.tables = new PriorityBlockingQueue<>(tableNum);
            // 初始化订单队列
            this.customerTicket = new LinkedBlockingQueue<>(ticketBlockingNum);
            this.lock = new ReentrantLock(true).newCondition();
            this.executorService = Executors.newFixedThreadPool(5);
            this.countDownLatch = new CountDownLatch(waiterNum + chefNum);
            // 初始化waiter
            initWaiters(waiterNum);
            // 初始化chef
            initChefs(chefNum);
            // 初始化table
            initTable(tableNum, tableSize);
        }

        private void initTable(int tableNum, int tableSize) {
            AtomicInteger waiterCount = new AtomicInteger(0);
            for (int i = 0; i < tableNum; i++) {
                Table table = new Table(lock, tableSize, customerTicket, waiters, waiterCount, this);
                this.tables.offer(table);
            }
        }

        private void initChefs(int chefs) {
            for (int i = 0; i < chefs; i++) {
                Chef chef = new Chef("chef-" + i, customerTicket, countDownLatch);
                this.chefs.add(chef);
            }
        }

        private void initWaiters(int waiters) {
            for (int i = 0; i < waiters; i++) {
                Waiter waiter = new Waiter("waiter-" + i, countDownLatch);
                this.waiters.add(waiter);
            }
        }

        @Override
        public void start() {
            synchronized (this) {
                if (isRunning()) {
                    return;
                }
                this.running = true;
            }
            // 启动waiter
            for (Waiter waiter : this.waiters) {
                waiter.start();
            }
            // 启动chef
            for (Chef chef : this.chefs) {
                chef.start();
            }
            doStart();
        }

        private void doStart() {
            Random random = new Random();
            // 模拟用户前来用餐
            while (!isStop()) {
                try {
                    int customerNum = random.nextInt(4) + 1;
                    Table table = tables.take();
                    boolean success = !table.full() && table.newCustomer(customerNum);
                    tables.offer(table);
                    if (success) {
                        continue;
                    }
                    // 阻塞等待有餐桌用户用餐完成或10s后自动唤醒线程
                    lock.await(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    //
                }
            }
        }


        @Override
        public void stop() {
            this.running = false;
            // 先停止chef
            for (Chef chef : chefs) {
                chef.stop();
                if (chef.isStop()) {
                    System.out.println(chef + "完成工作");
                }
            }
            for (; ; ) {
                if (countDownLatch.getCount() == waiters.size()) {
                    break;
                }
            }
            for (Waiter waiter : waiters) {
                waiter.stop();
                if (waiter.isStop()) {
                    System.out.println(waiter + "完成工作");
                }
            }
            if (!executorService.isShutdown()) {
                executorService.shutdown();
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                //
            }
        }

        @Override
        public boolean isStop() {
            return !isRunning();
        }

        @Override
        public boolean isRunning() {
            return running;
        }
    }

    /**
     * 服务员
     */
    private static class Waiter implements ProducerCustomer {

        private volatile boolean running = false;

        private final String no;
        private final BlockingQueue<Ticket> consumer;
        private final CountDownLatch countDownLatch;

        private Waiter(String no, CountDownLatch countDownLatch) {
            this.no = no;
            this.consumer = new LinkedBlockingQueue<>();
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void start() {
            synchronized (this) {
                if (isRunning()) {
                    return;
                }
                this.running = true;
            }
            while (!isStop()) {
                try {
                    Ticket ticket = consumer.take();
                    // 取餐完成，找到对应的顾客
                    Customer customer = ticket.getCustomer();
                    Food food = ticket.getFood();
                    customer.queue().offer(food);
                } catch (InterruptedException e) {
                    //
                }
            }
            countDownLatch.countDown();
        }

        @Override
        public void stop() {
            this.running = false;
        }

        @Override
        public boolean isStop() {
            return !isRunning() && consumer.size() == 0;
        }

        @Override
        public boolean isRunning() {
            return running;
        }

        BlockingQueue<Ticket> getConsumer() {
            return consumer;
        }

        @Override
        public String toString() {
            return no;
        }
    }

    /**
     * 厨房
     */
    private static class Chef implements ProducerCustomer {

        private volatile boolean running = false;

        private final String no;
        private final BlockingQueue<Ticket> customerTicket;
        private final CountDownLatch countDownLatch;

        Chef(String no, BlockingQueue<Ticket> customerTicket, CountDownLatch countDownLatch) {
            this.no = no;
            this.customerTicket = customerTicket;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void start() {
            synchronized (this) {
                if (isRunning()) {
                    return;
                }
                this.running = true;
            }
            while (!isStop()) {
                try {
                    Ticket ticket = this.customerTicket.take();
                    Food food = ticket.getFood();
                    System.out.println("厨房开始制作食物...");
                    food.making();
                    // 制作完成，通知服务员取餐
                    ticket.getWaiter().getConsumer().offer(ticket);
                } catch (Exception e) {
                    //
                }
            }
            countDownLatch.countDown();
        }

        @Override
        public void stop() {
            this.running = false;
        }

        @Override
        public boolean isStop() {
            return !isRunning() && customerTicket.size() == 0;
        }

        @Override
        public boolean isRunning() {
            return running;
        }

        @Override
        public String toString() {
            return no;
        }
    }

    /**
     * 餐桌
     */
    private static class Table implements Comparable<Table> {

        private final int tableSize;
        private AtomicInteger concurrentCustomerSize = new AtomicInteger(0);

        private final BlockingQueue<Ticket> customerTicket;
        private final List<Waiter> waiters;
        private final AtomicInteger waiterCount;
        private final Condition lock;
        private final Restaurant restaurant;

        Table(Condition lock, int tableSize, BlockingQueue<Ticket> customerTicket, List<Waiter> waiters, AtomicInteger waiterCount, Restaurant restaurant) {
            this.tableSize = tableSize;
            this.customerTicket = customerTicket;
            this.waiters = waiters;
            this.waiterCount = waiterCount;
            this.lock = lock;
            this.restaurant = restaurant;
        }

        @Override
        public int compareTo(Table other) {
            // 小的排前面
            return Integer.compare(concurrentCustomerSize.get(), other.concurrentCustomerSize.get());
        }

        boolean full() {
            return concurrentCustomerSize.get() == tableSize;
        }

        boolean newCustomer(int customerNum) {
            // 抢占座位
            for (; ; ) {
                int expect = concurrentCustomerSize.get();
                int update;
                // 如果满座，直接返回
                if ((update = expect + customerNum) > tableSize) {
                    return false;
                }
                if (concurrentCustomerSize.compareAndSet(expect, update)) {
                    break;
                }
            }
            // 下单
            Customer customer = new Customer(this, customerNum);
            Waiter waiter;
            if (waiters.size() == 1) {
                waiter = waiters.get(0);
            } else {
                int expect = this.waiterCount.getAndUpdate(prev -> {
                    int next;
                    return (next = (prev + 1)) == waiters.size() ? 0 : next;
                });
                waiter = this.waiters.get(expect);
            }
            Ticket ticket = new Ticket(customer, waiter);
            ticket.order(new Food(Thread.currentThread().getName()));
            // 提交订单
            this.customerTicket.offer(ticket);
            return true;
        }

        void customerLeave(int customerNum) {
            concurrentCustomerSize.getAndUpdate((prev) -> prev - customerNum);
            boolean full = full();
            synchronized (restaurant) {
                restaurant.tables.remove(this);
                restaurant.tables.offer(this);
            }
            if (full) {
                lock.signal();
            }
        }
    }

    /**
     * 订单
     */
    private static class Ticket {
        private Food food;
        private Customer customer;
        private Waiter waiter;

        Ticket(Customer customer, Waiter waiter) {
            this.customer = customer;
            this.waiter = waiter;
        }

        void order(Food food) {
            this.food = food;
            System.out.println(this);
        }

        Food getFood() {
            return this.food;
        }

        Customer getCustomer() {
            return customer;
        }

        Waiter getWaiter() {
            return waiter;
        }

        @Override
        public String toString() {
            return "Ticket{" +
                    "food=" + food +
                    ", customer=" + customer +
                    ", waiter=" + waiter +
                    '}';
        }
    }

    /**
     * 食物
     */
    private static class Food {

        private String name;

        Food(String name) {
            this.name = name;
        }

        void making() throws Exception {
            TimeUnit.SECONDS.sleep(10);
            System.out.println("食物制作完成");
        }

        @Override
        public String toString() {
            return "Food{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    /**
     * 顾客
     */
    private static class Customer {

        private final Random random = new Random();
        private final Table table;
        private final SynchronousQueue<Food> synchronousQueue = new SynchronousQueue<>(true);
        private final int customerNum;

        Customer(Table table, int customerNum) {
            this.table = table;
            this.customerNum = customerNum;
            eating();
        }

        private void eating() {
            try {
                Food food = synchronousQueue.take();
                System.out.println("开始用餐：" + food);
                TimeUnit.SECONDS.sleep(random.nextInt(10));
                // 用餐完成
                table.customerLeave(customerNum);
            } catch (InterruptedException e) {
                //
            }
        }

        SynchronousQueue<Food> queue() {
            return this.synchronousQueue;
        }
    }
}
