package com.happysnaker;

import com.happysnaker.exception.SignException;
import com.happysnaker.mapper.ComboMapper;
import com.happysnaker.mapper.DishMapper;
import com.happysnaker.mapper.MessageMapper;
import com.happysnaker.mapper.UserMapper;
import com.happysnaker.service.*;
import com.happysnaker.utils.TokenUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/24
 * @email happysnaker@foxmail.com
 */
@SpringBootTest
public class Test {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private DishMapper mapper1;

    @Autowired
    private ComboMapper mapper2;

    @Autowired
    StoreService sService;

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    private UserMapper mapper3;

    @Autowired
    private TokenUtils tokenUtils;


    @Autowired
    @Qualifier("myRabbitTemplate")
    private RabbitTemplate rabbitTemplate;
    @org.junit.jupiter.api.Test
    public void test() throws IllegalAccessException, NoSuchAlgorithmException, SignException {
        System.out.println(UUID.randomUUID().toString());

    }

    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redis;

    @Autowired
    DishService dishService;

    @Autowired
    UserService userService;

    @Autowired
    OrderService formService;



    @Autowired
    LoginService loginService;

}
