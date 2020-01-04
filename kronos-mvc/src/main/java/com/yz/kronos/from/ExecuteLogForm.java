package com.yz.kronos.from;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shanchong
 * @date 2019-12-30
 **/
@Data
@NoArgsConstructor
public class ExecuteLogForm {

    private Integer page = 1;

    private Integer limit = 10;

    private String flowName;

}
