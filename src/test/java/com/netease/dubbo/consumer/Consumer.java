package com.netease.dubbo.consumer;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.netease.dubbo.service.HelloService;

public class Consumer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "consumer.xml" });
        context.start();

        int count = 100;
        int threadNumber = 3;

        final Semaphore semaphore = new Semaphore(threadNumber);
        final CountDownLatch countDownLatch = new CountDownLatch(count);

        final HelloService service = context.getBean("helloService", HelloService.class);
        ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);
        for (int i = 0; i < count; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {

                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        System.out.println(service.sayHello("Test"));
                    } catch (Exception ex) {
                        System.out.println(Thread.currentThread().getId() + ": " + ex.getClass().getName() + " - "
                                + (ex.getCause() == null ? "" : ex.getCause().getClass().getName()));
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    semaphore.release();

                    countDownLatch.countDown();
                }
            });

        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        context.close();

    }

}
