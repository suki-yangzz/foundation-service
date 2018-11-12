package net.susss.demo.timer;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Suki Yang on 10/11/2018.
 */
public class RedisProducer implements Runnable {
    private Thread t;
    private String threadName;

    RedisProducer(String name) {
        threadName = name;
        System.out.println("Creating " +  threadName );
    }

    public void run() {
        System.out.println("Running " +  threadName );
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setConnectionPoolSize(10).setConnectionMinimumIdleSize(5);
        RedissonClient client = Redisson.create(config);
        RBlockingQueue<String> blockingQueue = client.getBlockingQueue("delay_queue");
        RDelayedQueue<String> delayedQueue = client.getDelayedQueue(blockingQueue);
        try {
//            for (;;) {
                long timeout = 5000;
                String startTime = String.valueOf(new Date().getTime());
                String uniqueId = "keyA";
                String key = startTime + "%" + uniqueId;
                RScoredSortedSet set = client.getScoredSortedSet(String.valueOf(timeout));
                set.add(Double.valueOf(startTime), key);
                delayedQueue.offer(timeout + "_" + key, timeout, TimeUnit.MILLISECONDS);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            delayedQueue.destroy();
            client.shutdown();
        }
        System.out.println("Thread " +  threadName + " exiting.");
    }

    public void start() {
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}