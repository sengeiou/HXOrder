package com.happysnaker;


import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author happysnakers
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableRabbit
@EnableScheduling
public class Start {

    public static void main(String[] args) {
        SpringApplication.run(Start.class, args);
//        Class aClass = Dish.class;
//        aClass.getDeclaredFields();
//        System.out.println(MyBatisUtils.makeResultMap(aClass));
    }

}
