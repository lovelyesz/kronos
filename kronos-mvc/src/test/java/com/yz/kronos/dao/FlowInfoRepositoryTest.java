package com.yz.kronos.dao;

import com.yz.kronos.schedule.model.FlowInfoModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author shanchong
 * @date 2019-12-19
 **/
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class FlowInfoRepositoryTest {


    @Autowired
    FlowInfoRepository flowInfoRepository;

    @Test
    public void test(){
        final List<FlowInfoModel> list = flowInfoRepository.findAll();
        System.out.println(list);
    }

}
