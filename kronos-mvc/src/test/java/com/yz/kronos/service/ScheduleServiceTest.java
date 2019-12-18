package com.yz.kronos.service;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ScheduleServiceTest {

    @Autowired
    ScheduleService scheduleService;


    @org.junit.Test
    public void runFlow() {
        scheduleService.runFlow(1l);
    }
}
