package com.yz.kronos.from;

import lombok.Data;

/**
 * @author shanchong
 * @date 2019-12-30
 **/
@Data
public class ExecuteLogForm {

    private Integer page = 1;

    private Integer limit = 10;

    private String flowName;

}
