package com.yz.meson.demo;

import com.yz.meson.demo.service.Demo1Service;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class Demo1ServiceTest {

    @Autowired
    Demo1Service demo1Service;


    @Test
    public void test() throws Exception {
        demo1Service.execute(0,1,"");
    }
}
