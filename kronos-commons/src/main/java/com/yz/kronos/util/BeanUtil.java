package com.yz.kronos.util;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

/**
 * @author shanchong
 * @date 2019-12-09
 **/
public class BeanUtil {

    private static Mapper mapper = new DozerBeanMapper();

    public static <T> T map(Object source, Class<T> destinationClass){
        return mapper.map(source,destinationClass);
    }

}
