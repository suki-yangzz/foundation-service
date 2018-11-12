package net.susss.demo.timer;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Suki Yang on 10/11/2018.
 */
public class RedisConsumer extends Thread {

    private int parameter;

    public RedisConsumer(int parameter) {
        this.parameter = parameter;
    }

    @Override
    public void run() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setConnectionPoolSize(10).setConnectionMinimumIdleSize(5);
        RedissonClient client = Redisson.create(config);
        RBlockingQueue<String> blockingQueue = client.getBlockingQueue("delay_queue");
        RDelayedQueue<String> delayedQueue = client.getDelayedQueue(blockingQueue);

        while (true) {
            try {
                String key = blockingQueue.take();
                String[] objects = key.split("%");
                if (client.getScoredSortedSet(objects[0]).contains(objects[1])) {
                    System.out.println("Consumer: " + client.getScoredSortedSet(objects[0]).contains(objects[1]));
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
