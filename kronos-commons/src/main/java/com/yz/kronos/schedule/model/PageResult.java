package com.yz.kronos.schedule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    private Integer code;

    private Long totalSize;

    private List<T> list;

    private Object condition;

}
