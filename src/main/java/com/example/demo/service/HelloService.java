package com.example.demo.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class HelloService {

    private String script="local store=tonumber(redis.call('get','store')) \n"
            +"if(store<= 0) then return '0' \n"
            +"else store=store-1; end \n"
            +"redis.call('set','store',store) \n"
            +"redis.call('rpush','decrease_time',ARGV[1]) \n"
            +"return '1' ";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void setStore(Integer store){
        stringRedisTemplate.boundValueOps("store").set(store.toString());
    }

    public void decreaseStore(String user_decrease_time){
        RedisScript redisScript=RedisScript.of(script,String.class);
        Integer num=Integer.parseInt((String) Objects.requireNonNull(stringRedisTemplate.execute(redisScript, null,user_decrease_time)));
        if (num.equals(0)){
            System.out.println("库存扣减完毕");
        }
    }

    public String getStore(){
        return stringRedisTemplate.boundValueOps("store").get();
    }

    public List<String> getDecreaseTime(){
        return stringRedisTemplate.boundListOps("decrease_time").range(0,-1);
    }
}