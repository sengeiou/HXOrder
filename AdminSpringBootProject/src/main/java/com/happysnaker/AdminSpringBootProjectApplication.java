package com.happysnaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author happysnakers
 */
@SpringBootApplication
@EnableTransactionManagement
public class AdminSpringBootProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminSpringBootProjectApplication.class, args);
    }

}
