package com.yz.kronos.api;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shanchong
 * @date 2019-12-20
 **/
@RestController
@RequestMapping(value = "/api/errorRecord")
public class ErrorRecordApi {

    @GetMapping(value = "/page")
    public Page page(){

        return null;
    }

}
