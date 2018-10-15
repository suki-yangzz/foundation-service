package net.susss.demo.timer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.TaskExecutor;

/**
 * Created by Suki Yang on 10/11/2018.
 */
public class ConsumerTest {

    public void create() {

        ApplicationContext appContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
        TaskExecutor executor = (TaskExecutor) appContext.getBean("taskExecutor");
        for (int i = 0; i < 50; i++) {
            RedisConsumer t = new RedisConsumer(i);
            executor.execute(t);
        }
    }

    public static void main(String args[]) {
        ConsumerTest consumerTest = new ConsumerTest();
        consumerTest.create();
        System.out.println("main process is finish .....");
    }
}
