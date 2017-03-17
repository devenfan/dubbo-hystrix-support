package com.netease.dubbo.provider;

import com.netease.dubbo.service.HelloService;

import java.util.Random;

public class HelloServiceImpl implements HelloService {

    Random random = new Random();

    @Override
    public String sayHello(String name) {
        /*  try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        int r = random.nextInt(3);
        if((r % 3) == 1) {
            throw new IllegalStateException();
        }
        return "Hello(" + r + ") " + name;
    }

}
