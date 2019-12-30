package com.yz.kronos.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shanchong
 * @date 2019-12-30
 **/
@RestController
public class HealthController {

    @RequestMapping(value = "track.jsp")
    public String track(){
        return "OK";
    }

}
