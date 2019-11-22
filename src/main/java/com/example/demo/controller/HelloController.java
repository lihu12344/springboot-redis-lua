package com.example.demo.controller;

import com.example.demo.service.HelloService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class HelloController {

    @Resource
    private HelloService helloService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/set")
    public String setStore(){
        helloService.setStore(1000);
        return "success";
    }

    @RequestMapping("/decrease")
    public String decreaseStore(){
        ExecutorService executorService= Executors.newFixedThreadPool(10);

        for (int i=0;i<10000;i++){
            int finalI = i;
            executorService.submit(() ->
                    helloService.decreaseStore("瓜田李下"+ finalI +"-"+System.currentTimeMillis()));
        }
        return "success";
    }

    @RequestMapping("/get")
    public String getStore(){
        String store=helloService.getStore();
        System.out.println("剩余库存为："+store);
        return store;
    }

    @RequestMapping("/get2")
    public List<String> getDecreaseTime(){
        List<String> list=helloService.getDecreaseTime();
        list.forEach(System.out::println);
        System.out.println(list.size());

        return list;
    }

    @RequestMapping("/delete")
    public String delete(){
        stringRedisTemplate.delete("decrease_time");

        return "success";
    }
}