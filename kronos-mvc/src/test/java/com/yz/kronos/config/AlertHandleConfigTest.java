package com.yz.kronos.config;

import com.yz.kronos.alert.AlertHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AlertHandleConfigTest {

    @Autowired
    private AlertHandler alertHandler;

    @Test
    public void test(){
        alertHandler.handle(new RuntimeException("测试"));
    }
}
