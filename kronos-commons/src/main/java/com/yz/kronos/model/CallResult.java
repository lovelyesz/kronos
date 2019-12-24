package com.yz.kronos.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author shanchong
 * @date 2019-11-07
 **/
@Data
@Builder
public class CallResult<T> {

    private Integer code;

    private String message;

    private T data;

}
