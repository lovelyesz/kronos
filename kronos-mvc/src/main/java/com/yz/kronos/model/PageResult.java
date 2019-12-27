package com.yz.kronos.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Builder
@Data
public class PageResult<T> {

    private int pageNum;//当前页数

    private int pageSize;//页大小

    private Long totalSize;//总数据量

    private List<T> list;

    private Object condition;

}
