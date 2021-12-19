package com.happysnaker;

import com.happysnaker.mapper.*;
import com.happysnaker.pojo.Message;
import com.happysnaker.service.AuthService;
import com.happysnaker.utils.MyBatisUtils;
import com.happysnaker.utils.TxCosUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class AdminSpringBootProjectApplicationTests {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    StoreMapper storeMapper;

    @Autowired
    ComboMapper comboMapper;

    @Autowired
    TxCosUtils uploadUtils;

    @Autowired
    DishMapper dishMapper;

    @Autowired
    OrderMapper orderMapper;
    @Test
    void contextLoads() throws IOException {
        System.out.println(MyBatisUtils.generateInsertSql(Message.class));
    }


}
