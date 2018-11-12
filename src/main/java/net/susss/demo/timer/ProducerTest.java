package net.susss.demo.timer;

/**
 * Created by Suki Yang on 10/11/2018.
 */
public class ProducerTest {
    public static void main(String args[]) {
        for (int i = 0; i < 1; i++) {
            RedisProducer r = new RedisProducer( "Thread-" + i);
            r.start();
        }
    }
}
