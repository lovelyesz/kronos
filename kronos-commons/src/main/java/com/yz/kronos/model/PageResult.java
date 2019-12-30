package com.yz.kronos.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Builder
@Data
public class PageResult<T> {

    private Integer code;

    private Long totalSize;//总数据量

    private List<T> list;

    private Object condition;

}
