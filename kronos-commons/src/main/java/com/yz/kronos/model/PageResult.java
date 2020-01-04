package com.yz.kronos.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@Data
@NoArgsConstructor
public class PageResult<T> {

    private Integer code;

    private Long totalSize;//总数据量

    private List<T> list;

    private Object condition;

}
