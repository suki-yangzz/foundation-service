package net.susss.demo.timer;

import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RBucket;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
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
        System.out.println(Thread.currentThread().getName() + ":执行了..." + parameter);
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setConnectionPoolSize(10).setConnectionMinimumIdleSize(5);
        RedissonClient client = Redisson.create(config);
        RBlockingQueue<String> blockingQueue = client.getBlockingQueue("delay_queue");
        RDelayedQueue<String> delayedQueue = client.getDelayedQueue(blockingQueue);

        while (true) {
            try {
                String key = blockingQueue.take();
                //TODO verify in Redis
                RBucket<String> keyObject = client.getBucket(key);
                System.out.println("Consumer " + parameter + " Takes " + key + "(" + keyObject.get() + ")"
                        + " at Time: " + new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
