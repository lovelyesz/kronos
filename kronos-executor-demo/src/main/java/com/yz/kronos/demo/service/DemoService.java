package com.yz.kronos.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

/**
 * @author shanchong
 * @date 2020-06-12
 **/
@Slf4j
@Service
public class DemoService {

    public void test(){
        IntStream.range(0,10000).forEach(i->{
            log.info("test data {},{}",i,System.currentTimeMillis());
        });
    }

}
