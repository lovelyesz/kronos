package com.yz.kronos.service;

import com.yz.kronos.enu.JobState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ExecuteLogServiceTest {

    @Autowired
    private ExecuteLogService executeLogService;

    @Test
    public void updateStatus() {
        executeLogService.updateStatus(1L, JobState.FAIL);
    }
}
