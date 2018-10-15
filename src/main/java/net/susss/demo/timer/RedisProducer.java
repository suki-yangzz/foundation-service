package net.susss.demo.timer;

import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RBucket;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
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
            for (;;) {
                String key = String.valueOf(new Date().getTime());
                key = key + key + key;

                //首先获取redis中的key-value对象，key不存在没关系
                RBucket<String> keyObject = client.getBucket(key);
                //如果key存在，就设置key的值为新值value
                //如果key不存在，就设置key的值为value
                keyObject.set("Processor_Flag");
                delayedQueue.offer(key, 5 * 1000, TimeUnit.MILLISECONDS);
                System.out.println("Thread " + threadName + " Offer " + key + " at Time: "
                        + new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()));
            }
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