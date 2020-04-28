package com.yz.kronos.schedule.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author shanchong
 * @date 2019-11-07
 **/
@ToString
@Data
@NoArgsConstructor
public class CallResult<T> {

    private Integer code;

    private String msg;

    private T data;

}
